package com.netcompany.robocode.security

interface SecurityPredicate {
    fun checkAccess(params: Map<String, Any>): Boolean
}