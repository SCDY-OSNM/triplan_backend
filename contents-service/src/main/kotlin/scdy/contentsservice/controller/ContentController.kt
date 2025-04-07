package scdy.contentsservice.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
@RequestMapping("/api/v1/contents")
class ContentController(private val contentService: ContentService) {

    // 콘텐츠 생성
    @PostMapping
    fun createContent(@RequestHeader("X-Authenticated-User") userId: Long, @RequestHeader ("X-User-Role") userRole: String, @RequestBody contentRequestDto: ContentRequestDto
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
            @RequestHeader("X-Authenticated-User") userId: Long, @RequestHeader("X-User-Role") userRole: UserRole,
            @PathVariable contentId: Long, @RequestBody contentRequestDto: ContentRequestDto
    ): ResponseEntity<ApiResponse<ContentResponseDto>> {
        val contentResponseDto = contentService.updateContent(userId, userRole, contentId, contentRequestDto)
        return ResponseEntity.ok(ApiResponse.success("콘텐츠 수정 성공", contentResponseDto))
    }

    // 콘텐츠 삭제
    @DeleteMapping("/{contentId}")
    fun deleteContent(
            @RequestHeader("X-Authenticated-User") userId: Long,
            @RequestHeader("X-User-Role") userRole: UserRole,
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
    /*
    // 타입별 콘텐츠 조회
    @GetMapping("/type")
    fun getContentsByType(@RequestParam contentType: ContentType): ResponseEntity<ApiResponse<List<ContentResponseDto>>> {
        val contents = contentService.readBoardByType(contentType)
        return ResponseEntity.ok(ApiResponse.success("타입별 콘텐츠 조회 성공", contents))
    }

    // Es 컨텐츠 생성
    @PostMapping("/es")
    fun createEsContent(@RequestHeader("X-Authenticated-User") userId: Long, @RequestHeader ("X-User-Role") userRole: String, @RequestBody contentRequestDto: ContentRequestDto
    ): ResponseEntity<ApiResponse<ContentResponseDto>> {
        val contentResponseDto = contentService.createContentEs(userId, userRole, contentRequestDto)
        return ResponseEntity.ok(ApiResponse.success("ES 콘텐츠 생성 성공", contentResponseDto))
    }

    // ES 컨텐츠 단일 조회
    @GetMapping("/es/{contentId}")
    fun getEsContentById(@PathVariable("contentId") contentId : Long) : ResponseEntity<ApiResponse<ContentResponseDto>> {
        val contentResponseDto = contentService.readContentEs(contentId)
        return ResponseEntity.ok(ApiResponse.success("ES 컨텐츠 조회 완료", contentResponseDto))
    }
    */
    // ES 컨텐츠 이름 조회
    @GetMapping("/es/name-search")
    fun getEsContentByName(@RequestParam("contentName") contentName : String, pageable: Pageable): ResponseEntity<ApiResponse<Page<ContentResponseDto>>>{
        val contentList = contentService.readContentsByNameEs(contentName, pageable)
        return ResponseEntity.ok(ApiResponse.success("ES 컨텐츠 이름으로 조회 완료", contentList))
    }

    // ES 컨텐츠 타입 조회
    @GetMapping("/es/type-search")
    fun getEsContentByType(@RequestParam("contentType") contentType: ContentType, pageable: Pageable): ResponseEntity<ApiResponse<Page<ContentResponseDto>>>{
        val contentList = contentService.readContentsByTypeEs(contentType, pageable)
        return ResponseEntity.ok(ApiResponse.success("ES 컨텐츠 타입으로 조회 완료", contentList))
    }

    // ES 컨텐츠 내용 조회
    @GetMapping("/es/explain-search")
    fun getEsContentsByExplain(@RequestParam("contentExplainKeyword") contentExplainKeyword : String, pageable: Pageable) : ResponseEntity<ApiResponse<Page<ContentResponseDto>>> {
        val contentList = contentService.readingContentsByExplain(contentExplainKeyword, pageable)
        return ResponseEntity.ok(ApiResponse.success("Es 컨텐츠 설명으로 조회 완료", contentList))
    }

}
