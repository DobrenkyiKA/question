package com.kdob.piq.question.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/health").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer {
                it.jwt {
                    it.jwtAuthenticationConverter(JwtAuthConverter())
                }
            }
        return http.build()
    }
}