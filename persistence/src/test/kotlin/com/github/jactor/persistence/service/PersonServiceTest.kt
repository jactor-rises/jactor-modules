package com.github.jactor.persistence.service

import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.entity.PersonEntity
import com.github.jactor.persistence.repository.PersonRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.called
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
internal class PersonServiceTest {

    @Autowired
    private lateinit var personService: PersonService

    @MockkBean
    private lateinit var personRepositoryMock: PersonRepository

    @Test
    fun `should create a new Person`() {
        val personEntitySlot = slot<PersonEntity>()
        every { personRepositoryMock.save(capture(personEntitySlot)) } returns PersonEntity()

        personService.createWhenNotExists(PersonInternalDto())

        assertThat(personEntitySlot.captured).isNotNull
    }

    @Test
    fun `should find Person by id`() {
        // given
        val personEntity = PersonEntity()
        every { personRepositoryMock.findById(1L) } returns Optional.of(personEntity)

        // when
        val person = personService.createWhenNotExists(
            PersonInternalDto(
                PersistentDto(1L, "creator", LocalDateTime.now(), "modifier", LocalDateTime.now()),
                PersonInternalDto()
            )
        )

        // then
        assertAll(
            { verify { personRepositoryMock.save(any()) wasNot called } },
            { assertThat(person).`as`("person").isEqualTo(personEntity) }
        )
    }
}
