package com.github.jactor.rises.persistence.repository;

import com.github.jactor.rises.persistence.JactorPersistence;
import com.github.jactor.rises.persistence.entity.address.AddressEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.github.jactor.rises.persistence.entity.address.AddressEntity.anAddress;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@Transactional
@DisplayName("An AddressRepository")
class AddressRepositoryTest {

    @Autowired private AddressRepository addressRepository;

    @DisplayName("should write then read address entities")
    @Test void shouldWriteThenWriteAddressEntities() {
        AddressEntity addressEntityToPersist = anAddress()
                .withAddressLine1("somewhere out there")
                .withZipCode(1234)
                .withCity("Rud")
                .build();

        addressRepository.save(addressEntityToPersist);
        List<AddressEntity> addressEntities = addressRepository.findByZipCode(1234);

        assertAll(
                () -> assertThat(addressEntities).isNotEmpty(),
                () -> {
                    AddressEntity addressEntity = addressEntities.get(0);
                    assertAll(
                            () -> assertThat(addressEntity.getAddressLine1()).as("address line 1").isEqualTo("somewhere out there"),
                            () -> assertThat(addressEntity.getZipCode()).as("zip code").isEqualTo(1234),
                            () -> assertThat(addressEntity.getCity()).as("city").isEqualTo("Rud")
                    );
                }
        );
    }

    @DisplayName("should read and write an address entity")
    @Test void shouldReadAndWriteAnAddressEntity() {
        AddressEntity addressEntityToPersist = anAddress()
                .withAddressLine1("somewhere out there")
                .withZipCode(1234)
                .withCity("Rud")
                .build();

        addressRepository.save(addressEntityToPersist);
        Optional<AddressEntity> addressEntityById = addressRepository.findById(addressEntityToPersist.getId());

        assertAll(
                () -> assertThat(addressEntityById).isPresent(),
                () -> {
                    AddressEntity addressEntity = addressEntityById.orElse(anAddress().build());
                    assertAll(
                            () -> assertThat(addressEntity.getAddressLine1()).as("address line 1").isEqualTo("somewhere out there"),
                            () -> assertThat(addressEntity.getZipCode()).as("zip code").isEqualTo(1234),
                            () -> assertThat(addressEntity.getCity()).as("city").isEqualTo("Rud")
                    );
                }
        );
    }
}
