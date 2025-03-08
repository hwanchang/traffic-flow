package io.trafficflow.auth.controller.v1

import io.trafficflow.auth.controller.v1.data.LoginRequest
import io.trafficflow.auth.service.AuthService
import io.trafficflow.common.authentication.cookie.Cookie.addTokenCookie
import io.trafficflow.configuration.security.JwtConfiguration
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("api/v1/auth")
@RestController
class AuthRestController(
    private val authService: AuthService,

    private val jwtConfiguration: JwtConfiguration,
) {
    @PostMapping("login")
    fun login(@RequestBody loginRequest: LoginRequest, response: HttpServletResponse) {
        val token = authService.login(loginRequest.toDomain())

        response.addTokenCookie(
            token = token,
            domain = jwtConfiguration.domain,
            accessTokenExpireMinute = jwtConfiguration.accessTokenExpireMinute,
            refreshTokenExpireMinute = jwtConfiguration.refreshTokenExpireMinute,
        )
    }

    @GetMapping("login/{provider}")
    fun socialLogin(
        @PathVariable provider: String,
        response: HttpServletResponse,
    ) {
        response.sendRedirect("/oauth2/authorization/$provider")
    }
}
