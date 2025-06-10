package com.github.jactor.persistence.blog

import java.time.LocalDate
import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.address.AddressModel
import com.github.jactor.persistence.common.PersistentModel
import com.github.jactor.persistence.person.PersonModel
import com.github.jactor.persistence.user.UserModel
import com.github.jactor.persistence.address.AddressBuilder
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull

internal class BlogRepositoryTest : AbstractSpringBootNoDirtyContextTest() {
    @Autowired
    private lateinit var blogRepository: BlogRepository

    @Test
    fun `should save and then read blog entity`() {
        val addressDto = AddressBuilder
            .new(
                addressModel = AddressModel(
                    zipCode = "1001",
                    addressLine1 = "Test Boulevard 1",
                    city = "Testing"
                )
            ).addressModel

        val personDto = PersonModel(
            persistentModel = PersistentModel(id = UUID.randomUUID()), address = addressDto, surname = "Adder"
        )

        val userDto = UserModel(
            PersistentModel(id = UUID.randomUUID()),
            personInternal = personDto,
            emailAddress = "public@services.com",
            username = "black"
        )

        val blogEntityToSave = BlogBuilder
            .new(blogModel = BlogModel(created = LocalDate.now(), title = "Blah", user = userDto))
            .buildBlogEntity()

        blogRepository.save(blogEntityToSave)

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
            username = "black"
        )

        val blogEntityToSave = BlogBuilder
            .new(blogModel = BlogModel(created = LocalDate.now(), title = "Blah", user = userDto))
            .buildBlogEntity()

        blogRepository.save(blogEntityToSave)

        val blogs = blogRepository.findBlogsByTitle("Blah")
        assertThat(blogs).hasSize(1)

        val blogEntitySaved = blogs.iterator().next()
        blogEntitySaved.title = "Duh"

        blogRepository.save(blogEntitySaved)

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
                addressModel = AddressModel(
                    zipCode = "1001",
                    addressLine1 = "Test Boulevard 1",
                    city = "Testing"
                )
            ).addressModel

        val personDto = PersonModel(
            persistentModel = PersistentModel(id = UUID.randomUUID()), address = addressDto, surname = "Adder"
        )

        val userDto = UserModel(
            PersistentModel(id = UUID.randomUUID()),
            personInternal = personDto,
            emailAddress = "public@services.com",
            username = "black"
        )

        val blogEntityToSave = BlogBuilder
            .new(blogModel = BlogModel(created = LocalDate.now(), title = "Blah", user = userDto))
            .buildBlogEntity()

        blogRepository.save(blogEntityToSave)

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
                addressModel = AddressModel(
                    zipCode = "1001",
                    addressLine1 = "Test Boulevard 1",
                    city = "Testing"
                )
            ).addressModel

        val personDto = PersonModel(
            persistentModel = PersistentModel(id = UUID.randomUUID()), address = addressDto, surname = "Adder"
        )

        val userDto = UserModel(
            persistentModel = PersistentModel(id = UUID.randomUUID()),
            personInternal = personDto,
            emailAddress = "public@services.com",
            username = "black"
        )

        var blogData = BlogBuilder.new(
            blogModel = BlogModel(
                created = LocalDate.now(), title = "Blah", user = userDto
            )
        )

        val blogEntityToSave: BlogEntity = blogData.buildBlogEntity()

        blogData = blogData.withEntry(
            blogEntryModel = BlogEntryModel(
                blog = blogEntityToSave.toModel(),
                creatorName = "arnold",
                entry = "i'll be back"
            )
        )

        val blogEntryToSave: BlogEntryEntity = blogData.buildBlogEntryEntity()

        blogEntityToSave.add(blogEntryToSave)
        blogRepository.save(blogEntityToSave)

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
