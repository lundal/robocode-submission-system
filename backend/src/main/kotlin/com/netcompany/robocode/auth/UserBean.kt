package com.netcompany.robocode.auth

import io.jsonwebtoken.Claims

interface UserBean {
    val isLoggedIn: Boolean
            get() = false
    val claims: Claims?
            get() = null
}