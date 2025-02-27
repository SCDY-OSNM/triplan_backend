package scdy.boardservice.exception

import scdy.boardservice.common.exceptions.BadRequestException

class UnLikedBoardException(message: String): BadRequestException(message) {
}