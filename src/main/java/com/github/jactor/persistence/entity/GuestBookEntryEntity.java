package com.github.jactor.persistence.entity;

import static java.util.Objects.hash;

import com.github.jactor.persistence.dto.GuestBookDto;
import com.github.jactor.persistence.dto.GuestBookEntryDto;
import com.github.jactor.persistence.dto.PersistentDto;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "T_GUEST_BOOK_ENTRY")
public class GuestBookEntryEntity implements PersistentEntity<GuestBookEntryEntity> {

  @Id
  private Long id;

  @Embedded
  @AttributeOverride(name = "createdBy", column = @Column(name = "CREATED_BY"))
  @AttributeOverride(name = "timeOfCreation", column = @Column(name = "CREATION_TIME"))
  @AttributeOverride(name = "modifiedBy", column = @Column(name = "UPDATED_BY"))
  @AttributeOverride(name = "timeOfModification", column = @Column(name = "UPDATED_TIME"))
  private PersistentDataEmbeddable persistentDataEmbeddable;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "GUEST_BOOK_ID")
  private GuestBookEntity guestBook;

  @Embedded
  @AttributeOverride(name = "createdTime", column = @Column(name = "CREATED_TIME"))
  @AttributeOverride(name = "creatorName", column = @Column(name = "GUEST_NAME"))
  @AttributeOverride(name = "entry", column = @Column(name = "ENTRY"))
  private EntryEmbeddable entryEmbeddable = new EntryEmbeddable();

  @SuppressWarnings("unused")
  GuestBookEntryEntity() {
    // used by entity manager
  }

  private GuestBookEntryEntity(GuestBookEntryEntity guestBookEntry) {
    entryEmbeddable = guestBookEntry.copyEntry();
    guestBook = guestBookEntry.copyGuestBookWithoutId();
    id = guestBookEntry.id;
    persistentDataEmbeddable = new PersistentDataEmbeddable();
  }

  public GuestBookEntryEntity(@NotNull GuestBookEntryDto guestBookEntry) {
    entryEmbeddable = new EntryEmbeddable(guestBookEntry.getCreatorName(), guestBookEntry.getEntry());
    guestBook = Optional.ofNullable(guestBookEntry.getGuestBook()).map(GuestBookEntity::new).orElse(null);
    id = guestBookEntry.getId();
    persistentDataEmbeddable = new PersistentDataEmbeddable(guestBookEntry.fetchPersistentDto());
  }

  private GuestBookEntity copyGuestBookWithoutId() {
    return guestBook.copyWithoutId();
  }

  private EntryEmbeddable copyEntry() {
    return entryEmbeddable.copy();
  }

  public GuestBookEntryDto asDto() {
    return asDto(guestBook.asDto());
  }

  private GuestBookEntryDto asDto(GuestBookDto guestBook) {
    return new GuestBookEntryDto(
        initPersistentDto(),
        guestBook,
        entryEmbeddable.getCreatorName(),
        entryEmbeddable.getEntry()
    );
  }

  public void modify(String modifiedBy, String entry) {
    entryEmbeddable.modify(modifiedBy, entry);
    persistentDataEmbeddable.modify();
  }

  @Override
  public GuestBookEntryEntity copyWithoutId() {
    GuestBookEntryEntity guestBookEntryEntity = new GuestBookEntryEntity(this);
    guestBookEntryEntity.setId(null);

    return guestBookEntryEntity;
  }

  @Override
  public PersistentDto initPersistentDto() {
    return new PersistentDto(getId(), getCreatedBy(), getTimeOfCreation(), getModifiedBy(), getTimeOfModification());
  }

  @Override
  public void modify() {
    persistentDataEmbeddable.modify();
  }

  @Override
  public Stream<PersistentEntity> streamSequencedDependencies() {
    return streamSequencedDependencies(guestBook);
  }

  @Override
  public boolean equals(Object o) {
    return this == o || o != null && getClass() == o.getClass() && isEqualTo((GuestBookEntryEntity) o);
  }

  private boolean isEqualTo(GuestBookEntryEntity o) {
    return Objects.equals(entryEmbeddable, o.entryEmbeddable) &&
        Objects.equals(guestBook, o.getGuestBook());
  }

  @Override
  public int hashCode() {
    return hash(guestBook, entryEmbeddable);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
        .appendSuper(super.toString())
        .append(getGuestBook())
        .append(entryEmbeddable)
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

  public GuestBookEntity getGuestBook() {
    return guestBook;
  }

  public String getEntry() {
    return entryEmbeddable.getEntry();
  }

  public String getCreatorName() {
    return entryEmbeddable.getCreatorName();
  }

  public static GuestBookEntryEntity aGuestBookEntry(GuestBookEntryDto guestBookEntryDto) {
    return new GuestBookEntryEntity(guestBookEntryDto);
  }
}
