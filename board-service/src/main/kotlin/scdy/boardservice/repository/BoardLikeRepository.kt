package scdy.boardservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import scdy.boardservice.entity.BoardLike
import java.util.*

interface BoardLikeRepository: JpaRepository<BoardLike, Long>, BoardLikeCustomRepository {

}