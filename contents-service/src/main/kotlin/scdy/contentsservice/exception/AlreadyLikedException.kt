package scdy.contentsservice.exception

import scdy.contentsservice.common.exceptions.BadRequestException

class AlreadyLikedException(message : String) : BadRequestException(message){
}