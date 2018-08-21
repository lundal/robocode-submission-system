package com.netcompany.robocode.auth

data class Team(val id: Long,
                val title: String,
                val password: String,
                val members: String,
                val locationId: Long,
                val locationName: String)