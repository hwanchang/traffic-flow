package io.trafficflow.common.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.TypeMismatchException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.HttpMediaTypeException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.multipart.MultipartException

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    private fun response(errorMessage: String?, httpStatus: HttpStatus): ResponseEntity<*> {
        return ResponseEntity(ErrorResponse(errorMessage), httpStatus)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoHandlerFoundException(e: Exception): ResponseEntity<*> {
        return response(e.message, NOT_FOUND)
    }

    @ExceptionHandler(
        IllegalStateException::class,
        IllegalArgumentException::class,
        TypeMismatchException::class,
        HttpMessageNotReadableException::class,
        MissingServletRequestParameterException::class,
        MultipartException::class
    )
    fun handleBadRequestException(e: Exception): ResponseEntity<*> {
        log.warn("Bad request exception occurred: {}", e.message, e)

        return response(e.message, BAD_REQUEST)
    }

    @ExceptionHandler(HttpMediaTypeException::class)
    fun handleHttpMediaTypeException(e: Exception): ResponseEntity<*> {
        return response(e.message, UNSUPPORTED_MEDIA_TYPE)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotAllowedException(e: Exception): ResponseEntity<*> {
        return response(e.message, METHOD_NOT_ALLOWED)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(e: Exception): ResponseEntity<*> {
        log.warn("Access denied exception occurred: {}", e.message, e)

        return response(e.message, UNAUTHORIZED)
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(e: Exception): ResponseEntity<*> {
        log.warn("Authentication exception occurred: {}", e.message, e)

        return response(e.message, FORBIDDEN)
    }

    @ExceptionHandler(Exception::class, RuntimeException::class)
    fun handleException(e: Exception): ResponseEntity<*> {
        log.error("Unexpected exception occurred: {}", e.message, e)

        return response(e.message, INTERNAL_SERVER_ERROR)
    }
}
