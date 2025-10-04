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
class JactorPersistenceRepositiesConfig(
    private val addressRepository: AddressRepository,
    private val blogRepository: BlogRepository,
    private val guestBookRepository: GuestBookRepository,
    private val personRepository: PersonRepository,
    private val userRepository: UserRepository,
) {

    init {
        fetchAddressRelation = { addressRepository.findById(id = it) }
        fetchBlogRelation = { blogRepository.findBlogById(id = it) }
        fetchBlogRelations = { blogRepository.findBlogsByUserId(id = it) }
        fetchGuestBookRelation = { guestBookRepository.findGuestBookById(id = it) }
        fetchPersonRelation = { personRepository.findById(id = it) }
        fetchUserRelation = { userRepository.findById(id = it) }
        fetchUserRelations = { userRepository.findByPersonId(id = it) }
    }

    internal companion object {
        internal var fetchAddressRelation: (UUID) -> AddressDao? = { null }
        internal var fetchBlogRelation: (UUID) -> BlogDao? = { null }
        internal var fetchBlogRelations: (UUID) -> List<BlogDao> = { emptyList() }
        internal var fetchGuestBookRelation: (UUID) -> GuestBookDao? = { null }
        internal var fetchPersonRelation: (UUID) -> PersonDao? = { null }
        internal var fetchUserRelation: (UUID) -> UserDao? = { null }
        internal var fetchUserRelations: (UUID) -> List<UserDao> = { emptyList() }
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
