package scdy.boardservice.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import scdy.boardservice.dto.CommentRequestDto
import scdy.boardservice.dto.CommentResponseDto
import scdy.boardservice.entity.Board
import scdy.boardservice.entity.Comment
import scdy.boardservice.exception.BoardNotFoundException
import scdy.boardservice.exception.NotFoundPermissionException
import scdy.boardservice.repository.BoardRepository
import scdy.boardservice.repository.CommentRepository
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class CommentService(private val commentRepository: CommentRepository, private val boardRepository: BoardRepository) {

    //create comment
    @Transactional
    fun createComment(commentRequestDto: CommentRequestDto, userId : Long): CommentResponseDto {

        val board = boardRepository.findById(commentRequestDto.boardId).orElseThrow {
            BoardNotFoundException("존재하지 않는 게시글입니다")
        }

        val comment = Comment(
            userId = userId,
            contents = commentRequestDto.contents,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            board = board,
        )

        val saved = commentRepository.save(comment)
        return CommentResponseDto.from(saved)
    }

    //read comment by boardId
    fun readCommentByBoardId(boardId: Long, pageable: Pageable): Page<CommentResponseDto> {

        getBoard(boardId)

        val commentPage = commentRepository.findCommentListByBoardId(boardId, pageable)
        return commentPage.map{CommentResponseDto.from(it)}
    }

    //read comment by userId
    fun readCommentByUserId(userId: Long, pageable: Pageable): Page<CommentResponseDto> {

        val commentPage = commentRepository.findCommentListByUserId(userId, pageable)
        return commentPage.map{CommentResponseDto.from(it)}
    }

    //update comment
    //Admin 및 작성자만 수정 가능
    @Transactional
    fun updateComment(commentRequestDto: CommentRequestDto, commentId: Long, userId:Long, userRole: String): CommentResponseDto {

        val comment = commentRepository.findByIdOrElseThrow(commentId)

        if( !isAdmin(userRole) && !isOwner(commentId, userId)){
            throw NotFoundPermissionException("수정 권한이 없는 사용자입니다.")
        }

        comment.update(commentRequestDto.contents)
        return CommentResponseDto.from(comment)
    }

    //delete comment
    //ADMIN 및 작성자만 삭제 가능
    @Transactional
    fun deleteComment(commentId: Long, userId:Long, userRole: String): Boolean{

        val comment = commentRepository.findByIdOrElseThrow(commentId)

        if( !isAdmin(userRole) && !isOwner(commentId, userId)){
            throw NotFoundPermissionException("삭제 권한이 없는 사용자입니다")
        }

        commentRepository.delete(comment)
        return true
    }



    fun getBoard(boardId: Long): Board {
        return boardRepository.findByIdOrElseThrow(boardId)
    }

    fun isAdmin(userRole: String): Boolean{
        return userRole == "ADMIN"
    }

    fun isOwner(commentUserId: Long, requesterId: Long): Boolean{
        return commentUserId == requesterId
    }
}