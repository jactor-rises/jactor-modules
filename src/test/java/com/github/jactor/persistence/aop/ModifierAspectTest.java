package com.github.jactor.persistence.aop;

import static com.github.jactor.persistence.entity.AddressEntity.anAddress;
import static com.github.jactor.persistence.entity.BlogEntity.aBlog;
import static com.github.jactor.persistence.entity.BlogEntryEntity.aBlogEntry;
import static com.github.jactor.persistence.entity.GuestBookEntity.aGuestBook;
import static com.github.jactor.persistence.entity.GuestBookEntryEntity.aGuestBookEntry;
import static com.github.jactor.persistence.entity.PersonEntity.aPerson;
import static com.github.jactor.persistence.entity.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.github.jactor.persistence.dto.AddressDto;
import com.github.jactor.persistence.dto.BlogDto;
import com.github.jactor.persistence.dto.BlogEntryDto;
import com.github.jactor.persistence.dto.GuestBookDto;
import com.github.jactor.persistence.dto.GuestBookEntryDto;
import com.github.jactor.persistence.dto.PersistentDto;
import com.github.jactor.persistence.dto.PersonInternalDto;
import com.github.jactor.persistence.dto.UserInternalDto;
import java.time.LocalDateTime;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ModifierAspect")
class ModifierAspectTest {

  private LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
  private ModifierAspect modifierAspect = new ModifierAspect();
  private PersistentDto persistentDto = new PersistentDto(null, "na", oneMinuteAgo, "na", oneMinuteAgo);

  @Mock
  private JoinPoint joinPointMock;


  @Test
  @DisplayName("should modify timestamp on address when used")
  void shouldModifyTimestampOnAddressWhenUsed() {
    var addressWithoutId = anAddress(new AddressDto(persistentDto, new AddressDto()));
    var address = anAddress(new AddressDto(persistentDto, new AddressDto()));
    address.setId(1L);

    when(joinPointMock.getArgs()).thenReturn(new Object[]{address, addressWithoutId});
    modifierAspect.modifyPersistentEntity(joinPointMock);

    assertThat(address.getTimeOfModification()).isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now());
    assertThat(addressWithoutId.getTimeOfModification()).isEqualTo(oneMinuteAgo);
  }

  @Test
  @DisplayName("should modify timestamp on blog when used")
  void shouldModifyTimestampOnBlogWhenUsed() {
    var blogWithouId = aBlog(new BlogDto(persistentDto, new BlogDto()));
    var blog = aBlog(new BlogDto(persistentDto, new BlogDto()));
    blog.setId(1L);

    when(joinPointMock.getArgs()).thenReturn(new Object[]{blog, blogWithouId});
    modifierAspect.modifyPersistentEntity(joinPointMock);

    assertThat(blog.getTimeOfModification()).isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now());
    assertThat(blogWithouId.getTimeOfModification()).isEqualTo(oneMinuteAgo);
  }

  @Test
  @DisplayName("should modify timestamp on blogEntry when used")
  void shouldModifyTimestampOnBlogEntryWhenUsed() {
    var blogEntryWithoutId = aBlogEntry(new BlogEntryDto(persistentDto, new BlogEntryDto()));
    var blogEntry = aBlogEntry(new BlogEntryDto(persistentDto, new BlogEntryDto()));
    blogEntry.setId(1L);

    when(joinPointMock.getArgs()).thenReturn(new Object[]{blogEntry, blogEntryWithoutId});
    modifierAspect.modifyPersistentEntity(joinPointMock);

    assertThat(blogEntry.getTimeOfModification()).isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now());
    assertThat(blogEntryWithoutId.getTimeOfModification()).isEqualTo(oneMinuteAgo);
  }

  @Test
  @DisplayName("should modify timestamp on guestBook when used")
  void shouldModifyTimestampOnGuestBookWhenUsed() {
    var guestBookWithoutId = aGuestBook(new GuestBookDto(persistentDto, new GuestBookDto()));
    var guestBook = aGuestBook(new GuestBookDto(persistentDto, new GuestBookDto()));
    guestBook.setId(1L);

    when(joinPointMock.getArgs()).thenReturn(new Object[]{guestBook, guestBookWithoutId});
    modifierAspect.modifyPersistentEntity(joinPointMock);

    assertThat(guestBook.getTimeOfModification()).isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now());
    assertThat(guestBookWithoutId.getTimeOfModification()).isEqualTo(oneMinuteAgo);
  }

  @Test
  @DisplayName("should modify timestamp on guestBookEntry when used")
  void shouldModifyTimestampOnGuestBookEntryWhenUsed() {
    var guestBookEntryWithoutId = aGuestBookEntry(new GuestBookEntryDto(persistentDto, new GuestBookEntryDto()));
    var guestBookEntry = aGuestBookEntry(new GuestBookEntryDto(persistentDto, new GuestBookEntryDto()));
    guestBookEntry.setId(1L);

    when(joinPointMock.getArgs()).thenReturn(new Object[]{guestBookEntry, guestBookEntryWithoutId});
    modifierAspect.modifyPersistentEntity(joinPointMock);

    assertThat(guestBookEntry.getTimeOfModification()).isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now());
    assertThat(guestBookEntryWithoutId.getTimeOfModification()).isEqualTo(oneMinuteAgo);
  }

  @Test
  @DisplayName("should modify timestamp on person when used")
  void shouldModifyTimestampOnPersonWhenUsed() {
    var personWithoutId = aPerson(new PersonInternalDto(persistentDto, new PersonInternalDto()));
    var person = aPerson(new PersonInternalDto(persistentDto, new PersonInternalDto()));
    person.setId(1L);

    when(joinPointMock.getArgs()).thenReturn(new Object[]{person, personWithoutId});
    modifierAspect.modifyPersistentEntity(joinPointMock);

    assertThat(person.getTimeOfModification()).isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now());
    assertThat(personWithoutId.getTimeOfModification()).isEqualTo(oneMinuteAgo);
  }

  @Test
  @DisplayName("should modify timestamp on user when used")
  void shouldModifyTimestampOnUserWhenUsed() {
    var userWithoutId = aUser(new UserInternalDto(persistentDto, new UserInternalDto()));
    var user = aUser(new UserInternalDto(persistentDto, new UserInternalDto()));
    user.setId(1L);

    when(joinPointMock.getArgs()).thenReturn(new Object[]{user, userWithoutId});
    modifierAspect.modifyPersistentEntity(joinPointMock);

    assertThat(user.getTimeOfModification()).isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now());
    assertThat(userWithoutId.getTimeOfModification()).isEqualTo(oneMinuteAgo);
  }
}