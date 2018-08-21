package com.netcompany.robocode.rest

import com.netcompany.robocode.security.Secured
import com.netcompany.robocode.security.predicates.UserIsAdmin
import com.netcompany.robocode.team.TeamDao
import org.springframework.stereotype.Service
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("team")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Service
class TeamRestService(private val teamDao: TeamDao) {
    @GET
    @Path("{location}")
    @Secured(UserIsAdmin::class)
    fun getTeamsForLocation(@PathParam("location") locationName: String): Response {
        return Response.ok(teamDao.getTeams(locationName)).build();
    }
}