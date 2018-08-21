package com.netcompany.robocode.auth

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class AuthenticationDao(private val namedTemplate: NamedParameterJdbcTemplate) {
    fun createTeam(title: String, password: String, locationId: Long) {
        namedTemplate.update("" +
                "INSERT INTO team(title, password, members, location_id) " +
                "VALUES (:title, :password, '', :locationId)",
                MapSqlParameterSource()
                        .addValue("title", title)
                        .addValue("password", password)
                        .addValue("locationId", locationId))
    }

    fun getAdministrator(title: String) =
            namedTemplate.query("" +
                    "SELECT " +
                    "a.id, a.title, a.password, a.superuser, a.location_id, l.title as location_title " +
                    "FROM administrator a " +
                    "JOIN location l ON l.id = a.location_id " +
                    "WHERE a.title = :title",
                    MapSqlParameterSource("title", title))
            { rs, _ ->
                Administrator(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("password"),
                        rs.getBoolean("superuser"),
                        rs.getLong("location_id"),
                        rs.getString("location_title"))
            }.getOrNull(0)

    fun getTeam(title: String) =
            namedTemplate.query("" +
                    "SELECT " +
                    "t.id, t.title, t.password, t.members, t.location_id, l.title as location_title " +
                    "FROM team t " +
                    "JOIN location l on l.id = t.location_id " +
                    "WHERE t.title = :title",
                    MapSqlParameterSource("title", title))
            { rs, _ ->
                Team(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("password"),
                        rs.getString("members"),
                        rs.getLong("location_id"),
                        rs.getString("location_title")
                )
            }.getOrNull(0)
}