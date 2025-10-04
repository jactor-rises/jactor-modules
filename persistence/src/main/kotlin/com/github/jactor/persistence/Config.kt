package com.github.jactor.persistence

import java.text.SimpleDateFormat
import java.util.UUID
import javax.sql.DataSource
import org.jetbrains.exposed.v1.core.DatabaseConfig
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.TransactionManager
import org.jetbrains.exposed.v1.spring.transaction.SpringTransactionManager
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info

@Configuration
class ExposedConfig {

    @Bean
    fun databaseConfig() = DatabaseConfig {
        useNestedTransactions = true
    }

    @Bean
    fun database(dataSource: DataSource, databaseConfig: DatabaseConfig): Database =
        Database.connect(dataSource, databaseConfig = databaseConfig).also {
            TransactionManager.defaultDatabase = it
        }

    @Bean
    fun transactionManager(dataSource: DataSource) = SpringTransactionManager(dataSource)
}

@Configuration
class JactorPersistenceRepositiesConfig {
    @Bean
    fun addressRepository(): AddressRepository = AddressRepositoryObject

    @Bean
    fun blogRepository(): BlogRepository = BlogRepositoryObject

    @Bean
    fun guestBookRepository(): GuestBookRepository = GuestBookRepositoryObject

    @Bean
    fun personRepository(): PersonRepository = PersonRepositoryObject

    @Bean
    fun userRepository(): UserRepository = UserRepositoryObject

    internal companion object {
        internal var fetchAddressRelation: (UUID) -> AddressDao? = { AddressRepositoryObject.findById(id = it) }
        internal var fetchBlogRelation: (UUID) -> BlogDao? = { BlogRepositoryObject.findBlogById(id = it) }
        internal var fetchBlogRelations: (UUID) -> List<BlogDao> = { BlogRepositoryObject.findBlogsByUserId(id = it) }
        internal var fetchPersonRelation: (UUID) -> PersonDao? = { PersonRepositoryObject.findById(id = it) }
        internal var fetchUserRelation: (UUID) -> UserDao? = { UserRepositoryObject.findById(id = it) }
        internal var fetchUserRelations: (UUID) -> List<UserDao> = { UserRepositoryObject.findByPersonId(id = it) }
        internal var fetchGuestBookRelation: (UUID) -> GuestBookDao? = {
            GuestBookRepositoryObject.findGuestBookById(id = it)
        }
    }
}

@Configuration
@OpenAPIDefinition(info = Info(title = "jactor-persistence", version = "v1"))
class JactorPersistenceConfig {

    @Bean
    fun commandLineRunner(applicationContext: ApplicationContext): CommandLineRunner {
        return CommandLineRunner {
            JactorPersistence.inspect(applicationContext, it)
        }
    }

    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper()
        .registerModule(JavaTimeModule())
        .registerModule(Jdk8Module())
        .setDateFormat(SimpleDateFormat("yyyy-MM-dd"))
        .setSerializationInclusion(Include.NON_NULL)
}
