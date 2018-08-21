package com.netcompany.robocode.security

import com.netcompany.robocode.auth.AuthenticationException
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Collectors

@Aspect
@Component
class SecurityAspect(private val applicationContext: ApplicationContext) {

    @Around("@annotation(Secured)")
    @Throws(Throwable::class)
    fun handleMethodCall(proceedingJoinPoint: ProceedingJoinPoint): Any {
        val predicates = getPredicates(proceedingJoinPoint)
        val arguments = getArguments(proceedingJoinPoint)

        if (predicates.stream().noneMatch { predicate -> predicate.checkAccess(arguments) }) {
            throw AuthenticationException("No security predicated returned true")
        }

        return proceedingJoinPoint.proceed()
    }

    /**
     * Gets all the SecurityPredicates that should be evaluated for the method being called.
     *
     * @param proceedingJoinPoint The join point before the method
     * @return The list of SecurityPredicates
     */
    private fun getPredicates(proceedingJoinPoint: ProceedingJoinPoint): List<SecurityPredicate> {
        val signature = proceedingJoinPoint.signature as MethodSignature
        val method = signature.method
        val annotation = method.getAnnotation(Secured::class.java) as Secured

        return Arrays.stream(annotation.value)
                .map { applicationContext.getBean(it.java) }
                .collect(Collectors.toList())
    }

    /**
     * Gets a map with the method parameter names and valued for the method being called.
     *
     * @param proceedingJoinPoint The join point before the method
     * @return A map of the method parameter names and values
     */
    private fun getArguments(proceedingJoinPoint: ProceedingJoinPoint): Map<String, Any> {
        val signature = proceedingJoinPoint.signature as MethodSignature
        val parameterValues = proceedingJoinPoint.args
        val parameterNames = signature.parameterNames

        val arguments = HashMap<String, Any>()

        for (i in parameterNames.indices) {
            arguments[parameterNames[i]] = parameterValues[i]
        }

        return arguments
    }
}