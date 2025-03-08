package io.trafficflow.user.service

import io.trafficflow.user.domain.User
import io.trafficflow.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    @Transactional
    fun signUp(user: User): User {
        if (userRepository.findByEmail(user.email) != null) {
            throw IllegalArgumentException("이미 존재하는 이메일입니다.")
        }

        return userRepository.save(user)
    }

    @Transactional(readOnly = true)
    fun findById(id: Long) = userRepository.findByIdOrNull(id) ?: throw NoSuchElementException("해당 유저를 찾을 수 없습니다.")

    @Transactional(readOnly = true)
    fun findByEmail(email: String) = userRepository.findByEmail(email)
}
