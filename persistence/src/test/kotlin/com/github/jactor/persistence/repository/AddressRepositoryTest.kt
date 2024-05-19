package com.github.jactor.persistence.repository

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import com.github.jactor.persistence.JactorPersistence
import com.github.jactor.persistence.dto.AddressInternalDto
import com.github.jactor.persistence.entity.AddressBuilder
import com.github.jactor.persistence.entity.AddressEntity
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isIn
import assertk.assertions.isPresent
import jakarta.persistence.EntityManager

@SpringBootTest(classes = [JactorPersistence::class])
@Transactional
internal class AddressRepositoryTest {

    @Autowired
    private lateinit var addressRepository: AddressRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @Test
    fun `should fetch address entities`() {
        addressRepository.save(
            AddressBuilder
                .new(
                    addressInternalDto = AddressInternalDto(
                        zipCode = "1234",
                        addressLine1 = "somewhere out there",
                        city = "Rud"
                    )
                )
                .build()
        )

        addressRepository.save(
            AddressBuilder
                .new(
                    addressInternalDto = AddressInternalDto(
                        zipCode = "1234",
                        addressLine1 = "somewhere in there",
                        city = "Rud"
                    )
                )
                .build()
        )

        entityManager.flush()
        entityManager.clear()

        val addressEntities = addressRepository.findByZipCode(zipCode = "1234")

        assertAll {
            assertThat(addressEntities).hasSize(2)
            addressEntities.forEach {
                assertThat(it.addressLine1).isIn("somewhere out there", "somewhere in there")
            }
        }
    }

    @Test
    fun `should write then read an address entity`() {
        val addressEntityToPersist = AddressBuilder
            .new(
                addressInternalDto = AddressInternalDto(
                    zipCode = "1234",
                    addressLine1 = "somewhere out there",
                    addressLine2 = "where the streets have no name",
                    addressLine3 = "in the middle of it",
                    city = "Rud",
                    country = "NO"
                )
            ).build()

        addressRepository.save(addressEntityToPersist)
        entityManager.flush()
        entityManager.clear()

        val possibleAddressEntityById = addressRepository.findById(addressEntityToPersist.id!!)

        assertThat(possibleAddressEntityById).isPresent().given { addressEntity: AddressEntity ->
            assertAll {
                assertThat(addressEntity.addressLine1).isEqualTo("somewhere out there")
                assertThat(addressEntity.addressLine2).isEqualTo("where the streets have no name")
                assertThat(addressEntity.addressLine3).isEqualTo("in the middle of it")
                assertThat(addressEntity.zipCode).isEqualTo("1234")
                assertThat(addressEntity.country).isEqualTo("NO")
                assertThat(addressEntity.city).isEqualTo("Rud")
            }
        }
    }

    @Test
    fun `should write then update and read an address entity`() {
        val addressEntityToPersist = AddressBuilder
            .new(
                addressInternalDto = AddressInternalDto(
                    zipCode = "1234",
                    addressLine1 = "somewhere out there",
                    addressLine2 = "where the streets have no name",
                    addressLine3 = "in the middle of it",
                    city = "Rud",
                    country = "NO"
                )
            )
            .build()

        addressRepository.save(addressEntityToPersist)
        entityManager.flush()
        entityManager.clear()

        val addressEntitySaved =
            addressRepository.findById(addressEntityToPersist.id!!).orElseThrow { addressNotFound() }!!
        addressEntitySaved.addressLine1 = "the truth is out there"
        addressEntitySaved.addressLine2 = "among the stars"
        addressEntitySaved.addressLine3 = "there will be life"
        addressEntitySaved.zipCode = "666"
        addressEntitySaved.city = "Cloud city"
        addressEntitySaved.country = "XX"

        addressRepository.save(addressEntitySaved)
        entityManager.flush()
        entityManager.clear()

        val possibleAddressEntityById = addressRepository.findById(addressEntityToPersist.id!!)

        assertThat(possibleAddressEntityById).isPresent().given { addressEntity: AddressEntity ->
            assertAll {
                assertThat(addressEntity.addressLine1).isEqualTo("the truth is out there")
                assertThat(addressEntity.addressLine2).isEqualTo("among the stars")
                assertThat(addressEntity.addressLine3).isEqualTo("there will be life")
                assertThat(addressEntity.zipCode).isEqualTo("666")
                assertThat(addressEntity.country).isEqualTo("XX")
                assertThat(addressEntity.city).isEqualTo("Cloud city")
            }
        }
    }

    private fun addressNotFound(): AssertionError {
        return AssertionError("address not found")
    }
}
