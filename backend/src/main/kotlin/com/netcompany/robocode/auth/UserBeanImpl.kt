package com.netcompany.robocode.auth

import io.jsonwebtoken.Claims
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
class UserBeanImpl(servletRequest: HttpServletRequest,
                   jwtHelper: JwtHelper) : UserBean {
    override val isLoggedIn: Boolean
    override val claims: Claims?

    init {
        val jwtString = servletRequest.getHeader("Authorization") ?: ""

        if (jwtString.isBlank()) {
            isLoggedIn = false
            claims = null
        } else {
            claims = jwtHelper.validateJwt(jwtString)
            isLoggedIn = true
        }
    }
}