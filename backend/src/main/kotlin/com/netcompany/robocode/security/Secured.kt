package com.netcompany.robocode.security

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Secured(vararg val value: KClass<out SecurityPredicate>)