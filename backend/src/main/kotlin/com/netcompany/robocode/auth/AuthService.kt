package com.netcompany.robocode.auth

import com.netcompany.robocode.location.LocationDao
import com.netcompany.robocode.rest.LoginDto
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(private val authenticationDao: AuthenticationDao,
                  private val locationDao: LocationDao,
                  private val passwordEncoder: PasswordEncoder,
                  private val jwtHelper: JwtHelper) {
    @Transactional
    fun registerTeam(registerDto: RegisterDto): JwtDto {
        val location = locationDao.getLocationByCode(registerDto.registrationCode)
                ?: throw AuthenticationException("Invalid registration code")

        if (!registerDto.teamName.matches("^[A-Za-z0-9]+$".toRegex())) {
            throw AuthenticationException("Team name is invalid, A-Z and letters are allowed")
        }

        if(registerDto.teamName.length > 30) {
            throw AuthenticationException("Team name too long (max 30 chars)")
        }

        if (authenticationDao.getTeam(registerDto.teamName) != null ||
                authenticationDao.getAdministrator(registerDto.teamName) != null) {
            throw AuthenticationException("Team name is taken")
        }

        val passwordHash = passwordEncoder.encode(registerDto.password)
        authenticationDao.createTeam(registerDto.teamName, passwordHash, location.id)
        val team = authenticationDao.getTeam(registerDto.teamName)!!

        return jwtHelper.createJwtForTeam(team)
    }

    fun login(loginDto: LoginDto): JwtDto {
        val administrator = authenticationDao.getAdministrator(loginDto.username)
        if (administrator != null) {
            return loginAdmin(administrator, loginDto)
        }

        val team = authenticationDao.getTeam(loginDto.username);
        if (team != null) {
            return loginTeam(team, loginDto)
        }

        throw AuthenticationException("Invalid credentials")
    }

    private fun loginAdmin(administrator: Administrator, loginDto: LoginDto): JwtDto {
        if (!passwordEncoder.matches(loginDto.password, administrator.password)) {
            throw AuthenticationException("Invalid login details")
        }

        return jwtHelper.createJwtForAdministrator(administrator)
    }

    private fun loginTeam(team: Team, loginDto: LoginDto): JwtDto {
        if (!passwordEncoder.matches(loginDto.password, team.password)) {
            throw AuthenticationException("Invalid login details")
        }

        return jwtHelper.createJwtForTeam(team)
    }
}