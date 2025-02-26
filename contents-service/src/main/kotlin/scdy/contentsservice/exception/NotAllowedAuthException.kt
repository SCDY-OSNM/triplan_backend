package scdy.contentsservice.exception

import scdy.contentsservice.common.exceptions.ForbiddenException

class NotAllowedAuthException(message: String) : ForbiddenException(message) {

}