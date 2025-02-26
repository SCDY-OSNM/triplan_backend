package scdy.contentsservice.exception

import scdy.contentsservice.common.exceptions.NotFoundException


class ContentNotFoundException(message: String) : NotFoundException(message) {

}