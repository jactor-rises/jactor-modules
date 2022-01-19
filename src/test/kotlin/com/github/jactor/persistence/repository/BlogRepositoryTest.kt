package com.github.jactor.persistence.repository

import com.github.jactor.persistence.dto.AddressInternalDto
import com.github.jactor.persistence.dto.BlogDto
import com.github.jactor.persistence.dto.BlogEntryDto
import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.entity.BlogEntity
import com.github.jactor.persistence.entity.BlogEntity.Companion.aBlog
import com.github.jactor.persistence.entity.BlogEntryEntity
import javax.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@Transactional
internal class BlogRepositoryTest {
    @Autowired
    private lateinit var blogRepositoryToTest: BlogRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @Test
    fun `should save and then read blog entity`() {
        val addressDto = AddressInternalDto(zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testing")
        val personDto = PersonInternalDto(address = addressDto, surname = "Adder")
        val userDto = UserInternalDto(PersistentDto(), personInternal = personDto, emailAddress = "public@services.com", username = "black")
        val blogEntityToSave = aBlog(BlogDto(created = LocalDate.now(), title = "Blah", userInternal = userDto))

        blogRepositoryToTest.save(blogEntityToSave)
        entityManager.flush()
        entityManager.clear()

        val blogs = blogRepositoryToTest.findAll()
        assertThat(blogs).`as`("blogs").hasSize(1)
        val blogEntity = blogs.iterator().next()

        assertAll(
            { assertThat(blogEntity.created).`as`("created").isEqualTo(LocalDate.now()) },
            { assertThat(blogEntity.title).`as`("title").isEqualTo("Blah") }
        )
    }

    @Test
    fun `should save then update and read blog entity`() {
        val addressDto = AddressInternalDto(zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testing")
        val personDto = PersonInternalDto(address = addressDto, surname = "Adder")
        val userDto = UserInternalDto(PersistentDto(), personInternal = personDto, emailAddress = "public@services.com", username = "black")
        val blogEntityToSave = aBlog(BlogDto(created = LocalDate.now(), title = "Blah", userInternal = userDto))

        blogRepositoryToTest.save(blogEntityToSave)
        entityManager.flush()
        entityManager.clear()

        val blogs = blogRepositoryToTest.findBlogsByTitle("Blah")
        assertThat(blogs).hasSize(1)

        val blogEntitySaved = blogs.iterator().next()
        blogEntitySaved.title = "Duh"

        blogRepositoryToTest.save(blogEntitySaved)
        entityManager.flush()
        entityManager.clear()

        val modifiedBlogs = blogRepositoryToTest.findBlogsByTitle("Duh")
        assertThat(modifiedBlogs).`as`("modified blogs").hasSize(1)
        val blogEntity: BlogEntity = modifiedBlogs.iterator().next()

        assertAll(
            { assertThat(blogEntity.created).`as`("created").isEqualTo(LocalDate.now()) },
            { assertThat(blogEntity.title).`as`("title").isEqualTo("Duh") }
        )
    }

    @Test
    fun `should find blog by title`() {
        val addressDto = AddressInternalDto(zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testing")
        val personDto = PersonInternalDto(address = addressDto, surname = "Adder")
        val userDto = UserInternalDto(PersistentDto(), personInternal = personDto, emailAddress = "public@services.com", username = "black")
        val blogEntityToSave = aBlog(BlogDto(created = LocalDate.now(), title = "Blah", userInternal = userDto))

        blogRepositoryToTest.save(blogEntityToSave)
        entityManager.flush()
        entityManager.clear()

        val blogs = blogRepositoryToTest.findBlogsByTitle("Blah")

        assertAll(
            { assertThat(blogs).`as`("blogs").hasSize(1) },
            { assertThat(blogs[0]).`as`("blog").isNotNull() },
            { assertThat(blogs[0].created).`as`("blog.created").isEqualTo(LocalDate.now()) }
        )
    }

    @Test
    fun `should be able to relate a blog entry`() {
        val addressDto = AddressInternalDto(zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testing")
        val personDto = PersonInternalDto(address = addressDto, surname = "Adder")
        val userDto = UserInternalDto(PersistentDto(), personInternal = personDto, emailAddress = "public@services.com", username = "black")
        val blogEntityToSave = aBlog(BlogDto(created = LocalDate.now(), title = "Blah", userInternal = userDto))
        val blogEntryToSave = BlogEntryEntity(BlogEntryDto(blog = blogEntityToSave.asDto(), creatorName = "arnold", entry = "i'll be back"))

        blogEntityToSave.add(blogEntryToSave)
        blogRepositoryToTest.save(blogEntityToSave)
        entityManager.flush()
        entityManager.clear()

        val blogs = blogRepositoryToTest.findBlogsByTitle("Blah")
        assertThat(blogs).hasSize(1)
        val blogEntity = blogs.iterator().next()
        assertThat(blogEntity.getEntries()).hasSize(1)
        val blogEntryEntity = blogEntity.getEntries().iterator().next()

        assertAll(
            { assertThat(blogEntryEntity.entry).`as`("entry").isEqualTo("i'll be back") },
            { assertThat(blogEntryEntity.creatorName).`as`("creatorName").isEqualTo("arnold") }
        )
    }
}
