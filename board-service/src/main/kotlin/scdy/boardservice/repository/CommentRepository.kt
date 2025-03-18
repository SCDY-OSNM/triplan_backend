package scdy.boardservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import scdy.boardservice.entity.Comment

interface CommentRepository: JpaRepository<Comment, Long>, CommentCustomRepository {

}