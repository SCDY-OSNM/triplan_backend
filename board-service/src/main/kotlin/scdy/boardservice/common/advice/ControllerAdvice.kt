package scdy.boardservice.common.advice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import scdy.boardservice.common.exceptions.*

@ExceptionHandler(BadRequestException::class)
private // 400 Bad Request
fun handleBadRequestException(e: BadRequestException): ResponseEntity<ErrorResponse> {
    return ResponseEntity.badRequest().body(ErrorResponse(e.message?: "Bad Request"))
}

@RestControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(NotFoundException::class) // 404 Not Found
    fun handleNotFoundException(e: NotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(e.message?: "Bad Request"))
    }

    @ExceptionHandler(BadRequestException::class)
    private // 400 Bad Request
    fun handleBadRequestException(e: BadRequestException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.badRequest().body(ErrorResponse(e.message?: "Bad Request"))
    }

    @ExceptionHandler(ForbiddenException::class) // 403 Forbidden
    fun handleForbiddenException(e: ForbiddenException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse(e.message?: "Bad Request"))
    }

    @ExceptionHandler(UnauthorizedException::class) // 401 Unauthorized
    fun handleUnauthorizedException(e: UnauthorizedException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse(e.message?: "Bad Request"))
    }

    @ExceptionHandler(InternalServerException::class) // 500 Internal Server Error
    fun handleInternalServerException(e: InternalServerException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse(e.message?: "Bad Request"))
    }
}