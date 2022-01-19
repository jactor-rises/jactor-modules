package com.github.jactor.persistence.service

import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.entity.PersonEntity
import com.github.jactor.persistence.repository.PersonRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDateTime
import java.util.Optional

@SpringBootTest
internal class PersonServiceTest {

    @Autowired
    private lateinit var personService: PersonService

    @MockBean
    private lateinit var personRepositoryMock: PersonRepository

    @Test
    fun `should create a new Person`() {
        whenever(personRepositoryMock.save(ArgumentMatchers.any())).thenReturn(PersonEntity())

        personService.createWhenNotExists(PersonInternalDto())
        val personEntityCaptor = ArgumentCaptor.forClass(PersonEntity::class.java)

        verify(personRepositoryMock).save(personEntityCaptor.capture())
        assertThat(personEntityCaptor.value).isNotNull
    }

    @Test
    fun `should find Person by id`() {
        // given
        val personEntity = PersonEntity()
        whenever(personRepositoryMock.findById(1L)).thenReturn(Optional.of(personEntity))

        // when
        val person = personService.createWhenNotExists(
            PersonInternalDto(PersistentDto(1L, "creator", LocalDateTime.now(), "modifier", LocalDateTime.now()), PersonInternalDto())
        )

        // then
        assertAll(
            { verify(personRepositoryMock, Mockito.never()).save(ArgumentMatchers.any()) },
            { assertThat(person).`as`("person").isEqualTo(personEntity) }
        )
    }
}
