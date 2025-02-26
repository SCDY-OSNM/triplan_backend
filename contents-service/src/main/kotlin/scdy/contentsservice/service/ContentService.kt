package scdy.contentsservice.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import scdy.contentsservice.common.exceptions.NotFoundException
import scdy.contentsservice.dto.ContentLikeResponseDto
import scdy.contentsservice.dto.ContentRequestDto
import scdy.contentsservice.dto.ContentResponseDto
import scdy.contentsservice.entity.Content
import scdy.contentsservice.entity.ContentLike
import scdy.contentsservice.enums.ContentType
import scdy.contentsservice.enums.UserRole
import scdy.contentsservice.exception.AlreadyLikedException
import scdy.contentsservice.exception.ContentNotFoundException
import scdy.contentsservice.exception.NotAllowedAuthException
import scdy.contentsservice.repository.ContentLikeRepository
import scdy.contentsservice.repository.ContentRepository

@Service
@Transactional(readOnly = true)
class ContentService(private var contentRepository: ContentRepository, private var contentLikeRepository: ContentLikeRepository) {

    // 콘텐츠 생성
    @Transactional
    fun createContent(userId: Long, userRole: UserRole, contentRequestDto: ContentRequestDto) :ContentResponseDto{
        if(userRole != UserRole.HOST && userRole != UserRole.ADMIN){
           throw NotAllowedAuthException("생성 권한이 없습니다.")
        }
        val content = Content(
                userId = userId,
                contentName = contentRequestDto.contentName,
                contentType = contentRequestDto.contentType,
                contentExplain = contentRequestDto.contentExplain,
                contentAddress = contentRequestDto.contentAddress,
                contentAmount = contentRequestDto.contentAmount,
                contentLatitude = contentRequestDto.contentLatitude,
                contentLongitude = contentRequestDto.contentLongitude,
                contentGrade = 0,
                contentLike = 0,
                contentPrice = contentRequestDto.contentPrice
        )

        contentRepository.save(content)

        return ContentResponseDto.from(content)
    }

    // 콘텐츠 조회
    fun readContent(contentId: Long) : ContentResponseDto{
        val content  = contentRepository.findById(contentId).orElseThrow{
            ContentNotFoundException("컨텐츠를 찾을 수 없습니다.")
        }

        return ContentResponseDto.from(content)
    }

    // 콘텐츠 수정
    @Transactional
    fun updateContent(userId: Long, userRole : UserRole , contentId : Long, contentRequestDto: ContentRequestDto) : ContentResponseDto{
        val content  = contentRepository.findById(contentId).orElseThrow{
            ContentNotFoundException("컨텐츠를 찾을 수 없습니다.")
        }
        val contentUserId = content.userId
        if(!checkAuth(userRole, userId, contentUserId)){
            throw NotAllowedAuthException("수정 권한이 없습니다.")
        }

        content.updateContent(
                contentName = contentRequestDto.contentName ?: content.contentName,
                contentType = contentRequestDto.contentType ?: content.contentType,
                contentExplain = contentRequestDto.contentExplain ?: content.contentExplain,
                contentAddress =contentRequestDto.contentAddress ?: content.contentAddress,
                contentAmount = contentRequestDto.contentAmount ?: content.contentAmount)

        return ContentResponseDto.from(content)
    }

    // 좋아요
    @Transactional
    fun updateContentLikeUp(userId : Long, contentId: Long) : ContentLikeResponseDto{
        val content  = contentRepository.findById(contentId).orElseThrow{
            ContentNotFoundException("컨텐츠를 찾을 수 없습니다.")
        }
        var contentLike = contentLikeRepository.findByContentAndUserId(content, userId).orElse(null)
        if (contentLike != null) {
            throw AlreadyLikedException("이미 좋아요를 누른 컨텐츠입니다.") // 예외처리

        }else{
            var contentLike = ContentLike(
                    userId = userId,
                    content = content
            )
            content.contentLikeUp()
            contentLikeRepository.save(contentLike)
            return ContentLikeResponseDto.from(contentLike)
        }
    }

    // 좋아요 취소
    @Transactional
    fun updateContentLikeDown(userId: Long, contentId : Long): ContentLikeResponseDto{
        val content  = contentRepository.findById(contentId).orElseThrow{
            ContentNotFoundException("컨텐츠를 찾을 수 없습니다.")
        }
        var contentLike = contentLikeRepository.findByContentAndUserId(content, userId).orElse(null)
        if(contentLike == null){
            throw NotFoundException("좋아요를 누르지 않은 컨텐츠입니다.")
        }else{
            contentLikeRepository.deleteById(requireNotNull(contentLike.id) { "Like ID가 null입니다." })
            content.contentLikeDown()
        }
        return ContentLikeResponseDto.from(contentLike)
    }

    // 사용자 별 좋아요 컨텐츠
    fun readLikedContentByUser(userId : Long): List<ContentResponseDto> {
        var contentLikeList = contentLikeRepository.findByUserId(userId)
        var contentList = contentLikeList.map{it.content}
        return contentList.map{ ContentResponseDto.from(it) }
    }

    // 컨텐츠 삭제
    @Transactional
    fun deleteContent(userRole: UserRole, userId: Long, contentId: Long) : ContentResponseDto{
        val content  = contentRepository.findById(contentId).orElseThrow{
            ContentNotFoundException("컨텐츠를 찾을 수 없습니다.")
        }

        val contentUserId = content.userId
        if(!checkAuth(userRole, userId, contentUserId)){
            throw NotAllowedAuthException("삭제 권한이 없습니다.")
        }
        contentRepository.deleteById(contentId)

        return ContentResponseDto.from(content)
    }

    // 타입별 컨텐츠 목록 조회
    @Transactional
    fun readBoardByType(contentType: ContentType) : List<ContentResponseDto>{
        val contents = contentRepository.findByContentType(contentType)
        if(contents.isEmpty()){
            throw ContentNotFoundException("콘텐츠가 존재하지 않습니다.")
        }
        return contents.map{ContentResponseDto.from(it)} // 코틀린은 map함수에 중괄호
    }


    fun checkAuth(userRole : UserRole, userId : Long, contentUserId : Long) : Boolean{
        return userId == contentUserId || userRole == UserRole.ADMIN
    } // 현재 컨텐츠 등록자가 맞는지 / 관리자인지

}


