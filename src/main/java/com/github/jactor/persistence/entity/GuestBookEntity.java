package com.github.jactor.persistence.entity;

import static java.util.Objects.hash;
import static java.util.stream.Collectors.toSet;

import com.github.jactor.persistence.dto.GuestBookDto;
import com.github.jactor.persistence.dto.PersistentDto;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "T_GUEST_BOOK")
public class GuestBookEntity extends PersistentEntity {

  @Id
  private Long id;

  @Column(name = "TITLE")
  private String title;
  @JoinColumn(name = "USER_ID")
  @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  private UserEntity user;
  @OneToMany(mappedBy = "guestBook", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  private Set<GuestBookEntryEntity> entries = new HashSet<>();

  @SuppressWarnings("unused")
  GuestBookEntity() {
    // used by entity manager
  }

  /**
   * @param guestBook to copy...
   */
  private GuestBookEntity(GuestBookEntity guestBook) {
    super(guestBook);
    title = guestBook.title;
    user = guestBook.copyUser();
    entries = guestBook.entries.stream().map(GuestBookEntryEntity::copy).collect(toSet());
  }

  public GuestBookEntity(@NotNull GuestBookDto guestBook) {
    super(guestBook.fetchPersistentDto());
    title = guestBook.getTitle();
    Optional.ofNullable(guestBook.getUser()).map(UserEntity::new).ifPresent(userEntity -> user = userEntity);
    entries = guestBook.getEntries().stream().map(GuestBookEntryEntity::new).collect(toSet());
  }

  private UserEntity copyUser() {
    return Optional.ofNullable(user).map(UserEntity::copy).orElse(null);
  }

  public GuestBookDto asDto() {
    return new GuestBookDto(
        initPersistentDto(),
        entries.stream().map(GuestBookEntryEntity::asDto).collect(toSet()), title,
        Optional.ofNullable(user).map(UserEntity::asDto).orElse(null)
    );
  }

  @Override
  public GuestBookEntity copy() {
    return new GuestBookEntity(this);
  }

  @Override
  public PersistentDto initPersistentDto() {
    return new PersistentDto(getId(), getCreatedBy(), getCreationTime(), getUpdatedBy(), getUpdatedTime());
  }

  @Override
  public Stream<Optional<PersistentEntity>> streamSequencedDependencies() {
    return Stream.concat(streamSequencedDependencies(user), entries.stream().map(Optional::of));
  }

  @Override
  public boolean equals(Object o) {
    return this == o || o != null && getClass() == o.getClass() &&
        Objects.equals(getTitle(), ((GuestBookEntity) o).getTitle()) &&
        Objects.equals(getUser(), ((GuestBookEntity) o).getUser());
  }

  @Override
  public int hashCode() {
    return hash(title, user);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
        .appendSuper(super.toString())
        .append(getTitle())
        .append(getUser())
        .toString();
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public Set<GuestBookEntryEntity> getEntries() {
    return entries;
  }

  public String getTitle() {
    return title;
  }

  public UserEntity getUser() {
    return user;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  public static GuestBookEntity aGuestBook(GuestBookDto guestBookDto) {
    return new GuestBookEntity(guestBookDto);
  }
}
