package com.github.jactor.persistence

import java.time.LocalDate
import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initBlog
import com.github.jactor.persistence.test.initBlogEntry
import com.github.jactor.persistence.test.initPerson
import com.github.jactor.shared.test.isNotOlderThan
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo

internal class BlogEntryRepositoryTest @Autowired constructor(
    private val blogEntryRepository: BlogEntryRepository
) : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should save then read blog entry`() {
        val address = initAddress(
            Persistent(id = UUID.randomUUID()), zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testing"
        )

        val person = initPerson(
            address = address, persistent = Persistent(id = UUID.randomUUID()), surname = "Adder",
        )

        val userDto = User(
            Persistent(id = UUID.randomUUID()),
            personInternal = person,
            emailAddress = "public@services.com",
            username = "white"
        )

        val blog = initBlog(created = LocalDate.now(), title = "and then some...", user = userDto).withId()
        val blogEntry = initBlogEntry(blog = blog, creatorName = "smith", entry = "once upon a time").withId()

        flush { blogEntryRepository.save(blogEntry.toEntity()) }

        val blogEntries = blogEntryRepository.findAll().toList()

        assertAll {
            assertThat(blogEntries).hasSize(1)
            val blogEntry = blogEntries.iterator().next()
            assertThat(blogEntry.creatorName).isEqualTo("smith")
            assertThat(blogEntry.timeOfCreation).isNotOlderThan(seconds = 1)
            assertThat(blogEntry.entry).isEqualTo("once upon a time")
        }
    }

    @Test
    fun `should write then update and read a blog entry`() {
        val address = initAddress(
            zipCode = "1001",
            addressLine1 = "Test Boulevard 1",
            city = "Testing"
        ).withId()

        val person = initPerson(
            address = address,
            persistent = Persistent(id = UUID.randomUUID()), surname = "Adder",
        )

        val user = User(
            Persistent(id = UUID.randomUUID()),
            personInternal = person,
            emailAddress = "public@services.com",
            username = "dark"
        )

        val blog = Blog(created = LocalDate.now(), title = "and then some...", user = user).withId()
        val blogEntry = BlogEntry(blog = blog, creatorName = "smith", entry = "once upon a time").withId()

        flush { blogEntryRepository.save(blogEntry.toEntity()) }

        val blogEntries = blogEntryRepository.findAll().toList()

        assertThat(blogEntries).hasSize(1)

        val readBlogEntry = blogEntries.iterator().next()
        readBlogEntry.modify("happily ever after", "luke")

        flush { blogEntryRepository.save(readBlogEntry) }

        val modifiedEntries = blogEntryRepository.findAll().toList()

        assertThat(modifiedEntries).hasSize(1)

        val modifiedEntry = modifiedEntries.iterator().next()

        assertAll {
            assertThat(modifiedEntry.creatorName).isEqualTo("luke")
            assertThat(modifiedEntry.timeOfModification).isNotOlderThan(seconds = 1)
            assertThat(modifiedEntry.entry).isEqualTo("happily ever after")
        }
    }
}
