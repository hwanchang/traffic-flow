package io.trafficflow.common.authentication.jwt

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticationToken(
    private val principal: Any,

    private var credentials: String? = null,

    authorities: List<GrantedAuthority>,
) : UsernamePasswordAuthenticationToken(principal, credentials, authorities) {
    override fun getPrincipal() = principal

    override fun getCredentials() = credentials

    override fun eraseCredentials() {
        super.eraseCredentials()
        credentials = null
    }
}
