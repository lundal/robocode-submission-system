package com.netcompany.robocode.rest

import com.netcompany.robocode.location.LocationDao
import com.netcompany.robocode.security.Secured
import com.netcompany.robocode.security.predicates.UserIsAdmin
import org.springframework.stereotype.Service
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Service
@Path("location")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class LocationRestService(private val locationDao: LocationDao) {
    @GET
    @Path("{name}")
    @Secured(UserIsAdmin::class)
    fun getLocation(@PathParam("name") locationName: String): Response {
        return Response.ok(locationDao.getLocationByName(locationName)).build()
    }

}