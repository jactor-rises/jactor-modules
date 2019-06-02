package com.github.jactor.persistence.entity;

import static java.util.Objects.hash;
import static java.util.stream.Collectors.toSet;

import com.github.jactor.persistence.dto.GuestBookDto;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "T_GUEST_BOOK")
public class GuestBookEntity implements PersistentEntity<GuestBookEntity> {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "guestBookSeq")
  @SequenceGenerator(name = "guestBookSeq", sequenceName = "T_GUEST_BOOK_SEQ", allocationSize = 1)
  private Long id;

  @Embedded
  @AttributeOverride(name = "createdBy", column = @Column(name = "CREATED_BY"))
  @AttributeOverride(name = "timeOfCreation", column = @Column(name = "CREATION_TIME"))
  @AttributeOverride(name = "modifiedBy", column = @Column(name = "UPDATED_BY"))
  @AttributeOverride(name = "timeOfModification", column = @Column(name = "UPDATED_TIME"))
  private PersistentDataEmbeddable persistentDataEmbeddable;

  @Column(name = "TITLE")
  private String title;
  @JoinColumn(name = "USER_ID")
  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
  private UserEntity user;
  @OneToMany(mappedBy = "guestBook", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  private Set<GuestBookEntryEntity> entries = new HashSet<>();

  @SuppressWarnings("unused")
  GuestBookEntity() {
    // used by entity manager
  }

  /**
   * @param guestBook to copyWithoutId...
   */
  private GuestBookEntity(GuestBookEntity guestBook) {
    entries = guestBook.entries.stream().map(GuestBookEntryEntity::copyWithoutId).collect(toSet());
    id = guestBook.id;
    persistentDataEmbeddable = new PersistentDataEmbeddable();
    title = guestBook.title;
    user = guestBook.copyUserWithoutId();
  }

  public GuestBookEntity(@NotNull GuestBookDto guestBook) {
    entries = guestBook.getEntries().stream().map(GuestBookEntryEntity::new).collect(toSet());
    id = guestBook.getId();
    persistentDataEmbeddable = new PersistentDataEmbeddable(guestBook.fetchPersistentDto());
    title = guestBook.getTitle();
    user = Optional.ofNullable(guestBook.getUser()).map(UserEntity::new).orElse(null);
  }

  private UserEntity copyUserWithoutId() {
    return Optional.ofNullable(user).map(UserEntity::copyWithoutId).orElse(null);
  }

  public GuestBookDto asDto() {
    return new GuestBookDto(
        persistentDataEmbeddable.asPersistentDto(id),
        entries.stream().map(GuestBookEntryEntity::asDto).collect(toSet()),
        title,
        Optional.ofNullable(user).map(UserEntity::asDto).orElse(null)
    );
  }

  @Override
  public GuestBookEntity copyWithoutId() {
    GuestBookEntity guestBookEntity = new GuestBookEntity(this);
    guestBookEntity.setId(null);

    return guestBookEntity;
  }

  @Override
  public void modify() {
    persistentDataEmbeddable.modify();
  }

  public void add(GuestBookEntryEntity guestBookEntry) {
    entries.add(guestBookEntry);
    guestBookEntry.setGuestBook(this);
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
  public String getCreatedBy() {
    return persistentDataEmbeddable.getCreatedBy();
  }

  @Override
  public LocalDateTime getTimeOfCreation() {
    return persistentDataEmbeddable.getTimeOfCreation();
  }

  @Override
  public String getModifiedBy() {
    return persistentDataEmbeddable.getModifiedBy();
  }

  @Override
  public LocalDateTime getTimeOfModification() {
    return persistentDataEmbeddable.getTimeOfModification();
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
