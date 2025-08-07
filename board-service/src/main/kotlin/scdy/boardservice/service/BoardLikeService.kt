package scdy.boardservice.service


import org.elasticsearch.search.aggregations.BucketOrder
import org.springframework.data.elasticsearch.client.elc.NativeQuery
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import scdy.boardservice.dto.BoardLikeResponseDto
import scdy.boardservice.entity.Board
import scdy.boardservice.entity.BoardLike
import scdy.boardservice.exception.AleadyLikedBoardException
import scdy.boardservice.exception.BoardNotFoundException
import scdy.boardservice.exception.UnLikedBoardException
import scdy.boardservice.repository.BoardLikeRepository
import scdy.boardservice.repository.BoardRepository
import scdy.boardservice.repository.EsBoardLikeRepository
import scdy.boardservice.repository.EsBoardRepository
import java.time.LocalDateTime



@Service
@Transactional(readOnly = true)
class BoardLikeService(
    private val boardRepository: BoardRepository,
    private val boardLikeRepository: BoardLikeRepository,
    private val esBoardRepository: EsBoardRepository,
    private val esBoardLikeRepository: EsBoardLikeRepository,
    private val repository: EsBoardRepository,
    private val elasticsearchOperations: ElasticsearchOperations,
){

    //Board Like (create BoardLike)
    @Transactional
    fun likeBoard(boardId: Long, userId: Long): BoardLikeResponseDto {

        //게시물 확인
        val board = boardRepository.findById(boardId).orElseThrow {
            throw BoardNotFoundException("존재하지 않는 게시물입니다.")
        }

        //사용자 확인(이미 좋아요가 있는지)
        if( boardLikeRepository.findByUserIdAndId(userId, boardId).isPresent){
            throw AleadyLikedBoardException("이미 좋아요 한 게시물입니다.")
        }

        val boardLike = BoardLike(
            userId = userId,
            board = board,
            boardLikeCreatedAt = LocalDateTime.now(),
        )

        boardLikeRepository.save(boardLike)
        return BoardLikeResponseDto.from(boardLike)
    }

    //TODO: Soft delete 구현
    //Cancel Board Like (delete boardLike)
    @Transactional
    fun cancelLikeBoard(boardId: Long, userId: Long): Boolean {

        boardRepository.findById(boardId).orElseThrow {
            throw BoardNotFoundException("존재하지 않는 게시물입니다.")
        }

        //사용자 확인(좋아요를 누르지 않은경우)
        val boardLike: BoardLike = boardLikeRepository.findByUserIdAndId(userId, boardId).orElse(null)
            ?: throw UnLikedBoardException("좋아요 하지 않은 게시물입니다.")

        boardLikeRepository.delete(boardLike)

        return true
    }

    //get Num of BoardLikes
    //TODO: 조회 마다 새로운 쿼리를 날리는게 비효율적. 대안 찾기
    fun getNumOfBoardLikes(boardId: Long): Int {

        boardRepository.findById(boardId).orElseThrow {
            throw BoardNotFoundException("존재하지 않는 게시물입니다.")
        }

        return boardLikeRepository.findNumberByBoardId(boardId)
    }


}

