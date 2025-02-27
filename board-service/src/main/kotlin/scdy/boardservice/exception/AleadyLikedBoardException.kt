package scdy.boardservice.exception

import scdy.boardservice.common.exceptions.BadRequestException

class AleadyLikedBoardException(message: String): BadRequestException(message) {
}