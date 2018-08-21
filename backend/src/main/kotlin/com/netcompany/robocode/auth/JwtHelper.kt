package com.netcompany.robocode.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class JwtHelper(@Value("\${jwt.signingKey}") private val signingKey: String) {
    fun createJwtForAdministrator(administrator: Administrator) =
            JwtDto(Jwts.builder()
                    .setSubject(administrator.title)
                    .claim("type", "administrator")
                    .claim("superuser", administrator.superuser)
                    .claim("location", administrator.locationName)
                    .signWith(SignatureAlgorithm.HS512, signingKey)
                    .compact())

    fun createJwtForTeam(team: Team) =
            JwtDto(Jwts.builder()
                    .setSubject(team.title)
                    .claim("teamId", team.id)
                    .claim("type", "team")
                    .claim("location", team.locationName)
                    .signWith(SignatureAlgorithm.HS512, signingKey)
                    .compact())

    fun validateJwt(jwt: String) =
            Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(jwt)
                    .body;
}