package com.netcompany.robocode.auth

data class RegisterDto(val registrationCode: String,
                       val teamName: String,
                       val password: String)