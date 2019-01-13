package com.netcompany.robocode.delivery

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
class DeliveryDao(private val namedTemplate: NamedParameterJdbcTemplate) {
    init {
        Objects.requireNonNull(namedTemplate)
    }

    fun createDelivery(teamId: Long, filename: String): Long {
        val keyHolder = GeneratedKeyHolder()
        namedTemplate.update("" +
                "INSERT INTO delivery " +
                "(created, filename, team_id) " +
                "VALUES (:created, :filename, :teamId)",
                MapSqlParameterSource()
                        .addValue("created", LocalDateTime.now())
                        .addValue("filename", filename)
                        .addValue("teamId", teamId),
                keyHolder,
                arrayOf("id"))

        return keyHolder.key.toLong()
    }

    fun deleteDeliveryById(id: Long) {
        namedTemplate.update("" +
                "DELETE FROM delivery WHERE id = :id",
                MapSqlParameterSource("id", id))
    }

    fun getDeliveriesForTeam(teamId: Long) =
            namedTemplate.query("" +
                    "SELECT id, created, filename, team_id " +
                    "FROM delivery " +
                    "WHERE team_id = :teamId " +
                    "ORDER BY created DESC",
                    MapSqlParameterSource("teamId", teamId))
            { rs, _ ->
                Delivery(
                        rs.getLong("id"),
                        rs.getTimestamp("created").toLocalDateTime(),
                        rs.getString("filename"),
                        rs.getLong("team_id"),
                        "TODO")
            }

    fun getAllDeliveriesForLocation(locationId: Long) =
            namedTemplate.query("" +
                    "SELECT d.id, d.created, d.filename, d.team_id, t.title as team_name " +
                    "FROM delivery d " +
                    "JOIN team t ON t.id = d.team_id " +
                    "WHERE t.location_id = :locationId AND d.created = (SELECT max(created) FROM delivery WHERE team_id = t.id)" +
                    "ORDER BY d.created DESC, d.id DESC " +
                    "",
                    MapSqlParameterSource("locationId", locationId))
            { rs, _ ->
                Delivery(
                        rs.getLong("id"),
                        rs.getTimestamp("created").toLocalDateTime(),
                        rs.getString("filename"),
                        rs.getLong("team_id"),
                        rs.getString("team_name"))
            }
}