package io.trafficflow.configuration.security.handler

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class EntryPointUnauthorizedHandler : AuthenticationEntryPoint {
    companion object {
        private val E401 = ResponseEntity("Authentication error (cause: unauthorized)", UNAUTHORIZED)
        private val objectMapper = jacksonObjectMapper()
    }

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        response.apply {
            status = SC_UNAUTHORIZED
            setHeader("content-type", "application/json")
            writer.write(objectMapper.writeValueAsString(E401))
            writer.flush()
            writer.close()
        }
    }
}
