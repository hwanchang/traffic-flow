package io.trafficflow.common.authentication.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import io.trafficflow.common.domain.Token
import io.trafficflow.configuration.security.JwtConfiguration
import io.trafficflow.user.domain.Role
import io.trafficflow.user.service.UserService
import java.util.Date
import org.springframework.context.annotation.Configuration

@Configuration
class Jwt(
    jwtConfiguration: JwtConfiguration,

    private val userService: UserService,
) {
    private val issuer: String = jwtConfiguration.issuer

    private val secret: String = jwtConfiguration.secret

    private val accessTokenExpireMinute: Int = jwtConfiguration.accessTokenExpireMinute

    private val refreshTokenExpireMinute: Int = jwtConfiguration.refreshTokenExpireMinute

    private val algorithm: Algorithm = Algorithm.HMAC512(secret)

    private val jwtVerifier: JWTVerifier = JWT.require(algorithm).withIssuer(issuer).build()

    fun refresh(beforeRefreshToken: String): Pair<Claims, Token> {
        val claims = verify(beforeRefreshToken)
        val newToken = generateToken(claims.id, claims.role)

        updateRefreshToken(claims.id, newToken.refreshToken)

        return claims to newToken
    }

    fun verify(token: String): Claims {
        return Claims(jwtVerifier.verify(token))
    }

    fun generateToken(id: Long, role: Role): Token {
        val now = Date()

        val accessToken = createJwt(now, id, role, accessTokenExpireMinute)
        val refreshToken = createJwt(now, id, role, refreshTokenExpireMinute)

        return Token(accessToken, refreshToken)
    }

    private fun createJwt(now: Date, id: Long, role: Role, expireMinute: Int): String {
        return JWT.create().run {
            withIssuer(issuer)
            withIssuedAt(now)
            if (expireMinute > 0) {
                val expireMilliseconds = expireMinute * 60 * 1000
                val expiresAt = Date(now.time + expireMilliseconds)
                withExpiresAt(expiresAt)
            }
            withClaim("id", id)
            withClaim("role", role.role)
        }.sign(algorithm)
    }

    private fun updateRefreshToken(id: Long, refreshToken: String) {
        userService.findById(id).refreshToken(refreshToken)
    }

    class Claims(
        val id: Long,
        val role: Role,
        var iat: Date? = null,
        var exp: Date? = null,
    ) {
        constructor(decodedJWT: DecodedJWT) : this(
            decodedJWT.getClaim("id").asLong(),
            Role.from(decodedJWT.getClaim("role").asString()),
            decodedJWT.issuedAt,
            decodedJWT.expiresAt,
        )
    }
}
