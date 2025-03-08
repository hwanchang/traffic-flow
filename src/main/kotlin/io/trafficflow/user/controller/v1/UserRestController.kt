package io.trafficflow.user.controller.v1

import io.trafficflow.common.authentication.jwt.JwtAuthentication
import io.trafficflow.user.controller.v1.data.SignUpRequest
import io.trafficflow.user.controller.v1.data.UserResponse
import io.trafficflow.user.service.UserService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RequestMapping("api/v1/users")
@RestController
class UserRestController(
    private val userService: UserService,
) {
    @PostMapping("signup")
    @ResponseStatus(CREATED)
    fun signUp(@RequestBody request: SignUpRequest) {
        userService.signUp(request.toDomain())
    }

    @GetMapping("me")
    fun me(@AuthenticationPrincipal jwtAuthentication: JwtAuthentication) =
        userService.findById(jwtAuthentication.id).let(::UserResponse)

    @GetMapping("{id}")
    fun get(@PathVariable id: Long) = userService.findById(id).let(::UserResponse)
}
