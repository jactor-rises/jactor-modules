package com.github.jactor.rises.persistence.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.jetbrains.exposed.v1.core.DatabaseConfig
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.TransactionManager
import org.jetbrains.exposed.v1.spring.transaction.SpringTransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
@OpenAPIDefinition(info = Info(title = "jactor-persistence.exposed", version = "v2"))
class ExposedConfig {
    @Bean
    fun databaseConfig() = DatabaseConfig.Companion {
        useNestedTransactions = true
    }

    @Bean
    fun database(dataSource: DataSource, databaseConfig: DatabaseConfig): Database =
        Database.Companion.connect(dataSource, databaseConfig = databaseConfig).also {
            TransactionManager.Companion.defaultDatabase = it
        }

    @Bean
    fun transactionManager(dataSource: DataSource) = SpringTransactionManager(dataSource)
}
