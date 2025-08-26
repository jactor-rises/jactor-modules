package com.github.jactor.persistence

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.person.PersonBuilder
import com.github.jactor.persistence.person.PersonModel
import com.github.jactor.persistence.user.UserBuilder
import com.github.jactor.persistence.user.UserEntity
import com.github.jactor.persistence.user.UserModel
import com.github.jactor.persistence.user.UserRepository
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull

internal class GuestBookEntryRepositoryTest : AbstractSpringBootNoDirtyContextTest() {
    @Autowired
    private lateinit var guestBookRepository: GuestBookRepository

    @Autowired
    private lateinit var guestBookEntryRepository: GuestBookEntryRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `should save then read guest book entry entity`() {
        val addressDto = AddressBuilder.new(
            addressModel = AddressModel(
                zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
            )
        ).addressModel

        val personDto = PersonBuilder.new(personModel = PersonModel(address = addressDto, surname = "AA"))
            .personModel

        val userDto = UserBuilder.new(
            userDto = UserModel(
                person = personDto,
                emailAddress = "casuel@tantooine.com",
                username = "causual"
            )
        ).userDto

        val savedUser = userRepository.save(UserEntity(userDto))
        var guestBookData = GuestBookBuilder.new(
            GuestBookModel(
                entries = emptySet(),
                title = "home sweet home",
                user = savedUser.toModel()
            )
        )

        savedUser.setGuestBook(guestBookData.buildGuestBookEntity())

        val savedGuestBook = guestBookRepository.save(savedUser.guestBook!!)
        guestBookData = guestBookData.withEntry(
            GuestBookEntryModel(
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
        val addressDto = AddressBuilder.new(
            addressModel = AddressModel(
                zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
            )
        ).addressModel

        val personDto = PersonBuilder.new(personModel = PersonModel(address = addressDto, surname = "AA"))
            .personModel

        val userDto = UserBuilder.new(
            userDto = UserModel(
                person = personDto,
                emailAddress = "casuel@tantooine.com",
                username = "causual"
            )
        ).userDto

        val savedUser = userRepository.save(UserEntity(userDto))
        val guestBookData = GuestBookBuilder.new(
            GuestBookModel(
                entries = emptySet(),
                title = "home sweet home",
                user = savedUser.toModel()
            )
        )

        val savedGuestBook = guestBookRepository.save(guestBookData.buildGuestBookEntity())

        flush {
            guestBookEntryRepository.save(
                guestBookData.withEntry(
                    GuestBookEntryModel(
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
        val addressDto = AddressBuilder.new(
            addressModel = AddressModel(
                zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testington"
            )
        ).addressModel

        val personDto = PersonBuilder.new(personModel = PersonModel(address = addressDto, surname = "AA"))
            .personModel

        val userDto = UserBuilder.new(
            userDto = UserModel(
                person = personDto,
                emailAddress = "casuel@tantooine.com",
                username = "causual"
            )
        ).userDto

        val savedUser = userRepository.save(UserEntity(userDto))
        val guestBookData = GuestBookBuilder.new(
            GuestBookModel(
                entries = emptySet(),
                title = "home sweet home",
                user = savedUser.toModel()
            )
        )

        val savedGuestBook = guestBookRepository.save(guestBookData.buildGuestBookEntity())
        guestBookEntryRepository.save(
            guestBookData.withEntry(
                GuestBookEntryModel(
                    guestBook = savedGuestBook.toModel(),
                    creatorName = "somone",
                    entry = "jadda"
                )
            ).buildGuestBookEntryEntity()
        )

        val anotherUserDto = UserBuilder.new(
            userDto = UserModel(
                person = personDto,
                emailAddress = "hidden@tantooine.com",
                username = "hidden"
            )
        ).userDto

        userRepository.save(UserEntity(anotherUserDto))

        val anotherGuestBookData = GuestBookBuilder.new(
            guestBookModel = GuestBookModel(
                entries = emptySet(),
                title = "home sweet home",
                user = savedUser.toModel()
            )
        )

        val anotherSavedGuestBook = guestBookRepository.save(guestBookData.buildGuestBookEntity())
        val anotherEntry = anotherGuestBookData.withEntry(
            guestBookEntryModel = GuestBookEntryModel(
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
