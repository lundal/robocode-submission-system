package com.netcompany.robocode.delivery

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.time.LocalDateTime

data class Delivery(val id: Long,
                    @JsonSerialize(using = LocalDateTimeSerializer::class) val created: LocalDateTime,
                    val filename: String,
                    val teamId: Long)