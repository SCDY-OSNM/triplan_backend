package scdy.boardservice.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.*
import org.springframework.stereotype.Repository
import scdy.boardservice.entity.Board
import scdy.boardservice.entity.QBoard
import scdy.boardservice.enums.BoardCategory
import scdy.boardservice.exception.BoardNotFoundException


@Repository
class BoardCustomRepositoryImpl(
    private val queryFactory: JPAQueryFactory
): BoardCustomRepository {


    //Reservation 엔티티의 속성을 쿼리에서 사용하기 위해 인스턴스를 생성
    var board: QBoard = QBoard.board

    override fun findBoardByBoardCategory(boardCategory: BoardCategory, pageable: Pageable): Page<Board> {

        val results = queryFactory
            .selectFrom(board)
            .where(board.boardCategory.eq(boardCategory))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(board.count())
            .from(board)
            .where(board.boardCategory.eq(boardCategory))
            .fetchOne() ?: 0L

        return PageImpl(results, pageable, total)
    }

    override fun findBoardByUserId(userId: Long, pageable: Pageable): Page<Board> {

        val results = queryFactory
            .selectFrom(board)
            .where(board.userId.eq(userId))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(board.count())
            .from(board)
            .where(board.boardId.eq(board.boardId))
            .fetchOne() ?: 0L

        return PageImpl(results, pageable, total)
    }

    override fun findByIdOrElseThrow(boardId: Long): Board {

        val result = queryFactory
            .select(board)
            .where(board.boardId.eq(boardId))
            .fetchFirst()

        if (result == null) {
            throw BoardNotFoundException("존재하지 않는 게시글입니다.")
        }
        return result
    }
}