package com.github.jactor.web

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
class JactorWeb : WebMvcConfigurer {
    @Bean
    fun commandLineRunner(applicationContext: ApplicationContext): CommandLineRunner {
        return CommandLineRunner { args: Array<String>? -> inspect(applicationContext, args ?: emptyArray()) }
    }

    private fun inspect(applicationContext: ApplicationContext, args: Array<String>) {
        if (LOGGER.isDebugEnabled) {
            val arguments = if (args.isEmpty()) "without arguments!" else "with arguments: ${args.joinToString { " " }}!"

            LOGGER.debug("Starting jactor-web {}", arguments)

            val springBeanNames = SpringBeanNames()
            applicationContext.beanDefinitionNames.sorted().forEach(springBeanNames::add)

            LOGGER.debug("Available beans (only simple names):")
            springBeanNames.listBeanNames().forEach {
                LOGGER.debug("- $it")
            }

            LOGGER.debug("Ready for service...")
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(JactorWeb::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(JactorWeb::class.java, *args)
        }
    }
}