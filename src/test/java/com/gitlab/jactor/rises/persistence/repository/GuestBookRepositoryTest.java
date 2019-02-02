package com.gitlab.jactor.rises.persistence.repository;

import com.gitlab.jactor.rises.persistence.JactorPersistence;
import com.gitlab.jactor.rises.persistence.entity.address.AddressEntity;
import com.gitlab.jactor.rises.persistence.entity.guestbook.GuestBookEntity;
import com.gitlab.jactor.rises.persistence.entity.person.PersonEntity;
import com.gitlab.jactor.rises.persistence.entity.user.UserEntity;
import com.gitlab.jactor.rises.test.extension.validate.fields.FieldValue;
import com.gitlab.jactor.rises.test.extension.validate.fields.RequiredFieldsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.Map;

import static com.gitlab.jactor.rises.persistence.entity.address.AddressEntity.anAddress;
import static com.gitlab.jactor.rises.persistence.entity.guestbook.GuestBookEntity.aGuestBook;
import static com.gitlab.jactor.rises.persistence.entity.person.PersonEntity.aPerson;
import static com.gitlab.jactor.rises.persistence.entity.user.UserEntity.aUser;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@Transactional
@DisplayName("A GuestBookRepository")
class GuestBookRepositoryTest {

    @RegisterExtension RequiredFieldsExtension requiredFieldsExtension = new RequiredFieldsExtension(Map.of(
            UserEntity.class, asList(
                    new FieldValue("username", () -> "unique@" + LocalDateTime.now()),
                    new FieldValue("personEntity", () -> aPerson().build())
            ), PersonEntity.class, asList(
                    new FieldValue("addressEntity", () -> anAddress().build()),
                    new FieldValue("surname", "sure, man")
            ), AddressEntity.class, asList(
                    new FieldValue("addressLine1", "Test Boulevard 1"),
                    new FieldValue("zipCode", 1001),
                    new FieldValue("city", "Testing")
            )
    ));

    private @Autowired GuestBookRepository guestBookRepository;
    private @Autowired EntityManager entityManager;

    @DisplayName("should write then read guest book")
    @Test void shouldWriteThenReadGuestBook() {
        GuestBookEntity guestBookEntityToSave = aGuestBook()
                .withTitle("home sweet home")
                .with(aUser())
                .build();

        guestBookRepository.save(guestBookEntityToSave);
        entityManager.flush();
        entityManager.clear();

        GuestBookEntity guestBookEntity = guestBookRepository.findById(guestBookEntityToSave.getId()).orElseThrow(this::guestBookNotFound);

        assertAll(
                () -> assertThat(guestBookEntity.getTitle()).as("title").isEqualTo("home sweet home"),
                () -> assertThat(guestBookEntity.getUser()).as("user").isNotNull()
        );
    }

    @DisplayName("should write then update and read guest book")
    @Test void shouldWriteThenUpdateAndReadGuestBook() {
        GuestBookEntity guestBookEntityToSave = aGuestBook()
                .withTitle("home sweet home")
                .with(aUser())
                .build();

        guestBookRepository.save(guestBookEntityToSave);
        entityManager.flush();
        entityManager.clear();

        GuestBookEntity guestBookEntityToUpdate = guestBookRepository.findById(guestBookEntityToSave.getId()).orElseThrow(this::guestBookNotFound);

        guestBookEntityToUpdate.setTitle("5000 thousands miles away from home");

        guestBookRepository.save(guestBookEntityToUpdate);
        entityManager.flush();
        entityManager.clear();

        GuestBookEntity guestBookEntity = guestBookRepository.findById(guestBookEntityToSave.getId()).orElseThrow(this::guestBookNotFound);

        assertThat(guestBookEntity.getTitle()).isEqualTo("5000 thousands miles away from home");
    }

    private AssertionError guestBookNotFound() {
        return new AssertionError("Guest book not found");
    }
}