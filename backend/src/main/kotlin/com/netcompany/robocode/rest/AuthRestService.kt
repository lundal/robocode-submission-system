package com.netcompany.robocode.rest

import com.netcompany.robocode.auth.AuthService
import com.netcompany.robocode.auth.AuthenticationException
import com.netcompany.robocode.auth.RegisterDto
import com.netcompany.robocode.security.Secured
import com.netcompany.robocode.security.predicates.UserIsAdmin
import com.netcompany.robocode.security.predicates.UserIsTeam
import org.springframework.stereotype.Component
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Component
@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class AuthRestService(private val authService: AuthService) {
    @POST
    @Path("registerTeam")
    fun register(registerDto: RegisterDto): Response {
        return try {
            val jwt = authService.registerTeam(registerDto)
            Response.ok(jwt).build()
        } catch (authenticationException: AuthenticationException) {
            Response.status(Response.Status.BAD_REQUEST)
                    .entity(ErrorDto(authenticationException.message!!))
                    .build()
        }
    }

    @POST
    @Path("login")
    fun login(loginDto: LoginDto): Response {
        return try {
            val jwt = authService.login(loginDto)
            Response.ok(jwt).build()
        } catch (authenticationException: AuthenticationException) {
            Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ErrorDto(authenticationException.message!!))
                    .build()
        }
    }

    @GET
    @Path("test")
    @Secured(UserIsAdmin::class)
    fun test(): Response {
        return Response.ok().build()
    }

    @GET
    @Path("test2")
    @Secured(UserIsTeam::class)
    fun test2() = Response.ok().build()
}