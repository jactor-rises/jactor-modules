package com.github.jactor.persistence

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext

@SpringBootApplication
@OpenAPIDefinition(
    info = Info(title = "jactor-persistence", version = "v1")
)
class JactorPersistence {

    companion object {
        @JvmStatic
        private val LOGGER = LoggerFactory.getLogger(JactorPersistence::class.java)

        fun inspect(applicationContext: ApplicationContext, args: Array<String>) {
            if (LOGGER.isDebugEnabled) {
                val arguments = if (args.isEmpty()) "without arguments!" else "with arguments: ${args.joinToString { " " }}!"

                LOGGER.debug("Starting application {}", arguments)

                val springBeanNames = SpringBeanNames()
                applicationContext.beanDefinitionNames.sorted().forEach(springBeanNames::add)

                LOGGER.debug("Available beans (only simple names):")
                springBeanNames.listBeanNames().forEach {
                    LOGGER.debug("- $it")
                }

                LOGGER.debug("Ready for service...")
            }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<JactorPersistence>(*args)
        }
    }

    internal class SpringBeanNames {
        private val beanNames: MutableList<String> = ArrayList()
        private val tenNames: MutableList<String> = ArrayList()

        fun add(name: String) {
            if (name.contains(".")) {
                val index = name.lastIndexOf('.')
                tenNames.add(name.substring(index + 1))
            } else {
                tenNames.add(name)
            }

            if (tenNames.size == 10) {
                beanNames.add(tenNames.joinToString(", "))
                tenNames.clear()
            }
        }

        fun listBeanNames(): List<String> {
            beanNames.add(tenNames.joinToString(", "))
            tenNames.clear()

            return beanNames
        }
    }
}
