package com.gitlab.jactor.rises.persistence.repository;

import com.gitlab.jactor.rises.persistence.JactorPersistence;
import com.gitlab.jactor.rises.persistence.entity.guestbook.GuestBookEntity;
import com.gitlab.jactor.rises.persistence.extension.RequiredFieldsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.gitlab.jactor.rises.persistence.entity.guestbook.GuestBookEntity.aGuestBook;
import static com.gitlab.jactor.rises.persistence.entity.user.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@ExtendWith(RequiredFieldsExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@Transactional
@DisplayName("A GuestBookRepository")
class GuestBookRepositoryTest {

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