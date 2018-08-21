package com.netcompany.robocode.rest

import com.netcompany.robocode.delivery.DeliveryService
import com.netcompany.robocode.location.LocationDao
import com.netcompany.robocode.security.Secured
import com.netcompany.robocode.security.predicates.UserIsAdmin
import org.springframework.stereotype.Service
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Service
@Path("download")
class DownloadRestService(private val deliveryService: DeliveryService) {

    @GET
    @Secured(UserIsAdmin::class)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("{location}")
    fun downloadRobots(@PathParam("location") location: String): Response {
        return Response.ok(deliveryService.getAllDeliveriesForLocation(location)).build()
    }
}