package com.github.jactor.persistence

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initPerson
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull

internal class GuestBookEntryRepositoryTest @Autowired constructor(
    private val guestBookRepository: GuestBookRepository,
    private val guestBookEntryRepository: GuestBookEntryRepository,
    private val userRepository: UserRepository,
) : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should save then read guest book entry entity`() {
        val address = initAddress(
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
        ).withId()

        val person = initPerson(address = address, surname = "AA").withId()
        val userDto = UserBuilder.new(
            userDto = User(
                person = person,
                emailAddress = "casuel@tantooine.com",
                username = "causual"
            )
        ).userDto

        val savedUser = userRepository.save(UserEntity(userDto))
        var guestBookData = GuestBookBuilder.new(
            GuestBook(
                entries = emptySet(),
                title = "home sweet home",
                user = savedUser.toModel()
            )
        )

        savedUser.guestBook = guestBookData.buildGuestBookEntity()

        val savedGuestBook = guestBookRepository.save(savedUser.guestBook!!)
        guestBookData = guestBookData.withEntry(
            GuestBookEntry(
                guestBook = savedUser.guestBook?.toModel(),
                creatorName = "Harry",
                entry = "Draco Dormiens Nunquam Tittilandus"
            )
        )

        flush { guestBookEntryRepository.save(guestBookData.buildGuestBookEntryEntity()) }

        val entriesByGuestBook = guestBookEntryRepository.findByGuestBook(savedGuestBook)
        assertThat(entriesByGuestBook).hasSize(1)
        val entry = entriesByGuestBook.iterator().next()

        assertAll {
            assertThat(entry.creatorName).isEqualTo("Harry")
            assertThat(entry.entry).isEqualTo("Draco Dormiens Nunquam Tittilandus")
        }
    }

    @Test
    fun `should save then modify and read guest book entry entity`() {
        val address = initAddress(
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
        ).withId()

        val person = initPerson(address = address, surname = "AA").withId()
        val userDto = UserBuilder.new(
            userDto = User(
                person = person,
                emailAddress = "casuel@tantooine.com",
                username = "causual"
            )
        ).userDto

        val savedUser = userRepository.save(UserEntity(userDto))
        val guestBookData = GuestBookBuilder.new(
            GuestBook(
                entries = emptySet(),
                title = "home sweet home",
                user = savedUser.toModel()
            )
        )

        val savedGuestBook = guestBookRepository.save(guestBookData.buildGuestBookEntity())

        flush {
            guestBookEntryRepository.save(
                guestBookData.withEntry(
                    GuestBookEntry(
                        guestBook = savedGuestBook.toModel(),
                        creatorName = "Harry",
                        entry = "Draco Dormiens Nunquam Tittilandus"
                    )
                ).buildGuestBookEntryEntity()
            )
        }

        val entriesByGuestBook = guestBookEntryRepository.findByGuestBook(savedGuestBook)
        assertThat(entriesByGuestBook).hasSize(1)
        entriesByGuestBook.iterator().next().modify("Willie", "On the road again")

        flush { guestBookEntryRepository.save(entriesByGuestBook.iterator().next()) }

        val modifiedEntriesByGuestBook = guestBookEntryRepository.findByGuestBook(savedGuestBook)
        assertThat(modifiedEntriesByGuestBook).hasSize(1)
        val entry = modifiedEntriesByGuestBook.iterator().next()

        assertAll {
            assertThat(entry.creatorName).isEqualTo("Willie")
            assertThat(entry.entry).isEqualTo("On the road again")
        }
    }

    @Test
    fun `should write two entries to two different guest books and then find one entry`() {
        val address = initAddress(
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
        ).withId()

        val person = initPerson(address = address, surname = "AA").withId()
        val userDto = UserBuilder.new(
            userDto = User(
                person = person,
                emailAddress = "casuel@tantooine.com",
                username = "causual"
            )
        ).userDto

        val savedUser = userRepository.save(UserEntity(userDto))
        val guestBookData = GuestBookBuilder.new(
            GuestBook(
                entries = emptySet(),
                title = "home sweet home",
                user = savedUser.toModel()
            )
        )

        val savedGuestBook = guestBookRepository.save(guestBookData.buildGuestBookEntity())
        guestBookEntryRepository.save(
            guestBookData.withEntry(
                GuestBookEntry(
                    guestBook = savedGuestBook.toModel(),
                    creatorName = "somone",
                    entry = "jadda"
                )
            ).buildGuestBookEntryEntity()
        )

        val anotherUserDto = UserBuilder.new(
            userDto = User(
                person = person,
                emailAddress = "hidden@tantooine.com",
                username = "hidden"
            )
        ).userDto

        userRepository.save(UserEntity(anotherUserDto))

        val anotherGuestBookData = GuestBookBuilder.new(
            guestBook = GuestBook(
                entries = emptySet(),
                title = "home sweet home",
                user = savedUser.toModel()
            )
        )

        val anotherSavedGuestBook = guestBookRepository.save(guestBookData.buildGuestBookEntity())
        val anotherEntry = anotherGuestBookData.withEntry(
            guestBookEntry = GuestBookEntry(
                guestBook = anotherSavedGuestBook.toModel(),
                creatorName = "shrek",
                entry = "far far away"
            )
        ).buildGuestBookEntryEntity()

        flush { guestBookEntryRepository.save(anotherEntry) }

        val lastEntry = guestBookRepository.findAll().toList()
            .flatMap { it.getEntries() }
            .firstOrNull { it.id == anotherEntry.id }


        assertAll {
            assertThat(lastEntry).isNotNull()
            assertThat(lastEntry?.creatorName).isEqualTo("shrek")
            assertThat(lastEntry?.entry).isEqualTo("far far away")
        }
    }
}
