package scdy.boardservice.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import scdy.boardservice.common.advice.ApiResponse
import scdy.boardservice.dto.BoardLikeResponseDto
import scdy.boardservice.dto.BoardRequestDto
import scdy.boardservice.dto.BoardResponseDto
import scdy.boardservice.dto.BoardUpdateRequestDto
import scdy.boardservice.enums.BoardCategory
import scdy.boardservice.service.BoardService

@RestController
@RequestMapping("api/v1/boards")
class BoardController(private val boardService: BoardService) {

    //게시글 생성
    @PostMapping()
    fun postBoard(@RequestHeader("X-Authenticated-User") userId: Long,
                  @RequestHeader("X-User-Role") userRole: String,
                  @RequestBody boardRequestDto: BoardRequestDto
    ): ResponseEntity<ApiResponse<BoardResponseDto>> {

        val boardResponseDto = boardService.createBoard(boardRequestDto, userId, userRole)

        return ResponseEntity.ok(ApiResponse.success("게시글 생성 성공", boardResponseDto))
    }


    //단일 게시글 읽기
    @GetMapping("/{boardId}")
    fun getBoard(@PathVariable boardId: Long): ResponseEntity<ApiResponse<BoardResponseDto>> {

        val boardResponseDto = boardService.readBoard(boardId)

        return ResponseEntity.ok(ApiResponse.success("게시글 조회 성공", boardResponseDto))
    }

    //카테고리 별 게시글 읽기
    @GetMapping("/category/{category}")
    fun getBoardByCategory(@PathVariable category: BoardCategory,
                           pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<BoardResponseDto>>> {

        val boardResponseDtoPage = boardService.getBoardByCategory(category, pageable)

        return ResponseEntity.ok(ApiResponse.success("카테고리 조회 성공", boardResponseDtoPage))
    }

    //사용자별 게시글 조회
    @GetMapping("/user/{userId}")
    fun getBoardByUserId(@PathVariable userId: Long,
                         pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<BoardResponseDto>>> {

        val boardResponseDtoPage = boardService.getBoardByUserId(userId, pageable)

        return ResponseEntity.ok(ApiResponse.success("사용자별 게시글 조회 성공", boardResponseDtoPage))
    }

    //게시글 수정
    @PatchMapping("/{boardId}")
    fun updateBoard(@RequestHeader("X-Authenticated-User") userId: Long,
                    @RequestHeader("X-User-Role") userRole: String,
                    @PathVariable boardId: Long,
                    @RequestBody boardUpdateRequestDto: BoardUpdateRequestDto
    ): ResponseEntity<ApiResponse<BoardResponseDto>> {

        val boardResponseDto = boardService.updateBoard(boardUpdateRequestDto, boardId, userId, userRole)

        return ResponseEntity.ok(ApiResponse.success("게시글 수정 성공", boardResponseDto))
    }

    //게시글 삭제
    @DeleteMapping("/{boardId}")
    fun deleteBoard(@RequestHeader("X-Authenticated-User") userId: Long,
                    @RequestHeader("X-User-Role") userRole: String,
                    @PathVariable boardId: Long
    ) : ResponseEntity<ApiResponse<Unit>>{

        boardService.deleteBoard(boardId, userId, userRole)

        return ResponseEntity.ok(ApiResponse.success("게시글 삭제 성공"))
    }

    //게시글 좋아요
    @GetMapping("/{boardId}/like")
    fun likeBoard(@RequestHeader("X-Authenticated-User") userId: Long,
                  @PathVariable boardId: Long
    ): ResponseEntity<ApiResponse<BoardLikeResponseDto>> {

        val boardLikeResponseDto = boardService.likeBoard(boardId, userId)

        return ResponseEntity.ok(ApiResponse.success("좋아요 성공", boardLikeResponseDto ))
    }

    @GetMapping("/{boardId}/cancel-like")
    fun cancelLike(@RequestHeader("X-Authenticated-User") userId: Long,
                   @PathVariable boardId: Long
    ): ResponseEntity<ApiResponse<Unit>> {

        boardService.cancelLikeBoard(boardId, userId)

        return ResponseEntity.ok(ApiResponse.success("좋아요 취소 성공"))
    }

    @GetMapping("/{boardId}/likes")
    fun getNumOfLikes(@PathVariable boardId: Long): ResponseEntity<ApiResponse<Int>> {

        val numOfLikes = boardService.getNumOfBoardLikes(boardId)

        return ResponseEntity.ok(ApiResponse.success("좋아요 개수 조회 성공", numOfLikes))
    }

}