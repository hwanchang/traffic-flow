package io.trafficflow.configuration.security.filter.jwt

import io.trafficflow.common.authentication.cookie.Cookie.addTokenCookie
import io.trafficflow.common.authentication.jwt.Jwt
import io.trafficflow.common.authentication.jwt.Jwt.Claims
import io.trafficflow.common.authentication.jwt.JwtAuthentication
import io.trafficflow.common.authentication.jwt.JwtAuthenticationToken
import io.trafficflow.configuration.security.JwtConfiguration
import com.auth0.jwt.exceptions.TokenExpiredException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwt: Jwt,

    private val jwtConfiguration: JwtConfiguration,
) : OncePerRequestFilter() {
    override fun shouldNotFilter(request: HttpServletRequest) = listOf(
        "/actuator/.*",
        "/api/v1/users/signup",
        "/api/v1/auth/login",
        "/api/v1/auth/login/.*",
    ).any { path -> request.servletPath.matches(Regex(Regex.escape(path))) }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        if (SecurityContextHolder.getContext().authentication == null) {
            val token = getToken(request, "access-token")
            if (token != null) {
                changeAuthentication(token, request, response)
            }

            val refreshToken = getToken(request, "refresh-token")
            if (refreshToken != null) {
                refreshToken(refreshToken, request, response)
            }
        }

        chain.doFilter(request, response)
    }

    private fun getToken(request: HttpServletRequest, name: String) =
        request.cookies?.firstOrNull { it.name == name }?.value

    private fun changeAuthentication(
        token: String,
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
    ) {
        try {
            val claims = jwt.verify(token)

            val authentication = JwtAuthenticationToken(
                principal = JwtAuthentication(token, claims.id),
                authorities = getAuthority(claims),
            ).apply { details = WebAuthenticationDetailsSource().buildDetails(httpServletRequest) }

            SecurityContextHolder.getContext().authentication = authentication
        } catch (tokenExpiredException: TokenExpiredException) {
            val refreshToken = getToken(httpServletRequest, "refresh-token")

            if (refreshToken != null) {
                refreshToken(refreshToken, httpServletRequest, httpServletResponse)
            } else {
                throw tokenExpiredException
            }
        }
    }

    private fun getAuthority(claims: Claims): List<GrantedAuthority> {
        val role = claims.role

        return listOf(SimpleGrantedAuthority(role.role))
    }

    private fun refreshToken(
        refreshToken: String,
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
    ) {
        val (claims, newToken) = jwt.refresh(refreshToken)
        httpServletResponse.addTokenCookie(
            token = newToken,
            domain = jwtConfiguration.domain,
            accessTokenExpireMinute = jwtConfiguration.accessTokenExpireMinute,
            refreshTokenExpireMinute = jwtConfiguration.refreshTokenExpireMinute,
        )

        val authentication = JwtAuthenticationToken(
            principal = JwtAuthentication(newToken.accessToken, claims.id),
            authorities = getAuthority(claims),
        ).apply { details = WebAuthenticationDetailsSource().buildDetails(httpServletRequest) }

        SecurityContextHolder.getContext().authentication = authentication
    }
}
