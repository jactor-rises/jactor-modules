package com.gitlab.jactor.rises.persistence.service;

import com.gitlab.jactor.rises.commons.dto.GuestBookDto;
import com.gitlab.jactor.rises.commons.dto.GuestBookEntryDto;
import com.gitlab.jactor.rises.commons.dto.UserDto;
import com.gitlab.jactor.rises.persistence.entity.guestbook.GuestBookEntity;
import com.gitlab.jactor.rises.persistence.entity.guestbook.GuestBookEntryEntity;
import com.gitlab.jactor.rises.persistence.extension.RequiredFieldsExtension;
import com.gitlab.jactor.rises.persistence.repository.GuestBookEntryRepository;
import com.gitlab.jactor.rises.persistence.repository.GuestBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Supplier;

import static com.gitlab.jactor.rises.persistence.entity.guestbook.GuestBookEntity.aGuestBook;
import static com.gitlab.jactor.rises.persistence.entity.guestbook.GuestBookEntryEntity.aGuestBookEntry;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("A GuestBookService")
@ExtendWith(RequiredFieldsExtension.class)
class GuestBookServiceTest {

    @InjectMocks private GuestBookService guestBookServiceToTest;
    @Mock private GuestBookRepository guestBookRepositoryMock;
    @Mock private GuestBookEntryRepository guestBookEntryRepositoryMock;

    @BeforeEach void initMocking() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("should map guest book to a dto")
    @Test void shouldMapBlogToDto() {
        Optional<GuestBookEntity> guestBookEntity = Optional.of(aGuestBook().withTitle("@home").build());
        when(guestBookRepositoryMock.findById(1001L)).thenReturn(guestBookEntity);

        GuestBookDto guestBookDto = guestBookServiceToTest.find(1001L).orElseThrow(mockError());

        assertThat(guestBookDto.getTitle()).as("title").isEqualTo("@home");
    }

    @DisplayName("should map guest book entry to a dto")
    @Test void shouldMapFoundBlogToDto() {
        Optional<GuestBookEntryEntity> anEntry = Optional.of(aGuestBookEntry().withCreatorName("me").withEntry("too").build());
        when(guestBookEntryRepositoryMock.findById(1001L)).thenReturn(anEntry);

        GuestBookEntryDto guestBookEntryDto = guestBookServiceToTest.findEntry(1001L).orElseThrow(mockError());

        assertAll(
                () -> assertThat(guestBookEntryDto.getCreatorName()).as("creator name").isEqualTo("me"),
                () -> assertThat(guestBookEntryDto.getEntry()).as("entry").isEqualTo("too")
        );
    }

    private Supplier<AssertionError> mockError() {
        return () -> new AssertionError("missed mocking?");
    }

    @DisplayName("should save GuestBookDto as GuestBookEntity")
    @Test void shouldSaveGuestBookDtoAsGuestBookEntity() {
        GuestBookDto guestBookDto = new GuestBookDto();
        guestBookDto.setEntries(new HashSet<>(Collections.singletonList(new GuestBookEntryDto())));
        guestBookDto.setTitle("home sweet home");
        guestBookDto.setUser(new UserDto());

        guestBookServiceToTest.saveOrUpdate(guestBookDto);

        ArgumentCaptor<GuestBookEntity> argCaptor = ArgumentCaptor.forClass(GuestBookEntity.class);
        verify(guestBookRepositoryMock).save(argCaptor.capture());
        GuestBookEntity guestBookEntity = argCaptor.getValue();

        assertAll(
                () -> assertThat(guestBookEntity.getEntries()).as("entries").hasSize(1),
                () -> assertThat(guestBookEntity.getTitle()).as("title").isEqualTo("home sweet home"),
                () -> assertThat(guestBookEntity.getUser()).as("user").isNotNull()
        );
    }

    @DisplayName("should save GuestBookEntryDto as GuestBookEntryEntity")
    @Test void shouldSaveBlogEntryDtoAsBlogEntryEntity() {
        GuestBookEntryDto blogEntryDto = new GuestBookEntryDto();
        blogEntryDto.setGuestBook(new GuestBookDto());
        blogEntryDto.setCreatorName("me");
        blogEntryDto.setEntry("if i where a rich man...");

        guestBookServiceToTest.saveOrUpdate(blogEntryDto);

        ArgumentCaptor<GuestBookEntryEntity> argCaptor = ArgumentCaptor.forClass(GuestBookEntryEntity.class);
        verify(guestBookEntryRepositoryMock).save(argCaptor.capture());
        GuestBookEntryEntity guestBookEntryEntity = argCaptor.getValue();


        assertAll(
                () -> assertThat(guestBookEntryEntity.getGuestBook()).as("guest book").isNotNull(),
                () -> assertThat(guestBookEntryEntity.getCreatorName()).as("creator name").isEqualTo("me"),
                () -> assertThat(guestBookEntryEntity.getEntry()).as("entry").isEqualTo("if i where a rich man...")
        );
    }
}
