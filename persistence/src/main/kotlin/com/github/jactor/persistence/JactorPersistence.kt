package com.github.jactor.persistence

import com.github.jactor.shared.SpringBeanNames
import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext

private val logger = KotlinLogging.logger {}

@SpringBootApplication
@OpenAPIDefinition(info = Info(title = "jactor-persistence", version = "v2"))
class JactorPersistence {
    companion object {
        fun inspect(applicationContext: ApplicationContext, args: Array<String>) {
            logger.debug {
                logger.debug { "Starting application ${list(args)}" }
                SpringBeanNames().also { springBeanNames ->
                    applicationContext.beanDefinitionNames.sorted().forEach(springBeanNames::add)

                    logger.debug { "Available beans (only simple names):" }

                    springBeanNames.names.forEach {
                        logger.debug { "> $it" }
                    }
                }

                "Ready for service..."
            }
        }
    }
}

private fun list(args: Array<String>) = when (args.isEmpty()) {
    true -> "without arguments!"
    false -> "with arguments: ${args.joinToString { " " }}!"
}

fun main(args: Array<String>) {
    runApplication<JactorPersistence>(*args)
}
