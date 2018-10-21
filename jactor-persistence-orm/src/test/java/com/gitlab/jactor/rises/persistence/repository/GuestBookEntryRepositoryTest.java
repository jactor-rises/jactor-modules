package com.gitlab.jactor.rises.persistence.repository;

import com.gitlab.jactor.rises.persistence.JactorPersistence;
import com.gitlab.jactor.rises.persistence.entity.address.AddressEntity;
import com.gitlab.jactor.rises.persistence.entity.guestbook.GuestBookEntity;
import com.gitlab.jactor.rises.persistence.entity.guestbook.GuestBookEntryEntity;
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
import java.util.List;
import java.util.Map;

import static com.gitlab.jactor.rises.persistence.entity.address.AddressEntity.anAddress;
import static com.gitlab.jactor.rises.persistence.entity.guestbook.GuestBookEntity.aGuestBook;
import static com.gitlab.jactor.rises.persistence.entity.guestbook.GuestBookEntryEntity.aGuestBookEntry;
import static com.gitlab.jactor.rises.persistence.entity.person.PersonEntity.aPerson;
import static com.gitlab.jactor.rises.persistence.entity.user.UserEntity.aUser;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@Transactional
@DisplayName("A GuestBookEntryRepository")
class GuestBookEntryRepositoryTest {

    @RegisterExtension RequiredFieldsExtension requiredFieldsExtension = new RequiredFieldsExtension(Map.of(
            GuestBookEntity.class, asList(
                    new FieldValue("title", "my guest book"),
                    new FieldValue("user", () -> aUser().build())
            ), UserEntity.class, asList(
                    new FieldValue("username", () -> "uniqueName@" + LocalDateTime.now()),
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

    private @Autowired GuestBookEntryRepository guestBookEntryRepository;
    private @Autowired GuestBookRepository guestBookRepository;
    private @Autowired EntityManager entityManager;

    @DisplayName("should save then read guest book entry entity")
    @Test void shouldSaveThenReadGuestBookEntryEntity() {
        GuestBookEntryEntity guestBookEntryEntityToSave = aGuestBookEntry()
                .with(aGuestBook())
                .withCreatorName("Harry")
                .withEntry("Draco Dormiens Nunquam Tittilandus")
                .build();

        guestBookEntryRepository.save(guestBookEntryEntityToSave);
        entityManager.flush();
        entityManager.clear();

        GuestBookEntryEntity entryById = guestBookEntryRepository.findById(guestBookEntryEntityToSave.getId())
                .orElseThrow(this::entryNotFound);

        assertAll(
                () -> assertThat(entryById.getCreatorName()).as("creator name").isEqualTo("Harry"),
                () -> assertThat(entryById.getEntry()).as("entry").isEqualTo("Draco Dormiens Nunquam Tittilandus")
        );
    }

    @DisplayName("should save then update and read guest book entry entity")
    @Test void shouldSaveThenUpdateAndReadGuestBookEntryEntity() {
        GuestBookEntryEntity guestBookEntryEntityToSave = aGuestBookEntry()
                .with(aGuestBook())
                .withCreatorName("Harry")
                .withEntry("Draco Dormiens Nunquam Tittilandus")
                .build();

        guestBookEntryRepository.save(guestBookEntryEntityToSave);
        entityManager.flush();
        entityManager.clear();

        GuestBookEntryEntity entryById = guestBookEntryRepository.findById(guestBookEntryEntityToSave.getId())
                .orElseThrow(this::entryNotFound);

        entryById.setCreatorName("Willie");
        entryById.update("On the road again");

        assertAll(
                () -> assertThat(entryById.getCreatorName()).as("creator name").isEqualTo("Willie"),
                () -> assertThat(entryById.getEntry()).as("entry").isEqualTo("On the road again")
        );
    }

    @DisplayName("should write two entries and on two blogs then find entry on blog")
    @Test void shouldWriteTwoEntriesOnTwoBlogsThenFindEntryOnBlog() {
        guestBookEntryRepository.save(
                aGuestBookEntry()
                        .with(aGuestBook())
                        .withCreatorName("someone")
                        .withEntry("jadda")
                        .build()
        );

        GuestBookEntity theGuestBook = aGuestBook().build();
        guestBookRepository.save(theGuestBook);
        entityManager.flush();
        entityManager.clear();

        GuestBookEntryEntity guestBookEntryToSave = aGuestBookEntry()
                .with(theGuestBook)
                .withCreatorName("shrek")
                .withEntry("far far away")
                .build();

        guestBookEntryRepository.save(guestBookEntryToSave);
        entityManager.flush();
        entityManager.clear();

        List<GuestBookEntryEntity> entriesByGuestBook = guestBookEntryRepository.findByGuestBook(theGuestBook);

        assertAll(
                () -> assertThat(guestBookEntryRepository.findAll()).as("all entries").hasSize(2),
                () -> assertThat(entriesByGuestBook).as("entriesByGuestBook").hasSize(1),
                () -> assertThat(entriesByGuestBook.get(0).getCreatorName()).as("entry.creatorName").isEqualTo("shrek"),
                () -> assertThat(entriesByGuestBook.get(0).getEntry()).as("entry.entry").isEqualTo("far far away")
        );
    }

    private AssertionError entryNotFound() {
        return new AssertionError("guest book entry not found");
    }
}