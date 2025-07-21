package scdy.configservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import scdy.configservice.Exception.FileSizeOverException;
import scdy.configservice.Exception.S3Exception;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3ImageService {

    //TODO: 매직넘버 검증 추가
    //TODO: 고아객체 검증 추가

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    //이미지 업로드
    //업로드 파일 명: YYMMDD-HH:mm:ss_원본파일명.확장자
    //용량 제한 5MB
    @Transactional
    public String upload(MultipartFile image){

        if(image.isEmpty() || Objects.isNull(image.getOriginalFilename())){
            throw new S3Exception("빈 파일입니다.");
        }

        //validation file size
        final long MAX_FILE_SIZE = 5 * 1024 * 1024;
        if(image.getSize() > MAX_FILE_SIZE){
            throw new FileSizeOverException("파일 크기가 5MB를 초과합니다.");
        }

        //원본 파일명 가져오기
        String originFileName = image.getOriginalFilename();

        //새 파일명 형식 생성
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd-HH:mm:ss");
        String timestamp = now.format(formatter);

        //새 파일명
        String s3Key = timestamp + "_" + originFileName;

        return this.uploadImage(image, s3Key);


    }

    //URL 받아서 서비스에 반환
    private String uploadImage(MultipartFile image, String s3Key) {
        this.validateImageFileExtension(image);
        try {
            return this.uploadImageToS3(image, s3Key);
        } catch (IOException e) {
            throw new S3Exception("업로드 실패");
        }
    }

    //확장자 및 MIME 검증
    private void validateImageFileExtension(MultipartFile image) {

        String originalFilename = image.getOriginalFilename();
        String contentType = image.getContentType();

        //확장자 검증
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new S3Exception("잘못된 파일입니다(확장자 없음)");
        }

        String extension = originalFilename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif");

        if (!allowedExtentionList.contains(extension)) {
            throw new S3Exception("지원하지 않는 형식입니다.");
        }

        //MIME 검증
        List<String> allowedMimeTypes = Arrays.asList("image/jpeg", "image/png", "image/gif");
        if (contentType == null || !allowedMimeTypes.contains(contentType.toLowerCase())) {
            throw new S3Exception("지원하지 않는 파일 형식(MIME Type)입니다: " + contentType);
        }

        //확장자와 MIME Type 불일치 검사: 예를 들어 확장자는 jpg인데 MIME Type이 text/plain인 경우
        if (contentType.toLowerCase().contains("jpeg") && !extension.equals("jpeg") && !extension.equals("jpg")) {
            // throw new S3Exception("파일 확장자와 MIME Type이 일치하지 않습니다.");
        }
    }

    //S3에 업로드 실행
    private String uploadImageToS3(MultipartFile image, String s3Key) throws IOException {

        InputStream is = null;

        try {
            is = image.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(image.getSize());
            metadata.setContentType(image.getContentType());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3Key, is, metadata);

            amazonS3.putObject(putObjectRequest);

            return amazonS3.getUrl(bucketName, s3Key).toString();
        }catch(IOException e){
            log.error(String.valueOf(e));
            throw new S3Exception("파일 스트림 처리 중 오류 발생");
        }catch(Exception e){
            log.error(String.valueOf(e));
            throw new S3Exception("S3 업로드 중 예외 발생");
        }finally {
            if(is != null){
                //스트림 닫기
                is.close();
            }
        }
    }

    //이미지 삭제
    @Transactional
    public void deleteImageFromS3(String imageAddress){
        String key = getKeyFromImageAddress(imageAddress);
        try{
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        }catch (Exception e){
            throw new S3Exception("이미지 삭제 실패");
        }
    }


    private String getKeyFromImageAddress(String imageAddress){
        try{
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1); // 맨 앞의 '/' 제거
        }catch (MalformedURLException | UnsupportedEncodingException e){
            throw new S3Exception("예외 발생");
        }
    }

}
