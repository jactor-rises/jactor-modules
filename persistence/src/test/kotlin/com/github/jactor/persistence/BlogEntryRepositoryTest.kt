package com.github.jactor.persistence

import java.time.LocalDate
import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
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

internal class BlogEntryRepositoryTest() : AbstractSpringBootNoDirtyContextTest() {
    private val blogRepository: BlogRepository = BlogRepositoryObject

    @Test
    fun `should save then read blog entry`() {
        val address = initAddress(
            Persistent(id = UUID.randomUUID()), zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testing"
        )

        val person = initPerson(
            address = address, persistent = Persistent(id = UUID.randomUUID()), surname = "Adder",
        )

        val userDto = User(
            persistent = Persistent(id = UUID.randomUUID()),
            person = person,
            emailAddress = "public@services.com",
            username = "white",
            usertype = User.Usertype.ACTIVE
        )

        val blog = initBlog(created = LocalDate.now(), title = "and then some...", user = userDto)
        val blogEntry = initBlogEntry(blog = blog, creatorName = "smith", entry = "once upon a time")

        blogRepository.save(blogEntry.toBlogEntryDao())

        val blogEntries = blogRepository.findBlogEntries()

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
        )

        val person = initPerson(
            address = address,
            persistent = Persistent(id = UUID.randomUUID()), surname = "Adder",
        )

        val user = User(
            persistent = Persistent(id = UUID.randomUUID()),
            person = person,
            emailAddress = "public@services.com",
            username = "dark",
            usertype = User.Usertype.ACTIVE,
        )

        val blog = Blog(created = LocalDate.now(), title = "and then some...", user = user)
        val blogEntry = BlogEntry(blog = blog, creatorName = "smith", entry = "once upon a time")

        blogRepository.save(blogEntry.toBlogEntryDao())

        val blogEntries = blogRepository.findBlogEntries()

        assertThat(blogEntries).hasSize(1)

        val readBlogEntry = blogEntries.firstOrNull() ?: fail { "No BlogEntry, even when size is one?..." }
        readBlogEntry.apply { entry = "happily evewr after" }.modifiedBy(modifier = "luke")

        blogRepository.save(readBlogEntry)

        val modifiedEntries = blogRepository.findBlogEntries()

        assertThat(modifiedEntries).hasSize(1)

        val modifiedEntry = modifiedEntries.firstOrNull() ?: fail { "No BlogEntry, even when size is one?..." }

        assertAll {
            assertThat(modifiedEntry.creatorName).isEqualTo("luke")
            assertThat(modifiedEntry.timeOfModification).isNotOlderThan(seconds = 1)
            assertThat(modifiedEntry.entry).isEqualTo("happily ever after")
        }
    }
}
