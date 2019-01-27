package com.netcompany.robocode.delivery

import com.netcompany.robocode.auth.UserBean
import com.netcompany.robocode.location.LocationDao
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.InputStream
import java.net.URI
import java.nio.file.*

@Service
class DeliveryService(private val userBean: UserBean,
                      private val deliveryDao: DeliveryDao,
                      private val locationDao: LocationDao,
                      @Value("\${uploadPath}") private val uploadPath: String) {

    private val log = LoggerFactory.getLogger(javaClass)

    val TEN_MB: Long = 10 * 1024 * 1024
    val BUFFER_SIZE = 10 * 1024
    val DEADLINE_SLACK = 5 * 60 * 1000 // 5 minutes in ms

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
        val path = determinePath(location, teamId, deliveryId, filename)
        try {
            writeToFile(path, inputStream)
            checkFileContainsRobot(path)
        } catch (ex: Exception) {
            deliveryDao.deleteDeliveryById(deliveryId)
            throw ex
        }
    }

    private fun checkDeadline() {
        val locationName = userBean.claims?.get("location") as String
        val location = locationDao.getLocationByName(locationName)!!
        if (System.currentTimeMillis() > location.deadline + DEADLINE_SLACK) {
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

    private fun determinePath(location: String, teamId: Long, deliveryId: Long, filename: String): Path {
        val dir = Paths.get(uploadPath.replaceFirst("~", System.getProperty("user.home")), location)
        if (!Files.exists(dir)) {
            Files.createDirectories(dir)
        } else {
            if (!Files.isDirectory(dir)) {
                throw FileUploadException("Server directory error")
            }
        }

        return dir.resolve("$teamId-$deliveryId-$filename")
    }

    private fun writeToFile(path: Path, file: InputStream) {
        val outputStream = Files.newOutputStream(path, StandardOpenOption.CREATE_NEW)
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
                Files.deleteIfExists(path)
                throw FileUploadException("File is too large, max size is 10MB")
            }

            outputStream.write(buffer, 0, read)
        }

        file.close()
        outputStream.close()
    }

    private fun checkFileContainsRobot(path: Path) {
        if (getRobotName(path) == null) {
            throw FileUploadException("No robot found in jar file")
        }
    }

    private fun getRobotName(path: Path): String? {
        return readManifist(path)
                .filter { line -> line.startsWith("robots:") }
                .map { line -> line.replaceFirst("robots:", "").trim() }
                .firstOrNull { line -> line.isNotEmpty() }
    }

    private fun readManifist(path: Path): List<String> {
        try {
            val uri = URI.create("jar:" + path.toAbsolutePath().toUri())
            FileSystems.newFileSystem(uri, emptyMap<String, String>()).use { zipfs ->
                val manifestPath = zipfs.getPath("/META-INF/MANIFEST.MF")
                return Files.readAllLines(manifestPath)
            }
        } catch (e: Throwable) { // Catch filesystem Errors in addition to Exceptions
            throw FileUploadException("Invalid jar file")
        }
    }

    fun getAllDeliveriesForLocation(locationName: String): InputStream? {
        val location = locationDao.getLocationByName(locationName)!!
        val deliveries = deliveryDao.getAllDeliveriesForLocation(location.id)
        val file = Files.createTempFile("robocodedl", ".zip")
        Files.deleteIfExists(file)
        val newUri = URI("jar:" + file.toAbsolutePath().toUri())
        val zipfs = FileSystems.newFileSystem(newUri, mapOf("create" to "true"))

        deliveries.forEach {
            val external = determinePath(locationName, it.teamId, it.id, it.filename)
            val dir = zipfs.getPath(locationName)
            if (!Files.exists(dir)) {
                Files.createDirectories(dir)
            }

            try {
                val internal = zipfs.getPath("$locationName/${getRobotName(external)} (${it.teamName}).jar")
                Files.copy(external.toAbsolutePath(), internal.toAbsolutePath(), StandardCopyOption.REPLACE_EXISTING)
            } catch (e: Exception) {
                log.warn("Skipping invalid file {}", external)
            }
        }

        zipfs.close()
        return Files.newInputStream(file, StandardOpenOption.READ)
    }
}