package com.github.jactor.persistence.repository;

import static com.github.jactor.persistence.entity.AddressEntity.anAddress;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.github.jactor.persistence.JactorPersistence;
import com.github.jactor.persistence.dto.AddressInternalDto;
import com.github.jactor.persistence.dto.PersistentDto;
import com.github.jactor.persistence.entity.AddressEntity;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@Transactional
@DisplayName("An AddressRepository")
class AddressRepositoryTest {

  @Autowired
  private AddressRepository addressRepository;
  @Autowired
  private EntityManager entityManager;

  @Test
  @DisplayName("should fetch address entities")
  void shouldFetchAddressEntities() {
    addressRepository.save(anAddress(
        new AddressInternalDto(new PersistentDto(), "1234", "somewhere out there", null, null, "Rud", null)
    ));

    addressRepository.save(anAddress(
        new AddressInternalDto(new PersistentDto(), "1234", "somewhere in there", null, null, "Rud", null)
    ));

    entityManager.flush();
    entityManager.clear();

    var addressEntities = addressRepository.findByZipCode("1234");

    assertAll(
        () -> assertThat(addressEntities).hasSize(2),
        () -> {
          for (AddressEntity addressEntity : addressEntities) {
            assertThat(addressEntity.getAddressLine1()).as("address line 1").isIn("somewhere out there", "somewhere in there");
          }
        }
    );
  }

  @Test
  @DisplayName("should write then read an address entity")
  void shouldWriteThenReadAnAddressEntity() {
    var addressEntityToPersist = anAddress(
        new AddressInternalDto(
            new PersistentDto(),
            "1234",
            "somewhere out there",
            "where the streets have no name",
            "in the middle of it",
            "Rud",
            "NO"
        )
    );

    addressRepository.save(addressEntityToPersist);
    entityManager.flush();
    entityManager.clear();

    var possibleAddressEntityById = addressRepository.findById(addressEntityToPersist.getId());

    assertAll(
        () -> assertThat(possibleAddressEntityById).isPresent(),
        () -> {
          AddressEntity addressEntity = possibleAddressEntityById.orElseThrow(this::addressNotFound);
          assertAll(
              () -> assertThat(addressEntity.getAddressLine1()).as("address line 1").isEqualTo("somewhere out there"),
              () -> assertThat(addressEntity.getAddressLine2()).as("address line 2").isEqualTo("where the streets have no name"),
              () -> assertThat(addressEntity.getAddressLine3()).as("address line 3").isEqualTo("in the middle of it"),
              () -> assertThat(addressEntity.getZipCode()).as("zip code").isEqualTo("1234"),
              () -> assertThat(addressEntity.getCountry()).as("country").isEqualTo("NO"),
              () -> assertThat(addressEntity.getCity()).as("city").isEqualTo("Rud")
          );
        }
    );
  }

  @Test
  @DisplayName("should write then update and read an address entity")
  void shouldWriteThenUpdateAndReadAnAddressEntity() {
    var addressEntityToPersist = anAddress(
        new AddressInternalDto(
            new PersistentDto(),
            "1234",
            "somewhere out there",
            "where the streets have no name",
            "in the middle of it",
            "Rud",
            "NO"
        )
    );

    addressRepository.save(addressEntityToPersist);
    entityManager.flush();
    entityManager.clear();

    var addressEntitySaved = addressRepository.findById(addressEntityToPersist.getId()).orElseThrow(this::addressNotFound);

    addressEntitySaved.setAddressLine1("the truth is out there");
    addressEntitySaved.setAddressLine2("among the stars");
    addressEntitySaved.setAddressLine3("there will be life");
    addressEntitySaved.setZipCode("666");
    addressEntitySaved.setCity("Cloud city");
    addressEntitySaved.setCountry("XX");

    addressRepository.save(addressEntitySaved);
    entityManager.flush();
    entityManager.clear();

    var possibleAddressEntityById = addressRepository.findById(addressEntityToPersist.getId());

    assertAll(
        () -> assertThat(possibleAddressEntityById).isPresent(),
        () -> {
          AddressEntity addressEntity = possibleAddressEntityById.orElseThrow(this::addressNotFound);
          assertAll(
              () -> assertThat(addressEntity.getAddressLine1()).as("address line 1").isEqualTo("the truth is out there"),
              () -> assertThat(addressEntity.getAddressLine2()).as("address line 2").isEqualTo("among the stars"),
              () -> assertThat(addressEntity.getAddressLine3()).as("address line 3").isEqualTo("there will be life"),
              () -> assertThat(addressEntity.getZipCode()).as("zip code").isEqualTo("666"),
              () -> assertThat(addressEntity.getCountry()).as("country").isEqualTo("XX"),
              () -> assertThat(addressEntity.getCity()).as("city").isEqualTo("Cloud city")
          );
        }
    );
  }

  private AssertionError addressNotFound() {
    return new AssertionError("address not found");
  }
}
