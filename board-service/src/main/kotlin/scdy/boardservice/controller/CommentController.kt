package scdy.boardservice.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import scdy.boardservice.common.advice.ApiResponse
import scdy.boardservice.dto.CommentRequestDto
import scdy.boardservice.dto.CommentResponseDto
import scdy.boardservice.service.CommentService

@RestController
@RequestMapping("/api/v1/comments")
class CommentController(private val commentService: CommentService) {

    //댓글 생성
    @PostMapping()
    fun postComment(@RequestHeader("X-Authenticated-User") userId: Long,
                    @RequestBody commentRequestDto: CommentRequestDto
    ): ResponseEntity<ApiResponse<CommentResponseDto>>{

        val commentResponseDto = commentService.createComment(commentRequestDto, userId)

        return ResponseEntity.ok(ApiResponse.success("댓글 생성 성공", commentResponseDto))
    }

    //게시글 ID로 댓글 조회
    @GetMapping("/board/{boardId}")
    fun getCommentByBoardId(@RequestHeader("X-Authenticated-User") userId: Long,
                            @PathVariable boardId: Long,
                            pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<CommentResponseDto>>>{

        val commentPage = commentService.readCommentByBoardId(boardId, pageable)

        return ResponseEntity.ok(ApiResponse.success("게시물 댓글 조회 성공", commentPage))
    }

    //사용자 ID로 댓글 조회
    @GetMapping("/user/{userId}")
    fun getCommentByUserId(@RequestHeader("X-Authenticated-User") userId: Long,
                           @PathVariable requestUserId: Long,
                           pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<CommentResponseDto>>>{

        val commentPage = commentService.readCommentByUserId(requestUserId, pageable)

        return ResponseEntity.ok(ApiResponse.success("사용자 댓글 조회 성공", commentPage))
    }

    //댓글 수정
    @PatchMapping("/{commentId}")
    fun updateComment(@RequestHeader("X-Authenticated-User") userId: Long,
                      @RequestHeader("X-User-Role") userRole: String,
                      @PathVariable commentId: Long,
                      @RequestBody commentRequestDto: CommentRequestDto
    ): ResponseEntity<ApiResponse<CommentResponseDto>>{

        val comment = commentService.updateComment(commentRequestDto, commentId, userId, userRole)

        return ResponseEntity.ok(ApiResponse.success("댓글 수정 성공", comment))
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    fun deleteComment(@RequestHeader("X-Authenticated-User") userId: Long,
                      @RequestHeader("X-User-Role") userRole: String,
                      @PathVariable commentId: Long
    ): ResponseEntity<ApiResponse<Unit>>{
        commentService.deleteComment(commentId, userId, userRole)

        return ResponseEntity.ok(ApiResponse.success("댓글 삭제 성공"))
    }
}