package com.github.jactor.persistence.repository

import com.github.jactor.persistence.dto.AddressInternalDto
import com.github.jactor.persistence.dto.GuestBookDto
import com.github.jactor.persistence.dto.GuestBookEntryDto
import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.entity.GuestBookEntity.Companion.aGuestBook
import com.github.jactor.persistence.entity.GuestBookEntryEntity
import com.github.jactor.persistence.entity.GuestBookEntryEntity.Companion.aGuestBookEntry
import com.github.jactor.persistence.entity.UserEntity
import javax.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
internal class GuestBookEntryRepositoryTest {

    @Autowired
    private lateinit var guestBookEntryRepository: GuestBookEntryRepository

    @Autowired
    private lateinit var guestBookRepository: GuestBookRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `should save then read guest book entry entity`() {
        val addressDto = AddressInternalDto(zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington")
        val personDto = PersonInternalDto(address = addressDto, surname = "AA")
        val userDto = UserInternalDto(PersistentDto(), personInternal = personDto, emailAddress = "casuel@tantooine.com", username = "causual")
        val savedUser = userRepository.save(UserEntity(userDto))

        savedUser.setGuestBook(aGuestBook(GuestBookDto(entries = emptySet(), title = "home sweet home", userInternal = savedUser.asDto())))

        val savedGuestBook = guestBookRepository.save(savedUser.guestBook!!)

        savedGuestBook.add(
            aGuestBookEntry(
                GuestBookEntryDto(
                    guestBook = savedUser.guestBook?.asDto(),
                    creatorName = "Harry",
                    entry = "Draco Dormiens Nunquam Tittilandus"
                )
            )
        )

        guestBookEntryRepository.saveAll(savedGuestBook.getEntries())
        entityManager.flush()
        entityManager.clear()

        val entriesByGuestBook = guestBookEntryRepository.findByGuestBook(savedUser.guestBook!!)
        assertThat(entriesByGuestBook).hasSize(1)
        val entry = entriesByGuestBook.iterator().next()

        assertAll(
            { assertThat(entry.creatorName).`as`("creator name").isEqualTo("Harry") },
            { assertThat(entry.entry).`as`("entry").isEqualTo("Draco Dormiens Nunquam Tittilandus") }
        )
    }

    @Test
    fun `should save then modify and read guest book entry entity`() {
        val addressDto = AddressInternalDto(zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington")
        val personDto = PersonInternalDto(address = addressDto, surname = "AA")
        val userDto = UserInternalDto(
            persistentDto = PersistentDto(), personInternal = personDto, emailAddress = "casuel@tantooine.com", username = "causual"
        )

        val savedUser = userRepository.save(UserEntity(userDto))

        savedUser.setGuestBook(aGuestBook(GuestBookDto(entries = emptySet(), title = "home sweet home", userInternal = savedUser.asDto())))

        val savedGuestBook = guestBookRepository.save(savedUser.guestBook!!)

        savedGuestBook.add(
            aGuestBookEntry(
                GuestBookEntryDto(
                    guestBook = savedUser.guestBook!!.asDto(),
                    creatorName = "Harry",
                    entry = "Draco Dormiens Nunquam Tittilandus"
                )
            )
        )

        guestBookEntryRepository.saveAll(savedGuestBook.getEntries())
        entityManager.flush()
        entityManager.clear()

        val entriesByGuestBook = guestBookEntryRepository.findByGuestBook(savedUser.guestBook!!)
        assertThat(entriesByGuestBook).hasSize(1)
        entriesByGuestBook.iterator().next().modify("Willie", "On the road again")

        guestBookEntryRepository.save<GuestBookEntryEntity>(entriesByGuestBook.iterator().next())

        entityManager.flush()
        entityManager.clear()

        val modifiedEntriesByGuestBook = guestBookEntryRepository.findByGuestBook(savedUser.guestBook!!)
        assertThat(modifiedEntriesByGuestBook).`as`("entries").hasSize(1)
        val entry = modifiedEntriesByGuestBook.iterator().next()

        assertAll(
            { assertThat(entry.creatorName).isEqualTo("Willie") },
            { assertThat(entry.entry).isEqualTo("On the road again") }
        )
    }

    @Test
    fun `should write two entries to two different guest books and then find one entry`() {
        val addressDto = AddressInternalDto(zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington")
        val personDto = PersonInternalDto(address = addressDto, surname = "AA")
        val userDto = UserInternalDto(PersistentDto(), personInternal = personDto, emailAddress = "casuel@tantooine.com", username = "causual")
        val savedUser = userRepository.save(UserEntity(userDto))

        savedUser.setGuestBook(aGuestBook(GuestBookDto(entries = emptySet(), title = "home sweet home", userInternal = savedUser.asDto())))
        val savedGuestBook = guestBookRepository.save(savedUser.guestBook!!)

        savedGuestBook.add(aGuestBookEntry(GuestBookEntryDto(guestBook = savedGuestBook.asDto(), creatorName = "somone", entry = "jadda")))

        guestBookEntryRepository.saveAll(savedGuestBook.getEntries())

        val anotherUserDto = UserInternalDto(PersistentDto(), personInternal = personDto, emailAddress = "hidden@tantooine.com", username = "hidden")
        val anotherSavedUser = userRepository.save(UserEntity(anotherUserDto))
        anotherSavedUser.setGuestBook(aGuestBook(GuestBookDto(entries = emptySet(), title = "home sweet home", userInternal = savedUser.asDto())))

        val anotherSavedGuestBook = guestBookRepository.save(anotherSavedUser.guestBook!!)

        anotherSavedGuestBook.add(
            aGuestBookEntry(GuestBookEntryDto(guestBook = anotherSavedGuestBook.asDto(), creatorName = "shrek", entry = "far far away"))
        )

        guestBookEntryRepository.saveAll(anotherSavedGuestBook.getEntries())
        entityManager.flush()
        entityManager.clear()

        val entriesByGuestBook = guestBookEntryRepository.findByGuestBook(anotherSavedGuestBook)
        assertThat(guestBookEntryRepository.findAll()).`as`("all entries").hasSize(2)

        assertAll(
            { assertThat(entriesByGuestBook).`as`("entriesByGuestBook").hasSize(1) },
            { assertThat(entriesByGuestBook[0].creatorName).`as`("entry.creatorName").isEqualTo("shrek") },
            { assertThat(entriesByGuestBook[0].entry).`as`("entry.entry").isEqualTo("far far away") }
        )
    }
}
