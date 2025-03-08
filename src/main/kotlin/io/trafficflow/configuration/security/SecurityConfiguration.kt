package io.trafficflow.configuration.security

import io.trafficflow.common.authentication.jwt.Jwt
import io.trafficflow.configuration.security.filter.internal.ApiKeyAuthenticationFilter
import io.trafficflow.configuration.security.filter.jwt.JwtAuthenticationFilter
import io.trafficflow.configuration.security.handler.AccessDeniedHandler
import io.trafficflow.configuration.security.handler.EntryPointUnauthorizedHandler
import io.trafficflow.user.domain.Role.ADMIN
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.OPTIONS
import org.springframework.http.HttpMethod.PATCH
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpMethod.PUT
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.DelegatingSecurityContextRepository
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val accessDeniedHandler: AccessDeniedHandler,

    private val unauthorizedHandler: EntryPointUnauthorizedHandler,

    private val jwt: Jwt,

    private val jwtConfiguration: JwtConfiguration,

    @Value("\${internal-api-key}")
    private val internalApiKey: String,
) {
    @Bean
    fun delegatingSecurityContextRepository() = DelegatingSecurityContextRepository(
        RequestAttributeSecurityContextRepository(),
        HttpSessionSecurityContextRepository(),
    )

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain = http
        .cors {
            it.configurationSource(
                UrlBasedCorsConfigurationSource().apply {
                    registerCorsConfiguration(
                        "/**",
                        CorsConfiguration().apply {
                            allowedOrigins = listOf("http://localhost:5173")
                            allowedOriginPatterns = listOf("*")
                            allowedMethods = listOf(POST, GET, PATCH, PUT, DELETE, OPTIONS).map(HttpMethod::name)
                            allowedHeaders = listOf("*")
                            allowCredentials = true
                        },
                    )
                },
            )
        }
        .csrf { it.disable() }
        .headers { it.disable() }
        .httpBasic { it.disable() }
        .formLogin { it.disable() }
        .rememberMe { it.disable() }
        .logout { it.disable() }
        .exceptionHandling {
            it.accessDeniedHandler(accessDeniedHandler)
            it.authenticationEntryPoint(unauthorizedHandler)
        }
        .sessionManagement { it.sessionCreationPolicy(IF_REQUIRED) }
        .securityContext {
            it.securityContextRepository(delegatingSecurityContextRepository())
            it.requireExplicitSave(true)
        }
        .authorizeHttpRequests {
            it.requestMatchers(
                AntPathRequestMatcher("/actuator/**"),
                AntPathRequestMatcher("/api/v1/users/signup", POST.name()),
                AntPathRequestMatcher("/api/v1/auth/login", POST.name()),
                AntPathRequestMatcher("/api/v1/auth/login/**", GET.name()),
            ).permitAll()
            it.requestMatchers(
                AntPathRequestMatcher("/api/v1/admin/**"),
            ).hasAuthority(ADMIN.role)
            it.anyRequest().permitAll()
//            it.anyRequest().hasAnyAuthority(ADMIN.role, USER.role) // 내부 API 요청 시 권한 없는 문제 해결 필요
        }
        .addFilterBefore(
            JwtAuthenticationFilter(jwt = jwt, jwtConfiguration = jwtConfiguration),
            UsernamePasswordAuthenticationFilter::class.java,
        )
        .addFilterAfter(
            ApiKeyAuthenticationFilter(internalApiKey),
            UsernamePasswordAuthenticationFilter::class.java,
        )
        .build()
}
