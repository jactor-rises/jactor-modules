package com.github.jactor.persistence

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.withId
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isStrictlyBetween

internal class BlogEntryRepositoryTest @Autowired constructor(
    private val blogEntryRepository: BlogEntryRepository
) : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should save then read blog entry`() {
        val addressDto = Address(
            Persistent(id = UUID.randomUUID()), zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testing"
        )

        val personDto = Person(
            persistent = Persistent(id = UUID.randomUUID()), address = addressDto, surname = "Adder"
        )

        val userDto = User(
            Persistent(id = UUID.randomUUID()),
            personInternal = personDto,
            emailAddress = "public@services.com",
            username = "white"
        )

        var blogData = BlogBuilder.new(
            blog = Blog(created = LocalDate.now(), title = "and then some...", user = userDto)
        )

        val blogDto = blogData.blog

        blogData = blogData.withEntry(
            blogEntry = BlogEntry(blog = blogDto, creatorName = "smith", entry = "once upon a time")
        )

        flush { blogEntryRepository.save(blogData.buildBlogEntryEntity()) }

        val blogEntries = blogEntryRepository.findAll().toList()

        assertAll {
            assertThat(blogEntries).hasSize(1)
            val blogEntry = blogEntries.iterator().next()
            assertThat(blogEntry.creatorName).isEqualTo("smith")
            assertThat(blogEntry.timeOfCreation)
                .isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now())
            assertThat(blogEntry.entry).isEqualTo("once upon a time")
        }
    }

    @Test
    fun `should write then update and read a blog entry`() {
        val address = Address(
            zipCode = "1001",
            addressLine1 = "Test Boulevard 1",
            city = "Testing"
        ).withId()

        val person = Person(
            persistent = Persistent(id = UUID.randomUUID()),
            address = address, surname = "Adder"
        )

        val user = User(
            Persistent(id = UUID.randomUUID()),
            personInternal = person,
            emailAddress = "public@services.com",
            username = "dark"
        )

        val blogEntryToSave = BlogBuilder
            .new(blog = Blog(created = LocalDate.now(), title = "and then some...", user = user))
            .withEntry(BlogEntry(creatorName = "smith", entry = "once upon a time"))
            .buildBlogEntryEntity()

        flush { blogEntryRepository.save(blogEntryToSave) }

        val blogEntries = blogEntryRepository.findAll().toList()

        assertThat(blogEntries).hasSize(1)

        val blogEntry = blogEntries.iterator().next()
        blogEntry.modify("happily ever after", "luke")

        flush { blogEntryRepository.save(blogEntry) }

        val modifiedEntries = blogEntryRepository.findAll().toList()

        assertThat(modifiedEntries).hasSize(1)

        val modifiedEntry = modifiedEntries.iterator().next()

        assertAll {
            assertThat(modifiedEntry.creatorName).isEqualTo("luke")
            assertThat(modifiedEntry.timeOfModification)
                .isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now())
            assertThat(modifiedEntry.entry).isEqualTo("happily ever after")
        }
    }
}
