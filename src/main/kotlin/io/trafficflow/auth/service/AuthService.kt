package io.trafficflow.auth.service

import io.trafficflow.auth.domain.login.Login
import io.trafficflow.common.authentication.jwt.Jwt
import io.trafficflow.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userService: UserService,

    private val jwt: Jwt,
) {
    @Transactional
    fun login(login: Login) = userService.findByEmail(login.email)
        ?.apply { login(login.password) }
        ?.createToken(jwt)
        ?: throw NoSuchElementException("해당 유저를 찾을 수 없습니다.")
}
