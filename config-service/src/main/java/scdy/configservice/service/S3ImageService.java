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
import scdy.configservice.entity.DeletedImageLog;
import scdy.configservice.repository.DeletedImageLogRepository;

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

    private final AmazonS3 amazonS3;
    private final DeletedImageLogRepository deletedImageLogRepository;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    //이미지 업로드
    public String upload(MultipartFile image, String s3Key) {

        return this.uploadImage(image, s3Key);
    }

    //URL 받아서 서비스에 반환
    private String uploadImage(MultipartFile image, String s3Key) {
        try {
            return this.uploadImageToS3(image, s3Key);
        } catch (IOException e) {
            throw new S3Exception("업로드 실패");
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
    public void deleteImageFromS3(String s3Key){

        try{
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, s3Key));
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

    public boolean doesImageExist(String S3Key){

        try{
            return amazonS3.doesObjectExist(bucketName, S3Key);
        }catch (Exception e){
            log.error("S3 객체 존재 여부 확인 중 오류 발생: {}", e.getMessage(), e);
            throw new S3Exception("S3 연결 오류 발생");
        }
    }

    //이미지 삭제 오류 로그 저장
    @Transactional
    public void saveDeleteErrorLog(String S3Key){

        DeletedImageLog deletedImageLog = new DeletedImageLog(S3Key);
        deletedImageLogRepository.save(deletedImageLog);
    }



}
