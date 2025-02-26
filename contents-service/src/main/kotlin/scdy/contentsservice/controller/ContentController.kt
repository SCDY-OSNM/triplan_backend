package scdy.contentsservice.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import scdy.contentsservice.common.advice.ApiResponse
import scdy.contentsservice.dto.ContentRequestDto
import scdy.contentsservice.dto.ContentResponseDto
import scdy.contentsservice.dto.ContentLikeResponseDto
import scdy.contentsservice.enums.ContentType
import scdy.contentsservice.enums.UserRole
import scdy.contentsservice.service.ContentService

@RestController
@RequestMapping("/contents")
class ContentController(private val contentService: ContentService) {

    // 콘텐츠 생성
    @PostMapping
    fun createContent(@RequestHeader("X-Authenticated-User") userId: Long, userRole: UserRole, @RequestBody contentRequestDto: ContentRequestDto
    ): ResponseEntity<ApiResponse<ContentResponseDto>> {
        val contentResponseDto = contentService.createContent(userId, userRole, contentRequestDto)
        return ResponseEntity.ok(ApiResponse.success("콘텐츠 생성 성공", contentResponseDto))
    }

    // 콘텐츠 조회
    @GetMapping("/{contentId}")
    fun readContent(@PathVariable contentId: Long): ResponseEntity<ApiResponse<ContentResponseDto>> {
        val contentResponseDto = contentService.readContent(contentId)
        return ResponseEntity.ok(ApiResponse.success("콘텐츠 조회 성공", contentResponseDto))
    }

    // 콘텐츠 수정
    @PutMapping("/{contentId}")
    fun updateContent(
            @RequestHeader("X-Authenticated-User") userId: Long, @RequestHeader("X-Authenticated-Role") userRole: UserRole,
            @PathVariable contentId: Long, @RequestBody contentRequestDto: ContentRequestDto
    ): ResponseEntity<ApiResponse<ContentResponseDto>> {
        val contentResponseDto = contentService.updateContent(userId, userRole, contentId, contentRequestDto)
        return ResponseEntity.ok(ApiResponse.success("콘텐츠 수정 성공", contentResponseDto))
    }

    // 콘텐츠 삭제
    @DeleteMapping("/{contentId}")
    fun deleteContent(
            @RequestHeader("X-Authenticated-User") userId: Long,
            @RequestHeader("X-Authenticated-Role") userRole: UserRole,
            @PathVariable contentId: Long
    ): ResponseEntity<ApiResponse<ContentResponseDto>> {
        val contentResponseDto = contentService.deleteContent(userRole, userId, contentId)
        return ResponseEntity.ok(ApiResponse.success("콘텐츠 삭제 성공", contentResponseDto))
    }

    // 좋아요 추가
    @PostMapping("/{contentId}/like")
    fun likeContent(@RequestHeader("X-Authenticated-User") userId: Long, @PathVariable contentId: Long): ResponseEntity<ApiResponse<ContentLikeResponseDto>> {
        val contentLikeResponseDto = contentService.updateContentLikeUp(userId, contentId)
        return ResponseEntity.ok(ApiResponse.success("좋아요 추가 성공", contentLikeResponseDto))
    }

    // 좋아요 취소
    @DeleteMapping("/{contentId}/like")
    fun unlikeContent(@RequestHeader("X-Authenticated-User") userId: Long, @PathVariable contentId: Long): ResponseEntity<ApiResponse<ContentLikeResponseDto>> {
        val contentLikeResponseDto = contentService.updateContentLikeDown(userId, contentId)
        return ResponseEntity.ok(ApiResponse.success("좋아요 취소 성공", contentLikeResponseDto))
    }

    // 사용자가 좋아요한 콘텐츠 조회
    @GetMapping("/liked")
    fun getLikedContents(@RequestHeader("X-Authenticated-User") userId: Long): ResponseEntity<ApiResponse<List<ContentResponseDto>>> {
        val likedContents = contentService.readLikedContentByUser(userId)
        return ResponseEntity.ok(ApiResponse.success("좋아요한 콘텐츠 조회 성공", likedContents))
    }

    // 타입별 콘텐츠 조회
    @GetMapping("/type")
    fun getContentsByType(@RequestParam contentType: ContentType): ResponseEntity<ApiResponse<List<ContentResponseDto>>> {
        val contents = contentService.readBoardByType(contentType)
        return ResponseEntity.ok(ApiResponse.success("타입별 콘텐츠 조회 성공", contents))
    }
}
