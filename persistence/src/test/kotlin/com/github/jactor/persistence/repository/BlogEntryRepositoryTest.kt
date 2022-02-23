package com.github.jactor.persistence.repository

import com.github.jactor.persistence.dto.AddressInternalDto
import com.github.jactor.persistence.dto.BlogDto
import com.github.jactor.persistence.dto.BlogEntryDto
import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.entity.BlogEntryEntity.Companion.aBlogEntry
import javax.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@SpringBootTest
@Transactional
internal class BlogEntryRepositoryTest {

    @Autowired
    private lateinit var blogEntryRepository: BlogEntryRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @Test
    fun `should save then read blog entry`() {
        val addressDto = AddressInternalDto(PersistentDto(), zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testing")
        val personDto = PersonInternalDto(address = addressDto, surname = "Adder")
        val userDto = UserInternalDto(PersistentDto(), personInternal = personDto, emailAddress = "public@services.com", username = "white")
        val blogDto = BlogDto(created = LocalDate.now(), title = "and then some...", userInternal = userDto)
        val blogEntryToSave = aBlogEntry(BlogEntryDto(blog = blogDto, creatorName = "smith", entry = "once upon a time"))

        blogEntryRepository.save(blogEntryToSave)
        entityManager.flush()
        entityManager.clear()

        val blogEntries = blogEntryRepository.findAll()

        assertAll(
            { assertThat(blogEntries).hasSize(1) },
            {
                val blogEntry = blogEntries.iterator().next()
                assertAll(
                    { assertThat(blogEntry.creatorName).`as`("entry.creatorName").isEqualTo("smith") },
                    {
                        assertThat(blogEntry.timeOfCreation).`as`("entry.creationTime")
                            .isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now())
                    },
                    { assertThat(blogEntry.entry).`as`("entry.entry").isEqualTo("once upon a time") }
                )
            }
        )
    }

    @Test
    fun `should write then update and read a blog entry`() {
        val addressDto = AddressInternalDto(zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testing")
        val personDto = PersonInternalDto(PersistentDto(), address = addressDto, surname = "Adder")
        val userDto = UserInternalDto(PersistentDto(), personInternal = personDto, emailAddress = "public@services.com", username = "dark")
        val blogDto = BlogDto(created = LocalDate.now(), title = "and then some...", userInternal = userDto)
        val blogEntryToSave = aBlogEntry(BlogEntryDto(blog = blogDto, creatorName = "smith", entry = "once upon a time"))

        blogEntryRepository.save(blogEntryToSave)
        entityManager.flush()
        entityManager.clear()

        val blogEntries = blogEntryRepository.findAll()

        assertThat(blogEntries).hasSize(1)

        val blogEntry = blogEntries.iterator().next()
        blogEntry.modify("happily ever after", "luke")

        blogEntryRepository.save(blogEntry)
        entityManager.flush()
        entityManager.clear()

        val modifiedEntries = blogEntryRepository.findAll()

        assertThat(modifiedEntries).hasSize(1)

        val modifiedEntry = modifiedEntries.iterator().next()

        assertAll(
            { assertThat(modifiedEntry.creatorName).`as`("entry.creatorName").isEqualTo("luke") },
            {
                assertThat(modifiedEntry.timeOfModification).`as`("entry.timeOfModification")
                    .isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now())
            },
            { assertThat(modifiedEntry.entry).`as`("entry.entry").isEqualTo("happily ever after") }
        )
    }
}