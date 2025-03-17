package scdy.boardservice.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.*
import scdy.boardservice.entity.Comment
import scdy.boardservice.entity.QComment
import scdy.boardservice.exception.BoardNotFoundException

class CommentCustomRepositoryImpl(
    private val queryFactory: JPAQueryFactory
): CommentCustomRepository {

    private var comment: QComment = QComment.comment

    override fun findCommentListByBoardId(boardId: Long, pageable: Pageable): Page<Comment> {

        val result =  queryFactory
            .selectFrom(comment)
            .where(comment.board.boardId.eq(boardId))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(comment.count())
            .from(comment)
            .where(comment.board.boardId.eq(boardId))
            .fetchOne() ?: 0L

        return PageImpl(result, pageable, total)
    }

    override fun findCommentListByUserId(userId: Long, pageable: Pageable): Page<Comment> {

        val result =  queryFactory
            .selectFrom(comment)
            .where(comment.userId.eq(userId))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(comment.count())
            .from(comment)
            .where(comment.userId.eq(userId))
            .fetchOne() ?: 0L

        return PageImpl(result, pageable, total)
    }

    override fun findByIdOrElseThrow(commentId: Long): Comment {

        val result = queryFactory
            .select(comment)
            .where(comment.commentId.eq(commentId))
            .fetchFirst()

        if(result == null) {
            throw BoardNotFoundException("존재하지 않는 댓글입니다.")
        }

        return result
    }
}