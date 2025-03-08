package io.trafficflow.configuration.security.handler

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class AccessDeniedHandler : AccessDeniedHandler {
    companion object {
        private val E403 = ResponseEntity("Authentication error (cause: forbidden)", FORBIDDEN)
        private val objectMapper = jacksonObjectMapper()
    }

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException,
    ) {
        response.apply {
            status = SC_FORBIDDEN
            setHeader("content-type", "application/json")
            writer.write(objectMapper.writeValueAsString(E403))
            writer.flush()
            writer.close()
        }
    }
}
