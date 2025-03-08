package io.trafficflow.configuration.security.filter.internal

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class ApiKeyAuthenticationFilter(private val internalApiKey: String) : OncePerRequestFilter() {
    override fun shouldNotFilter(request: HttpServletRequest) = listOf(
        "/actuator/.*",
        "/api/v1/users/signup",
        "/api/v1/auth/login",
        "/api/v1/auth/login/.*",
    ).any { path -> request.servletPath.matches(Regex(Regex.escape(path))) }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        if (SecurityContextHolder.getContext().authentication != null) {
            filterChain.doFilter(request, response)
        } else {
            val apiKey = request.getHeader("X-API-KEY") ?: null
            if (apiKey == null || apiKey != internalApiKey) {
                response.sendError(SC_UNAUTHORIZED, "Authentication error (cause: unauthorized)")
                return
            }

            filterChain.doFilter(request, response)
        }
    }
}
