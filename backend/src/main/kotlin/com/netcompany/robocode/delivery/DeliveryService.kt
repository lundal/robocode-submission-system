package com.netcompany.robocode.delivery

import com.fasterxml.jackson.databind.AnnotationIntrospector.pair
import com.netcompany.robocode.auth.UserBean
import com.netcompany.robocode.location.LocationDao
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.io.InputStream
import java.net.URI
import java.nio.file.*
import java.time.LocalDateTime

@Service
class DeliveryService(private val userBean: UserBean,
                      private val deliveryDao: DeliveryDao,
                      private val locationDao: LocationDao,
                      @Value("\${uploadPath}") private val uploadPath: String) {
    val TEN_MB: Long = 10 * 1024 * 1024
    val BUFFER_SIZE = 10 * 1024

    init {
        val uploadPath = Paths.get(uploadPath)
        if (Files.exists(uploadPath) && !Files.isDirectory(uploadPath)) {
            throw IllegalStateException("Upload path $uploadPath exists but is not a directory!")
        }

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath)
        }
    }

    private fun getTeamId() = (userBean.claims?.get("teamId") as Number).toLong()
    private fun getLocation() = userBean.claims?.get("location") as String

    fun createDelivery(filename: String, inputStream: InputStream) {
        checkFilename(filename)
        checkDeadline()
        val teamId = getTeamId()
        val location = getLocation()
        val deliveryId = deliveryDao.createDelivery(teamId, filename)
        try {
            writeToFile(location, teamId, deliveryId, filename, inputStream)
        } catch (ex: Exception) {
            deliveryDao.deleteDeliveryById(deliveryId)
            throw ex
        }
    }

    private fun checkDeadline() {
        val locationName = userBean.claims?.get("location") as String
        val location = locationDao.getLocationByName(locationName)!!
        if (LocalDateTime.now() > location.deadline) {
            throw FileUploadException("Delivery deadline has passed")
        }
    }

    fun getCurrentUserDeliveries(): List<Delivery> {
        val teamId = getTeamId()
        return deliveryDao.getDeliveriesForTeam(teamId)
    }

    private fun checkFilename(filename: String) {
        if (filename.length > 100) {
            throw FileUploadException("Filename is too long")
        }

        for (ch in filename) {
            if (!isLetter(ch) && !isDigit(ch) && !isSpecial(ch)) {
                throw FileUploadException("Illegal character in file name")
            }
        }
    }

    private fun isLetter(char: Char): Boolean {
        return (char in 'A'..'Z') || (char in 'a'..'z')
    }

    private fun isDigit(char: Char): Boolean {
        return char in '0'..'9'
    }

    private fun isSpecial(char: Char): Boolean {
        val allowed = arrayOf('-', '_', '(', ')', '.')
        return char in allowed
    }

    private fun getFilename(location: String, teamId: Long, deliveryId: Long, filename: String) : Path
    {
        val dir = Paths.get(uploadPath, location)
        if(!Files.exists(dir)) {
            Files.createDirectories(dir)
        } else {
            if(!Files.isDirectory(dir)) {
                throw FileUploadException("Server directory error")
            }
        }

        return dir.resolve("$teamId-$deliveryId-$filename")
    }


    private fun writeToFile(location: String, teamId: Long, deliveryId: Long, filename: String, file: InputStream) {
        val p = getFilename(location, teamId, deliveryId, filename)
        val outputStream = Files.newOutputStream(p, StandardOpenOption.CREATE_NEW)
        val buffer = ByteArray(BUFFER_SIZE)
        var totalSize = 0

        while (true) {
            val read = file.read(buffer)

            if (read == -1) {
                break
            }

            totalSize += read

            if (totalSize > TEN_MB) {
                outputStream.close()
                file.close()
                Files.deleteIfExists(p)
                throw FileUploadException("File is too large, max size is 10MB")
            }

            outputStream.write(buffer, 0, read)
        }

        file.close()
        outputStream.close()
    }

    fun getAllDeliveriesForLocation(locationName: String): InputStream? {
        val location = locationDao.getLocationByName(locationName)!!
        val deliveries = deliveryDao.getAllDeliveriesForLocation(location.id)
        val file = Files.createTempFile("robocodedl", ".zip")
        Files.deleteIfExists(file)
        val newUri = URI("jar:"+file.toAbsolutePath().toUri())
        val zipfs = FileSystems.newFileSystem(newUri, mapOf("create" to "true"))

        deliveries.forEach {
            val external = getFilename(locationName, it.teamId, it.id, it.filename)
            val dir = zipfs.getPath(locationName)
            if(!Files.exists(dir)) {
                Files.createDirectories(dir)
            }

            val internal = zipfs.getPath("$locationName/${it.filename}")
            Files.copy(external.toAbsolutePath(), internal.toAbsolutePath(), StandardCopyOption.REPLACE_EXISTING)
        }

        zipfs.close()
        return Files.newInputStream(file, StandardOpenOption.READ)
    }
}