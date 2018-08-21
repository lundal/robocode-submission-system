package com.netcompany.robocode.rest

import com.netcompany.robocode.auth.AuthenticationException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class AuthenticationExceptionMapper : ExceptionMapper<AuthenticationException> {
    override fun toResponse(e: AuthenticationException) = Response.status(403).build()
}