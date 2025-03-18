package scdy.boardservice.exception

import scdy.boardservice.common.exceptions.NotFoundException

class BoardNotFoundException(message: String): NotFoundException(message) {
}