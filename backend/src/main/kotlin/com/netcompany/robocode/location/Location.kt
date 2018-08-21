package com.netcompany.robocode.location

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.netcompany.robocode.delivery.LocalDateTimeSerializer
import java.time.LocalDateTime

data class Location(val id: Long,
                    val title: String,
                    val registrationCode: String,
                    @JsonSerialize(using = LocalDateTimeSerializer::class) val deadline: LocalDateTime)