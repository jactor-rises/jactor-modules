package com.github.jactor.persistence.service;

import static com.github.jactor.persistence.entity.GuestBookEntity.aGuestBook;
import static com.github.jactor.persistence.entity.GuestBookEntryEntity.aGuestBookEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.jactor.persistence.dto.GuestBookDto;
import com.github.jactor.persistence.dto.GuestBookEntryDto;
import com.github.jactor.persistence.dto.PersistentDto;
import com.github.jactor.persistence.dto.UserInternalDto;
import com.github.jactor.persistence.entity.GuestBookEntity;
import com.github.jactor.persistence.entity.GuestBookEntryEntity;
import com.github.jactor.persistence.repository.GuestBookEntryRepository;
import com.github.jactor.persistence.repository.GuestBookRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("A GuestBookService")
class GuestBookServiceTest {

  @InjectMocks
  private GuestBookService guestBookServiceToTest;
  @Mock
  private GuestBookRepository guestBookRepositoryMock;
  @Mock
  private GuestBookEntryRepository guestBookEntryRepositoryMock;

  @Test
  @DisplayName("should map guest book to a dto")
  void shouldMapBlogToDto() {
    Optional<GuestBookEntity> guestBookEntity = Optional.of(aGuestBook(new GuestBookDto(new PersistentDto(), new HashSet<>(), "@home", null)));
    when(guestBookRepositoryMock.findById(1001L)).thenReturn(guestBookEntity);

    GuestBookDto guestBookDto = guestBookServiceToTest.find(1001L).orElseThrow(mockError());

    assertThat(guestBookDto.getTitle()).as("title").isEqualTo("@home");
  }

  @Test
  @DisplayName("should map guest book entry to a dto")
  void shouldMapFoundBlogToDto() {
    Optional<GuestBookEntryEntity> anEntry = Optional.of(aGuestBookEntry(
        new GuestBookEntryDto(new PersistentDto(), new GuestBookDto(), "me", "too")
    ));

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

  @Test
  @DisplayName("should save GuestBookDto as GuestBookEntity")
  void shouldSaveGuestBookDtoAsGuestBookEntity() {
    GuestBookEntryDto guestBookEntryDto = new GuestBookEntryDto();
    guestBookEntryDto.setGuestBook(new GuestBookDto());
    GuestBookDto guestBookDto = new GuestBookDto();
    guestBookDto.setEntries(Set.of(guestBookEntryDto));
    guestBookDto.setTitle("home sweet home");
    guestBookDto.setUserInternal(new UserInternalDto());

    when(guestBookRepositoryMock.save(any())).thenReturn(new GuestBookEntity(guestBookDto));
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

  @Test
  @DisplayName("should save GuestBookEntryDto as GuestBookEntryEntity")
  void shouldSaveBlogEntryDtoAsBlogEntryEntity() {
    GuestBookEntryDto guestBookEntryDto = new GuestBookEntryDto();
    guestBookEntryDto.setGuestBook(new GuestBookDto());
    guestBookEntryDto.setCreatorName("me");
    guestBookEntryDto.setEntry("if i where a rich man...");

    when(guestBookEntryRepositoryMock.save(any())).thenReturn(new GuestBookEntryEntity(guestBookEntryDto));
    guestBookServiceToTest.saveOrUpdate(guestBookEntryDto);

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
