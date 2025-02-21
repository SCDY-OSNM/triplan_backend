package scdy.contentsservice.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import scdy.contentsservice.common.exceptions.ForbiddenException
import scdy.contentsservice.common.exceptions.NotFoundException
import scdy.contentsservice.dto.ContentRequestDto
import scdy.contentsservice.dto.ContentResponseDto
import scdy.contentsservice.entity.Content
import scdy.contentsservice.enums.UserRole
import scdy.contentsservice.repository.ContentRepository

@Service
@Transactional(readOnly = true)
class ContentService(private var contentRepository: ContentRepository) {

    @Transactional
    fun createContent(userId: Long, userRole: UserRole, contentRequestDto: ContentRequestDto) :ContentResponseDto{
        if(userRole != UserRole.HOST && userRole != UserRole.ADMIN){
           throw ForbiddenException("생성 권한이 없습니다.") // 커스텀으로 바꿔주기
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

    fun readContent(contentId: Long) : ContentResponseDto{
        val content = contentRepository.findByIdOrElseThrow(contentId);

        return ContentResponseDto.from(content);
    }

    @Transactional
    fun updateContent(userId: Long, userRole : UserRole , contentId : Long, contentRequestDto: ContentRequestDto) : ContentResponseDto{
        val content = contentRepository.findByIdOrElseThrow(contentId)
        val contentUserId = content.userId
        if(!checkAuth(userRole, userId, contentUserId)){
            throw ForbiddenException("수정 권한이 없습니다.") //커스텀
        }

        content.updateContent(contentRequestDto.contentName, contentRequestDto.contentType, contentRequestDto.contentExplain, contentRequestDto.contentAddress, contentRequestDto.contentAmount)

        return ContentResponseDto.from(content)
        }
        // 변경되지 않은 값이 있을 때 기존 값으로 유지되게 코딩해야되나요?

    @Transactional
    fun deleteContent(userRole: UserRole, userId: Long, contentId: Long) : ContentResponseDto{
        val content  = contentRepository.findByIdOrElseThrow(contentId)
        val contentUserId = content.userId
        if(!checkAuth(userRole, userId, contentUserId)){
            throw ForbiddenException("삭제 권한이 없습니다.") //커스텀
        }
        contentRepository.deleteById(contentId)

        return ContentResponseDto.from(content);
    }


    fun checkAuth(userRole : UserRole, userId : Long, contentUserId : Long) : Boolean{
        return userId == contentUserId || userRole == UserRole.ADMIN
    }// 현재 컨텐츠 등록자가 맞는지 / 관리자인지


}

