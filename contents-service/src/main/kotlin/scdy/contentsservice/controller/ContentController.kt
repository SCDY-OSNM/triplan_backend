package scdy.contentsservice.controller

import com.google.protobuf.Api
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import scdy.contentsservice.common.advice.ApiResponse
import scdy.contentsservice.dto.ContentRequestDto
import scdy.contentsservice.dto.ContentResponseDto
import scdy.contentsservice.enums.UserRole
import scdy.contentsservice.service.ContentService

@RestController
@RequestMapping("/api/contents")
class ContentController(private val contentService: ContentService) {

    @PostMapping
    fun createContent(@RequestHeader("X-Authenticated-User") userId: Long, userRole: UserRole, @RequestBody contentRequestDto: ContentRequestDto): ResponseEntity<ApiResponse<ContentResponseDto>> {
        val contentResponse = contentService.createContent(userId, userRole, contentRequestDto)
        return ResponseEntity.ok(ApiResponse.success("생성 성공", contentResponse))
    }

    @GetMapping("/{contentId}")
    fun readContent(@PathVariable contentId: Long): ResponseEntity<ApiResponse<ContentResponseDto>> {
        val contentResponse = contentService.readContent(contentId)
        return ResponseEntity.ok(ApiResponse.success("조회 성공",contentResponse ))
    }

    @PutMapping("/{contentId}")
    fun updateContent(@RequestHeader("X-Authenticated-User") userId: Long, userRole: UserRole, @PathVariable contentId: Long, @RequestBody contentRequestDto: ContentRequestDto): ResponseEntity<ApiResponse<ContentResponseDto>> {
        val contentResponse = contentService.updateContent(userId, userRole, contentId, contentRequestDto)
        return ResponseEntity.ok(ApiResponse.success("수정 성공", contentResponse))
    }

    @DeleteMapping("/{contentId}")
    fun deleteContent(@RequestHeader("X-Authenticated-User") userRole: UserRole, userId: Long, @PathVariable contentId: Long): ResponseEntity<ApiResponse<ContentResponseDto>> {
        val contentResponse = contentService.deleteContent(userRole, userId, contentId)
        return ResponseEntity.ok(ApiResponse.success("삭제 성공", contentResponse))
    }
}
