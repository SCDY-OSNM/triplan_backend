package scdy.boardservice.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import scdy.boardservice.dto.BoardLikeResponseDto
import scdy.boardservice.dto.BoardRequestDto
import scdy.boardservice.dto.BoardResponseDto
import scdy.boardservice.dto.BoardUpdateRequestDto
import scdy.boardservice.elasticsearch.BoardDocument
import scdy.boardservice.entity.Board
import scdy.boardservice.entity.BoardLike
import scdy.boardservice.enums.BoardCategory
import scdy.boardservice.exception.AleadyLikedBoardException
import scdy.boardservice.exception.BoardNotFoundException
import scdy.boardservice.exception.NotFoundPermissionException
import scdy.boardservice.exception.UnLikedBoardException
import scdy.boardservice.repository.BoardLikeRepository
import scdy.boardservice.repository.BoardRepository
import scdy.boardservice.repository.EsBoardRepository
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class BoardService(
    private val boardRepository: BoardRepository,
    private val esBoardRepository: EsBoardRepository,
//    private val elasticsearchOperations: ElasticsearchOperations,
    private val searchService: SearchService,
    private val boardLikeRepository: BoardLikeRepository,
) {

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

        val saved = boardRepository.save(board);
//        esBoardRepository.save(board.toDocument());

//        val savedDoc = board.toDocument()
//        elasticsearchOperations.save(savedDoc, IndexCoordinates.of("board_index"))
//        elasticsearchOperations.indexOps(BoardDocument::class.java).refresh()
        val likeCount = boardLikeRepository.countByBoard_BoardId(board.boardId ?: -1)

        searchService.saveBoardIndex(board)


        return BoardResponseDto.from(saved)
    }

    //get board By boardId
    fun readBoard(boardId: Long): BoardResponseDto {

        val board = boardRepository.findById(boardId).orElseThrow {
            BoardNotFoundException("존재하지 않는 게시글입니다.")
        }

        return BoardResponseDto.from(board)
    }

    //get Board By Category
    fun getBoardByCategory(boardCategory: BoardCategory, pageable: Pageable): Page<BoardResponseDto> {

        val boardPage: Page<Board> = boardRepository.findBoardByBoardCategory(boardCategory, pageable)

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
        println(userRole +  " " + board.userId + userId )
        if( !isAdmin(userRole) && !isOwner(board.userId, userId)){
            throw NotFoundPermissionException("수정 권한이 없는 사용자입니다.")
        }

        board.updateBoard(
            boardTitle = boardUpdateRequestDto.boardTitle,
            boardContents = boardUpdateRequestDto.boardContents,
            boardHashtag = boardUpdateRequestDto.boardHashtag,
        )

        searchService.updateBoardIndex(board)

        return BoardResponseDto.from(board)
    }


    //Delete Board
    //ADMIN 및 작성자만 삭제 가능
    @Transactional
    fun deleteBoard(boardId: Long, userId: Long, userRole: String): Boolean {

        val board = boardRepository.findById(boardId).orElseThrow {
            throw BoardNotFoundException("존재하지 않는 게시물입니다.")
        }

        if( !isAdmin(userRole) && !isOwner(board.userId, userId)){
            throw NotFoundPermissionException("삭제 권한이 없는 사용자입니다")
        }

        boardRepository.delete(board)
        searchService.deleteBoardIndex(boardId)

        return true
    }




    //search by title
    fun searchByTitle(title: String, pageable: Pageable): Page<BoardResponseDto> {

        val result =  boardRepository.searchBoardByTitle(title, pageable)

        return result.map { BoardResponseDto.from(it) }
    }

    //search by contents
    fun searchByContents(contents: String, pageable: Pageable): Page<BoardResponseDto> {

        val result =  boardRepository.searchBoardByContents(contents, pageable)

        return result.map { BoardResponseDto.from(it) }
    }

    //search by hashtag
    fun searchByHashTag(hashTag: String, pageable: Pageable): Page<BoardResponseDto> {

        val result = boardRepository.searchBoardByHashtag(hashTag, pageable)

        return result.map { BoardResponseDto.from(it) }
    }

    fun searchByEsTitle(title: String, pageable: Pageable): Page<BoardResponseDto> {

        val result = esBoardRepository.findByBoardTitle(title, pageable)

        return result.map { BoardResponseDto.from(it) }
    }

    fun searchByEsContents(contents: String, pageable: Pageable): Page<BoardResponseDto> {

        val result = esBoardRepository.findByBoardContents(contents, pageable)

        return result.map { BoardResponseDto.from(it) }
    }

    fun searchByEsHashtag(hashTag: String, pageable: Pageable): Page<BoardResponseDto> {

        val result = esBoardRepository.findByBoardHashtag(hashTag, pageable)

        return result.map { BoardResponseDto.from(it) }
    }


    //permission check
    fun isAdmin(userRole: String): Boolean {
        println(userRole)
        return userRole == "ADMIN"
    }

    fun isOwner(boardUserId: Long, requestUserId: Long): Boolean {
        return boardUserId == requestUserId
    }

}