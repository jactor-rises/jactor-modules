package com.github.jactor.persistence

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.common.PersistentModel
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isStrictlyBetween

internal class BlogEntryRepositoryTest : AbstractSpringBootNoDirtyContextTest() {
    @Autowired
    private lateinit var blogEntryRepository: BlogEntryRepository

    @Test
    fun `should save then read blog entry`() {
        val addressDto = AddressModel(
            PersistentModel(id = UUID.randomUUID()), zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testing"
        )

        val personDto = PersonModel(
            persistentModel = PersistentModel(id = UUID.randomUUID()), address = addressDto, surname = "Adder"
        )

        val userDto = UserModel(
            PersistentModel(id = UUID.randomUUID()),
            personInternal = personDto,
            emailAddress = "public@services.com",
            username = "white"
        )

        var blogData = BlogBuilder.new(
            blogModel = BlogModel(created = LocalDate.now(), title = "and then some...", user = userDto)
        )

        val blogDto = blogData.blogModel

        blogData = blogData.withEntry(
            blogEntryModel = BlogEntryModel(blog = blogDto, creatorName = "smith", entry = "once upon a time")
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
        val addressDto = AddressBuilder
            .new(
                addressModel = AddressModel(
                    zipCode = "1001",
                    addressLine1 = "Test Boulevard 1",
                    city = "Testing"
                )
            )
            .addressModel

        val personDto = PersonModel(
            persistentModel = PersistentModel(id = UUID.randomUUID()), address = addressDto, surname = "Adder"
        )

        val userDto = UserModel(
            PersistentModel(id = UUID.randomUUID()),
            personInternal = personDto,
            emailAddress = "public@services.com",
            username = "dark"
        )

        val blogEntryToSave = BlogBuilder
            .new(blogModel = BlogModel(created = LocalDate.now(), title = "and then some...", user = userDto))
            .withEntry(BlogEntryModel(creatorName = "smith", entry = "once upon a time"))
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
