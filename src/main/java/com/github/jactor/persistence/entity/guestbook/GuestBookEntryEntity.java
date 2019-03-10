package com.github.jactor.persistence.entity.guestbook;

import static java.util.Objects.hash;

import com.github.jactor.persistence.dto.GuestBookDto;
import com.github.jactor.persistence.dto.GuestBookEntryDto;
import com.github.jactor.persistence.entity.EntryEmbeddable;
import com.github.jactor.persistence.entity.PersistentEntity;
import com.github.jactor.persistence.time.Now;
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
public class GuestBookEntryEntity extends PersistentEntity {

  @Id
  private Long id;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "GUEST_BOOK_ID")
  private GuestBookEntity guestBook;

  @Embedded
  @AttributeOverride(name = "createdTime", column = @Column(name = "CREATED_TIME"))
  @AttributeOverride(name = "creatorName", column = @Column(name = "GUEST_NAME"))
  @AttributeOverride(name = "entry", column = @Column(name = "ENTRY"))
  private EntryEmbeddable entryEmbeddable = new EntryEmbeddable();

  GuestBookEntryEntity() {
    // used by builder
  }

  private GuestBookEntryEntity(GuestBookEntryEntity guestBookEntry) {
    super(guestBookEntry);
    guestBook = guestBookEntry.copyGuestBook();
    entryEmbeddable = guestBookEntry.copyEntry();
  }

  public GuestBookEntryEntity(@NotNull GuestBookEntryDto guestBookEntry) {
    super(guestBookEntry.fetchPersistentDto());
    Optional.ofNullable(guestBookEntry.getGuestBook()).map(GuestBookEntity::new).ifPresent(guestBookEntity -> guestBook = guestBookEntity);
    entryEmbeddable = new EntryEmbeddable(guestBookEntry.getCreatorName(), guestBookEntry.getEntry());
  }

  private GuestBookEntity copyGuestBook() {
    return guestBook.copy();
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

  public void create(String entry) {
    setCreationTime(Now.asDateTime());
    entryEmbeddable.setEntry(entry);
  }

  public void update(String entry) {
    setUpdatedTime(Now.asDateTime());
    entryEmbeddable.setEntry(entry);
  }

  @Override
  public GuestBookEntryEntity copy() {
    return new GuestBookEntryEntity(this);
  }

  @Override
  protected Stream<Optional<PersistentEntity>> streamSequencedDependencies() {
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
  public Long getId() {
    return id;
  }

  @Override
  protected void setId(Long id) {
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

  public void setGuestBook(GuestBookEntity guestBookEntity) {
    this.guestBook = guestBookEntity;
  }

  public void setCreatorName(String creatorName) {
    entryEmbeddable.setCreatorName(creatorName);
  }

  public static GuestBookEntryEntityBuilder aGuestBookEntry() {
    return new GuestBookEntryEntityBuilder();
  }

  public static GuestBookEntryEntity aGuestBookEntry(GuestBookEntryDto guestBookEntryDto) {
    return new GuestBookEntryEntity(guestBookEntryDto);
  }
}
