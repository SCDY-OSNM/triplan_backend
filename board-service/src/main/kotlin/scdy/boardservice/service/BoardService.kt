package scdy.boardservice.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import scdy.boardservice.dto.BoardLikeResponseDto
import scdy.boardservice.dto.BoardRequestDto
import scdy.boardservice.dto.BoardResponseDto
import scdy.boardservice.dto.BoardUpdateRequestDto
import scdy.boardservice.entity.Board
import scdy.boardservice.entity.BoardLike
import scdy.boardservice.enums.BoardCategory
import scdy.boardservice.exception.AleadyLikedBoardException
import scdy.boardservice.exception.BoardNotFoundException
import scdy.boardservice.exception.NotFoundPermissionException
import scdy.boardservice.exception.UnLikedBoardException
import scdy.boardservice.repository.BoardLikeRepository
import scdy.boardservice.repository.BoardRepository
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class BoardService (private var boardRepository: BoardRepository, private var boardLikeRepository: BoardLikeRepository) {

    // create Board
    // 공지는 ADMIN 만 작성 가능
    @Transactional
    fun createBoard(boardRequestDto: BoardRequestDto, userId: Long, userRole: String): BoardResponseDto {

        if(boardRequestDto.boardCategory.equals(BoardCategory.NOTICE) && userRole != "ADMIN") {
            throw NotFoundPermissionException("작성 권한이 없는 사용자입니다.")
        }

        val board = Board(
            userId = userId,
            boardTitle = boardRequestDto.boardTitle,
            boardContents = boardRequestDto.boardContents,
            boardHashtag = boardRequestDto.boardHashtag,
            boardCreatedAt = LocalDateTime.now(),
            boardUpdatedAt = LocalDateTime.now(),
            boardCategory = boardRequestDto.boardCategory
        )

        boardRepository.save(board);

        return BoardResponseDto.from(board)
    }

    //get board By boardId
    fun readBoard(boardId: Long): BoardResponseDto {

        val board = boardRepository.findById(boardId).orElseThrow {
            BoardNotFoundException("존재하지 않는 게시글입니다")
        }

        return BoardResponseDto.from(board)
    }

    //get Board By Category
    fun getBoardByCategory(boardCategory: BoardCategory, pageable: Pageable): Page<BoardResponseDto> {

        val boardPage: Page<Board> = boardRepository.findBoardByCategory(boardCategory, pageable)

        return boardPage.map{ BoardResponseDto.from(it) }
    }

    //get Board By userId
    fun getBoardByUserId(userId: Long, pageable: Pageable): Page<BoardResponseDto> {

        val boardPage: Page<Board> = boardRepository.findBoardByUserId(userId, pageable)

        return boardPage.map{ BoardResponseDto.from(it) }
    }

    //update Board
    //ADMIN 및 작성자만 수정 가능
    @Transactional
    fun updateBoard(boardUpdateRequestDto: BoardUpdateRequestDto, boardId: Long, userId: Long, userRole: String): BoardResponseDto {

        val board = boardRepository.findById(boardId).orElseThrow{
            throw BoardNotFoundException("존재하지 않는 게시글입니다")
        }

        if( !isAdmin(userRole) || !isOwner(board.userId, userId)){
            throw NotFoundPermissionException("수정 권한이 없는 사용자입니다.")
        }

        board.updateBoard(
            boardTitle = boardUpdateRequestDto.boardTitle,
            boardContents = boardUpdateRequestDto.boardContents,
            boardHashtag = boardUpdateRequestDto.boardHashtag,
        )

        return BoardResponseDto.from(board)
    }


    //Delete Board
    //ADMIN 및 작성자만 삭제 가능
    @Transactional
    fun deleteBoard(boardId: Long, userId: Long, userRole: String): Boolean {

        val board = boardRepository.findById(boardId).orElseThrow {
            throw BoardNotFoundException("존재하지 않는 게시물입니다.")
        }

        if( !isAdmin(userRole) || !isOwner(board.userId, userId)){
            throw NotFoundPermissionException("삭제 권한이 없는 사용자입니다")
        }

        boardRepository.delete(board)

        return true
    }


    //Board Like (create BoardLike)
    @Transactional
    fun likeBoard(boardId: Long, userId: Long): BoardLikeResponseDto {

        //게시물 확인
        val board = boardRepository.findById(boardId).orElseThrow {
            BoardNotFoundException("존재하지 않는 게시물입니다.")
        }

        //사용자 확인(이미 좋아요가 있는지)
        if( boardLikeRepository.findByUserId(userId) != null){
            AleadyLikedBoardException("이미 좋아요 한 게시물입니다.")
        }

        val boardLike = BoardLike(
            userId = userId,
            board = board
        )

        boardLikeRepository.save(boardLike)
        return BoardLikeResponseDto.from(boardLike)
    }

    //TODO: Soft delete 구현
    //Cancel Board Like (delete boardLike)
    @Transactional
    fun cancelLikeBoard(boardId: Long, userId: Long): Boolean {

        val board = boardRepository.findById(boardId).orElseThrow {
            throw BoardNotFoundException("존재하지 않는 게시물입니다.")
        }

        //사용자 확인(좋아요를 누르지 않은경우)
        var boardLike: BoardLike? = boardLikeRepository.findByUserIdAndBoardId(userId, boardId).orElse(null)
            ?: throw UnLikedBoardException("좋아요 하지 않은 게시물입니다.")

        if (boardLike != null) {
            boardLikeRepository.deleteById(boardLike.userId)
        }

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


    //permission check
    fun isAdmin(userRole: String): Boolean {
        return userRole == "ADMIN"
    }

    fun isOwner(boardUserId: Long, requestUserId: Long): Boolean {
        return boardUserId == requestUserId
    }

}