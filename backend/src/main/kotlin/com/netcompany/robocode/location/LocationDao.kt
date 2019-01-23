package com.netcompany.robocode.location

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class LocationDao(private val namedTemplate: NamedParameterJdbcTemplate) {
    fun getLocationByCode(registerCode: String) =
            namedTemplate.query("" +
                    "SELECT id, title, registration_code, deadline " +
                    "FROM location " +
                    "WHERE registration_code = :registrationCode",
                    MapSqlParameterSource("registrationCode", registerCode))
            { rs, _ ->
                Location(rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("registration_code"),
                        rs.getLong("deadline"))
            }.getOrNull(0)

    fun getLocationByName(locationName: String) =
            namedTemplate.query("" +
                    "SELECT id, title, registration_code, deadline " +
                    "FROM location " +
                    "WHERE title = :title",
                    MapSqlParameterSource("title", locationName))
            { rs, _ ->
                Location(rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("registration_code"),
                        rs.getLong("deadline"))
            }.getOrNull(0)
}