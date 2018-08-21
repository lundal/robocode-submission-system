package com.netcompany.robocode.rest

import com.netcompany.robocode.delivery.DeliveryService
import com.netcompany.robocode.delivery.FileUploadException
import com.netcompany.robocode.security.Secured
import com.netcompany.robocode.security.predicates.UserIsTeam
import org.glassfish.jersey.media.multipart.FormDataContentDisposition
import org.glassfish.jersey.media.multipart.FormDataParam
import org.springframework.stereotype.Service
import java.io.InputStream
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Service
@Path("delivery")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class DeliveryRestService(private val deliveryService: DeliveryService) {
    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Secured(UserIsTeam::class)
    fun uploadFile(@FormDataParam("file") uploadedInputStream: InputStream,
                   @FormDataParam("file") fileDetail: FormDataContentDisposition): Response {
        try {
            deliveryService.createDelivery(fileDetail.fileName, uploadedInputStream)
        } catch (ex: FileUploadException) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ErrorDto(ex.message)).build()
        } finally {
            uploadedInputStream.close()
        }
        return Response.ok().build()
    }

    @GET
    @Secured(UserIsTeam::class)
    fun getDeliveries() = Response.ok(deliveryService.getCurrentUserDeliveries()).build()
}