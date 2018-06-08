package com.github.jactor.rises.persistence.entity.guestbook;

import com.github.jactor.rises.client.dto.NewGuestBookDto;
import com.github.jactor.rises.client.dto.NewGuestBookEntryDto;
import com.github.jactor.rises.commons.time.Now;
import com.github.jactor.rises.persistence.entity.EntryEmbeddable;
import com.github.jactor.rises.persistence.entity.PersistentEntity;
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

import static java.util.Objects.hash;

@Entity
@Table(name = "T_GUEST_BOOK_ENTRY")
public class GuestBookEntryEntity extends PersistentEntity<Long> {

    @Id private Long id;

    @ManyToOne(cascade = CascadeType.MERGE) @JoinColumn(name = "GUEST_BOOK_ID") private GuestBookEntity guestBook;

    @Embedded @AttributeOverrides({
            @AttributeOverride(name = "createdTime", column = @Column(name = "CREATED_TIME")),
            @AttributeOverride(name = "creatorName", column = @Column(name = "GUEST_NAME")),
            @AttributeOverride(name = "entry", column = @Column(name = "ENTRY"))
    }) private EntryEmbeddable entryEmbeddable = new EntryEmbeddable();

    GuestBookEntryEntity() {
        // used by jpa
    }

    private GuestBookEntryEntity(GuestBookEntryEntity guestBookEntry) {
        super(guestBookEntry);
        guestBook = guestBookEntry.copyGuestBook();
        entryEmbeddable = guestBookEntry.copyEntry();
    }

    GuestBookEntryEntity(NewGuestBookEntryDto guestBookEntry) {
        super(guestBookEntry);
        guestBook = new GuestBookEntity(guestBookEntry.getGuestBook());
        entryEmbeddable = new EntryEmbeddable(guestBookEntry.getCreatorName(), guestBookEntry.getEntry());
    }

    private GuestBookEntity copyGuestBook() {
        return guestBook.copy();
    }

    private EntryEmbeddable copyEntry() {
        return entryEmbeddable.copy();
    }

    public NewGuestBookEntryDto asDto() {
        return asDto(guestBook.asDto());
    }

    NewGuestBookEntryDto asDto(NewGuestBookDto guestBook) {
        NewGuestBookEntryDto guestBookEntry = new NewGuestBookEntryDto();
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

    @Override public GuestBookEntryEntity copy() {
        return new GuestBookEntryEntity(this);
    }

    @Override public void addSequencedIdAlsoIncludingDependencies(Sequencer sequencer) {
        id = fetchId(sequencer);
        addSequencedIdToDependencies(guestBook, sequencer);
    }

    @Override public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && isEqualTo((GuestBookEntryEntity) o);
    }

    private boolean isEqualTo(GuestBookEntryEntity o) {
        return Objects.equals(entryEmbeddable, o.entryEmbeddable) &&
                Objects.equals(guestBook, o.guestBook);
    }

    @Override public int hashCode() {
        return hash(guestBook, entryEmbeddable);
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(guestBook).append(entryEmbeddable).toString();
    }

    @Override public Long getId() {
        return id;
    }

    GuestBookEntity getGuestBook() {
        return guestBook;
    }

    public String getEntry() {
        return entryEmbeddable.getEntry();
    }

    public String getCreatorName() {
        return entryEmbeddable.getCreatorName();
    }

    void setGuestBook(GuestBookEntity guestBookEntity) {
        this.guestBook = guestBookEntity;
    }

    public void setCreatorName(String creatorName) {
        entryEmbeddable.setCreatorName(creatorName);
    }

    public static GuestBookEntryEntityBuilder aGuestBookEntry() {
        return new GuestBookEntryEntityBuilder();
    }
}
