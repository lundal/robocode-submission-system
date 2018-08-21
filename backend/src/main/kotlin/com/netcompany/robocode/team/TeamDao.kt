package com.netcompany.robocode.team

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class TeamDao(private val namedTemplate: NamedParameterJdbcTemplate) {
    fun getTeams(locationName: String) =
            namedTemplate.query("" +
                    "SELECT team.title, " +
                    "(select count(*) from delivery where team_id = team.id) as count " +
                    "FROM team team " +
                    "WHERE team.location_id = (SELECT id FROM location WHERE title = :locationName)",
                    MapSqlParameterSource("locationName", locationName))
            { rs, _ ->
                TeamDto(rs.getString("title"),
                        rs.getInt("count") > 0)
            }
}