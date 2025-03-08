package io.trafficflow.common.authentication.cookie

import io.trafficflow.common.domain.Token
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse

object Cookie {
    fun HttpServletResponse.addTokenCookie(
        token: Token,
        domain: String,
        accessTokenExpireMinute: Int,
        refreshTokenExpireMinute: Int,
    ) = apply {
        addCookie(createCookie("access-token", token.accessToken, domain, accessTokenExpireMinute))
        addCookie(createCookie("refresh-token", token.refreshToken, domain, refreshTokenExpireMinute))
    }

    private fun createCookie(key: String, value: String, cookieDomain: String, age: Int) = Cookie(key, value).apply {
        domain = cookieDomain
        path = "/"
        isHttpOnly = true
        secure = false
        maxAge = age * 60
    }
}
