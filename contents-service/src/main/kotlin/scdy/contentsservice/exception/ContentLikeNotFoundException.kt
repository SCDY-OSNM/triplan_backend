package scdy.contentsservice.exception

import jakarta.ws.rs.NotFoundException

class ContentLikeNotFoundException(message : String): NotFoundException(message) {
}