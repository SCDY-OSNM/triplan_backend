package scdy.boardservice.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import scdy.boardservice.entity.Comment

interface CommentCustomRepository {

    fun findCommentListByBoardId(boardId:Long, pageable: Pageable) : Page<Comment>

    fun findCommentListByUserId(userId: Long, pageable: Pageable) : Page<Comment>

    fun findByIdOrElseThrow(commentId: Long) : Comment
}