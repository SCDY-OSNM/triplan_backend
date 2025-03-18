package scdy.boardservice.exception

import scdy.boardservice.common.exceptions.ForbiddenException

class NotFoundPermissionException(message: String): ForbiddenException(message) {
}