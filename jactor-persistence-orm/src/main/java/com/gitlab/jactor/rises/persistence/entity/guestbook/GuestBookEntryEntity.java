package com.gitlab.jactor.rises.persistence.entity.guestbook;

import com.gitlab.jactor.rises.commons.dto.GuestBookDto;
import com.gitlab.jactor.rises.commons.dto.GuestBookEntryDto;
import com.gitlab.jactor.rises.commons.time.Now;
import com.gitlab.jactor.rises.persistence.entity.EntryEmbeddable;
import com.gitlab.jactor.rises.persistence.entity.PersistentEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.hash;

@Entity
@Table(name = "T_GUEST_BOOK_ENTRY")
public class GuestBookEntryEntity extends PersistentEntity<Long> {

    private @Id Long id;

    private @ManyToOne(cascade = CascadeType.MERGE) @JoinColumn(name = "GUEST_BOOK_ID") GuestBookEntity guestBook;
    private @Embedded @AttributeOverrides({
            @AttributeOverride(name = "createdTime", column = @Column(name = "CREATED_TIME")),
            @AttributeOverride(name = "creatorName", column = @Column(name = "GUEST_NAME")),
            @AttributeOverride(name = "entry", column = @Column(name = "ENTRY"))
    }) EntryEmbeddable entryEmbeddable = new EntryEmbeddable();

    GuestBookEntryEntity() {
        // used by builder
    }

    private GuestBookEntryEntity(GuestBookEntryEntity guestBookEntry) {
        super(guestBookEntry);
        guestBook = guestBookEntry.copyGuestBook();
        entryEmbeddable = guestBookEntry.copyEntry();
    }

    public GuestBookEntryEntity(GuestBookEntryDto guestBookEntry) {
        super(guestBookEntry);
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
        GuestBookEntryDto guestBookEntry = addPersistentData(new GuestBookEntryDto());
        guestBookEntry.setCreatorName(entryEmbeddable.getCreatorName());
        guestBookEntry.setEntry(entryEmbeddable.getEntry());
        guestBookEntry.setGuestBook(guestBook);

        return guestBookEntry;
    }

    public void create(String entry) {
        setCreationTime(Now.asDateTime());
        entryEmbeddable.setEntry(entry);
    }

    public void update(String entry) {
        setUpdatedTime(Now.asDateTime());
        entryEmbeddable.setEntry(entry);
    }

    public @Override GuestBookEntryEntity copy() {
        return new GuestBookEntryEntity(this);
    }

    protected @Override Stream<Optional<PersistentEntity<Long>>> streamSequencedDependencies() {
        return streamSequencedDependencies(guestBook);
    }

    public @Override boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && isEqualTo((GuestBookEntryEntity) o);
    }

    private boolean isEqualTo(GuestBookEntryEntity o) {
        return Objects.equals(entryEmbeddable, o.entryEmbeddable) &&
                Objects.equals(guestBook, o.getGuestBook());
    }

    public @Override int hashCode() {
        return hash(guestBook, entryEmbeddable);
    }

    public @Override String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .appendSuper(super.toString())
                .append(getGuestBook())
                .append(entryEmbeddable)
                .toString();
    }

    public @Override Long getId() {
        return id;
    }

    protected @Override void setId(Long id) {
        this.id = id;
    }

    @SuppressWarnings("WeakerAccess") public GuestBookEntity getGuestBook() {
        return guestBook;
    } // used by reflection

    public String getEntry() {
        return entryEmbeddable.getEntry();
    }

    public String getCreatorName() {
        return entryEmbeddable.getCreatorName();
    }

    @SuppressWarnings("WeakerAccess") public void setGuestBook(GuestBookEntity guestBookEntity) {
        this.guestBook = guestBookEntity;
    } // user by reflection

    public void setCreatorName(String creatorName) {
        entryEmbeddable.setCreatorName(creatorName);
    }

    public static GuestBookEntryEntityBuilder aGuestBookEntry() {
        return new GuestBookEntryEntityBuilder();
    }
}
