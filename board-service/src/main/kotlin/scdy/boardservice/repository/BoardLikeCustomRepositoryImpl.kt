package scdy.boardservice.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import scdy.boardservice.entity.BoardLike
import scdy.boardservice.entity.QBoardLike
import java.util.*

class BoardLikeCustomRepositoryImpl(
    private val queryFactory: JPAQueryFactory
): BoardLikeCustomRepository {

    var boardLike : QBoardLike = QBoardLike.boardLike

    override fun findNumberByBoardId(boardId: Long): Int {

        return queryFactory
            .select(boardLike.count())
            .from(boardLike)
            .where(boardLike.board.boardId.eq(boardId))
            .fetchOne()?.toInt() ?: -1

    }

    override fun findByUserIdAndId(userId: Long, boardId: Long): Optional<BoardLike> {

        val result =  queryFactory
            .selectFrom(boardLike)
            .where(boardLike.board.boardId.eq(boardId), boardLike.userId.eq(userId))
            .fetchOne()

        return Optional.ofNullable(result)

    }

    override fun findByUserId(userId: Long): BoardLike? {

        return queryFactory
            .selectFrom(boardLike)
            .where(boardLike.userId.eq(userId))
            .fetchOne()

    }
}