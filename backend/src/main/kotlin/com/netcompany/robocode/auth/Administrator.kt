package com.netcompany.robocode.auth

data class Administrator(val id: Long,
                         val title: String,
                         val password: String,
                         val superuser: Boolean,
                         val locationId: Long,
                         val locationName: String)