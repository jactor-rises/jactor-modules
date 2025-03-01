package com.github.jactor.web

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import com.github.jactor.shared.SpringBeanNames
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

@SpringBootApplication
class JactorWeb : WebMvcConfigurer {
    @Bean
    fun commandLineRunner(applicationContext: ApplicationContext): CommandLineRunner {
        return CommandLineRunner { args: Array<String>? -> inspect(applicationContext, args ?: emptyArray()) }
    }

    private fun inspect(applicationContext: ApplicationContext, args: Array<String>) {
        logger.debug {
            logger.debug { "Starting jactor-web ${gatherArgs(args)}" }
            logger.debug { "Available beans (only simple names):" }

            SpringBeanNames().also { springBeanNames ->
                applicationContext.beanDefinitionNames.sorted().forEach(springBeanNames::add)
                springBeanNames.listBeanNames().forEach {
                    logger.debug { "- $it" }
                }
            }

            "Ready for service..."
        }
    }

    fun gatherArgs(args: Array<String>): String {
        val arguments = if (args.isEmpty()) {
            "without arguments!"
        } else {
            "with arguments: ${args.joinToString { " " }}!"
        }
        return arguments
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(JactorWeb::class.java, *args)
}
