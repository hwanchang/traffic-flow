package io.trafficflow.user.domain

import io.trafficflow.common.authentication.jwt.Jwt
import io.trafficflow.common.domain.Token
import io.trafficflow.common.entity.BaseTimeEntity
import io.trafficflow.user.domain.Role.USER
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import org.hibernate.annotations.DynamicUpdate

@DynamicUpdate
@Entity
class User(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0,

    val email: String,

    val password: String,

    val nickname: String,

    @Enumerated(STRING)
    val role: Role = USER,

    var refreshToken: String? = null,

    private var lastLoginAt: LocalDateTime? = null,
) : BaseTimeEntity() {
    fun login(password: String) {
        require(this.password == password) { "로그인 실패했습니다." }

        this.lastLoginAt = now()
    }

    fun createToken(jwt: Jwt): Token {
        val token = jwt.generateToken(id, role)
        this.refreshToken = token.refreshToken

        return token
    }

    fun refreshToken(newRefreshToken: String) {
        this.refreshToken = newRefreshToken
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
