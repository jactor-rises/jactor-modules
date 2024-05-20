package com.github.jactor.persistence.repository

import java.time.LocalDate
import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.dto.AddressInternalDto
import com.github.jactor.persistence.dto.BlogDto
import com.github.jactor.persistence.dto.BlogEntryDto
import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.entity.AddressBuilder
import com.github.jactor.persistence.entity.BlogBuilder
import com.github.jactor.persistence.entity.BlogEntity
import com.github.jactor.persistence.entity.BlogEntryEntity
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull

internal class BlogRepositoryTest : AbstractSpringBootNoDirtyContextTest(){
    @Test
    fun `should save and then read blog entity`() {
        val addressDto = AddressBuilder
            .new(
                addressInternalDto = AddressInternalDto(
                    zipCode = "1001",
                    addressLine1 = "Test Boulevard 1",
                    city = "Testing"
                )
            ).addressInternalDto

        val personDto = PersonInternalDto(
            persistentDto = PersistentDto(id = UUID.randomUUID()), address = addressDto, surname = "Adder"
        )

        val userDto = UserInternalDto(
            PersistentDto(id = UUID.randomUUID()),
            personInternal = personDto,
            emailAddress = "public@services.com",
            username = "black"
        )

        val blogEntityToSave = BlogBuilder
            .new(blogDto = BlogDto(created = LocalDate.now(), title = "Blah", userInternal = userDto))
            .buildBlogEntity()

        blogRepository.save(blogEntityToSave)
        entityManager.flush()
        entityManager.clear()

        val blogs = blogRepository.findAll().toList()
        assertThat(blogs).hasSize(1)
        val blogEntity = blogs.iterator().next()

        assertAll {
            assertThat(blogEntity.created).isEqualTo(LocalDate.now())
            assertThat(blogEntity.title).isEqualTo("Blah")
        }
    }

    @Test
    fun `should save then update and read blog entity`() {
        val addressDto = AddressBuilder
            .new(
                addressInternalDto = AddressInternalDto(
                    zipCode = "1001",
                    addressLine1 = "Test Boulevard 1",
                    city = "Testing"
                )
            )
            .addressInternalDto

        val personDto = PersonInternalDto(
            persistentDto = PersistentDto(id = UUID.randomUUID()), address = addressDto, surname = "Adder"
        )

        val userDto = UserInternalDto(
            PersistentDto(id = UUID.randomUUID()),
            personInternal = personDto,
            emailAddress = "public@services.com",
            username = "black"
        )

        val blogEntityToSave = BlogBuilder
            .new(blogDto = BlogDto(created = LocalDate.now(), title = "Blah", userInternal = userDto))
            .buildBlogEntity()

        blogRepository.save(blogEntityToSave)
        entityManager.flush()
        entityManager.clear()

        val blogs = blogRepository.findBlogsByTitle("Blah")
        assertThat(blogs).hasSize(1)

        val blogEntitySaved = blogs.iterator().next()
        blogEntitySaved.title = "Duh"

        blogRepository.save(blogEntitySaved)
        entityManager.flush()
        entityManager.clear()

        val modifiedBlogs = blogRepository.findBlogsByTitle("Duh")
        assertThat(modifiedBlogs).hasSize(1)
        val blogEntity: BlogEntity = modifiedBlogs.iterator().next()

        assertAll {
            assertThat(blogEntity.created).isEqualTo(LocalDate.now())
            assertThat(blogEntity.title).isEqualTo("Duh")
        }
    }

    @Test
    fun `should find blog by title`() {
        val addressDto = AddressBuilder
            .new(
                addressInternalDto = AddressInternalDto(
                    zipCode = "1001",
                    addressLine1 = "Test Boulevard 1",
                    city = "Testing"
                )
            ).addressInternalDto

        val personDto = PersonInternalDto(
            persistentDto = PersistentDto(id = UUID.randomUUID()), address = addressDto, surname = "Adder"
        )

        val userDto = UserInternalDto(
            PersistentDto(id = UUID.randomUUID()),
            personInternal = personDto,
            emailAddress = "public@services.com",
            username = "black"
        )

        val blogEntityToSave = BlogBuilder
            .new(blogDto = BlogDto(created = LocalDate.now(), title = "Blah", userInternal = userDto))
            .buildBlogEntity()

        blogRepository.save(blogEntityToSave)
        entityManager.flush()
        entityManager.clear()

        val blogs = blogRepository.findBlogsByTitle("Blah")

        assertAll {
            assertThat(blogs).hasSize(1)
            assertThat(blogs[0]).isNotNull()
            assertThat(blogs[0].created).isEqualTo(LocalDate.now())

        }
    }

    @Test
    fun `should be able to relate a blog entry`() {
        val addressDto = AddressBuilder
            .new(
                addressInternalDto = AddressInternalDto(
                    zipCode = "1001",
                    addressLine1 = "Test Boulevard 1",
                    city = "Testing"
                )
            ).addressInternalDto

        val personDto = PersonInternalDto(
            persistentDto = PersistentDto(id = UUID.randomUUID()), address = addressDto, surname = "Adder"
        )

        val userDto = UserInternalDto(
            persistentDto = PersistentDto(id = UUID.randomUUID()),
            personInternal = personDto,
            emailAddress = "public@services.com",
            username = "black"
        )

        var blogData = BlogBuilder.new(
            blogDto = BlogDto(
                created = LocalDate.now(), title = "Blah", userInternal = userDto
            )
        )

        val blogEntityToSave: BlogEntity = blogData.buildBlogEntity()

        blogData = blogData.withEntry(
            blogEntryDto = BlogEntryDto(
                blog = blogEntityToSave.asDto(),
                creatorName = "arnold",
                entry = "i'll be back"
            )
        )

        val blogEntryToSave: BlogEntryEntity = blogData.buildBlogEntryEntity()

        blogEntityToSave.add(blogEntryToSave)
        blogRepository.save(blogEntityToSave)
        entityManager.flush()
        entityManager.clear()

        val blogs = blogRepository.findBlogsByTitle("Blah")
        assertThat(blogs).hasSize(1)
        val blogEntity = blogs.iterator().next()
        assertThat(blogEntity.getEntries()).hasSize(1)
        val blogEntryEntity = blogEntity.getEntries().iterator().next()

        assertAll {
            assertThat(blogEntryEntity.entry).isEqualTo("i'll be back")
            assertThat(blogEntryEntity.creatorName).isEqualTo("arnold")
        }
    }
}
