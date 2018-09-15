package com.gitlab.jactor.rises.persistence.repository;

import com.gitlab.jactor.rises.persistence.JactorPersistence;
import com.gitlab.jactor.rises.persistence.entity.address.AddressEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.gitlab.jactor.rises.persistence.entity.address.AddressEntity.anAddress;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@Transactional
@DisplayName("An AddressRepository")
class AddressRepositoryTest {

    private @Autowired AddressRepository addressRepository;
    private @Autowired EntityManager entityManager;

    @DisplayName("should fetch address entities")
    @Test void shouldFetchAddressEntities() {
        addressRepository.save(anAddress()
                .withAddressLine1("somewhere out there")
                .withZipCode(1234)
                .withCity("Rud")
                .build()
        );

        addressRepository.save(anAddress()
                .withAddressLine1("somewhere in there")
                .withZipCode(1234)
                .withCity("Rud")
                .build()
        );

        entityManager.flush();
        entityManager.clear();

        List<AddressEntity> addressEntities = addressRepository.findByZipCode(1234);

        assertAll(
                () -> assertThat(addressEntities).hasSize(2),
                () -> {
                    for (AddressEntity addressEntity : addressEntities) {
                        assertThat(addressEntity.getAddressLine1()).as("address line 1").isIn("somewhere out there", "somewhere in there");
                    }
                }
        );
    }

    @DisplayName("should write then read an address entity")
    @Test void shouldWriteThenReadAnAddressEntity() {
        AddressEntity addressEntityToPersist = anAddress()
                .withAddressLine1("somewhere out there")
                .withAddressLine2("where the streets have no name")
                .withAddressLine3("in the middle of it")
                .withZipCode(1234)
                .withCountryCode("NO")
                .withCity("Rud")
                .build();

        addressRepository.save(addressEntityToPersist);
        entityManager.flush();
        entityManager.clear();

        Optional<AddressEntity> addressEntityById = addressRepository.findById(addressEntityToPersist.getId());

        assertAll(
                () -> assertThat(addressEntityById).isPresent(),
                () -> {
                    AddressEntity addressEntity = addressEntityById.orElseThrow(this::addressNotFound);
                    assertAll(
                            () -> assertThat(addressEntity.getAddressLine1()).as("address line 1").isEqualTo("somewhere out there"),
                            () -> assertThat(addressEntity.getAddressLine2()).as("address line 2").isEqualTo("where the streets have no name"),
                            () -> assertThat(addressEntity.getAddressLine3()).as("address line 3").isEqualTo("in the middle of it"),
                            () -> assertThat(addressEntity.getZipCode()).as("zip code").isEqualTo(1234),
                            () -> assertThat(addressEntity.getCountry()).as("country").isEqualTo("NO"),
                            () -> assertThat(addressEntity.getCity()).as("city").isEqualTo("Rud")
                    );
                }
        );
    }

    @DisplayName("should write then update and read an address entity")
    @Test void shouldWriteThenUpdateAndReadAnAddressEntity() {
        AddressEntity addressEntityToPersist = anAddress()
                .withAddressLine1("somewhere out there")
                .withAddressLine2("where the streets have no name")
                .withAddressLine3("in the middle of it")
                .withZipCode(1234)
                .withCountryCode("NO")
                .withCity("Rud")
                .build();

        addressRepository.save(addressEntityToPersist);
        entityManager.flush();
        entityManager.clear();

        AddressEntity addressEntitySaved = addressRepository.findById(addressEntityToPersist.getId()).orElseThrow(this::addressNotFound);


        addressEntitySaved.setAddressLine1("the truth is out there");
        addressEntitySaved.setAddressLine2("among the stars");
        addressEntitySaved.setAddressLine3("there will be life");
        addressEntitySaved.setZipCode(666);
        addressEntitySaved.setCity("Cloud city");
        addressEntitySaved.setCountry("XX");

        addressRepository.save(addressEntitySaved);
        entityManager.flush();
        entityManager.clear();

        Optional<AddressEntity> addressEntityById = addressRepository.findById(addressEntityToPersist.getId());

        assertAll(
                () -> assertThat(addressEntityById).isPresent(),
                () -> {
                    AddressEntity addressEntity = addressEntityById.orElseThrow(this::addressNotFound);
                    assertAll(
                            () -> assertThat(addressEntity.getAddressLine1()).as("address line 1").isEqualTo("the truth is out there"),
                            () -> assertThat(addressEntity.getAddressLine2()).as("address line 2").isEqualTo("among the stars"),
                            () -> assertThat(addressEntity.getAddressLine3()).as("address line 3").isEqualTo("there will be life"),
                            () -> assertThat(addressEntity.getZipCode()).as("zip code").isEqualTo(666),
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
