package com.netcompany.robocode

import org.glassfish.jersey.server.ResourceConfig
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.stereotype.Component
import org.springframework.util.ClassUtils
import java.util.stream.Collectors
import javax.ws.rs.Path
import javax.ws.rs.ext.Provider
import org.glassfish.jersey.media.multipart.MultiPartFeature




@Component
class JerseyConfig : ResourceConfig() {
    init {
        val scanner = ClassPathScanningCandidateComponentProvider(false)
        scanner.addIncludeFilter(AnnotationTypeFilter(Provider::class.java))
        scanner.addIncludeFilter(AnnotationTypeFilter(Path::class.java))
        val list = scanner.findCandidateComponents("com.netcompany.robocode.rest").stream()
                .map { beanDefinition -> ClassUtils.resolveClassName(beanDefinition.beanClassName, this.classLoader) }
                .collect(Collectors.toSet())
        registerClasses(list)
        registerClasses(MultiPartFeature::class.java)
    }
}