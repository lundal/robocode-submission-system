package com.netcompany.robocode.delivery

data class Delivery(
        val id: Long,
        val created: Long,
        val filename: String,
        val teamId: Long,
        val teamName: String
)