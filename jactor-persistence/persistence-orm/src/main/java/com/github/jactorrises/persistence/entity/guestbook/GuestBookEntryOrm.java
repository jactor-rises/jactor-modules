package com.github.jactorrises.persistence.entity.guestbook;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.GuestBookEntry;
import com.github.jactorrises.persistence.client.dto.GuestBookEntryDto;
import com.github.jactorrises.persistence.entity.EntryEmbeddable;
import com.github.jactorrises.persistence.entity.PersistentEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.Objects.hash;

@Entity
@Table(name = "T_GUEST_BOOK_ENTRY")
public class GuestBookEntryOrm extends PersistentEntity implements GuestBookEntry {
    @ManyToOne() @JoinColumn(name = "GUEST_BOOK_ID") private GuestBookEntity guestBookEntity;

    @Embedded @AttributeOverrides({
            @AttributeOverride(name = "createdTime", column = @Column(name = "CREATED_TIME")),
            @AttributeOverride(name = "creatorName", column = @Column(name = "GUEST_NAME")),
            @AttributeOverride(name = "entry", column = @Column(name = "ENTRY"))
    }) private EntryEmbeddable persistentEntry = new EntryEmbeddable();

    public GuestBookEntryOrm() {
        // empty...
    }

    private GuestBookEntryOrm(GuestBookEntryOrm guestBookEntry) {
        super(guestBookEntry);
        guestBookEntity = guestBookEntry.copyGuestBook();
        persistentEntry = guestBookEntry.copyEntry();
    }

    public GuestBookEntryOrm(GuestBookEntryDto guestBookEntry) {
        super(guestBookEntry);
        guestBookEntity = new GuestBookEntity(guestBookEntry.getGuestBook());
        persistentEntry = new EntryEmbeddable(guestBookEntry.getCreatedTime(), guestBookEntry.getCreatorName(), guestBookEntry.getEntry());
    }

    public GuestBookEntryOrm copy() {
        return new GuestBookEntryOrm(this);
    }

    private GuestBookEntity copyGuestBook() {
        return guestBookEntity.copy();
    }

    private EntryEmbeddable copyEntry() {
        return persistentEntry.copy();
    }

    @Override public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && isEqualTo((GuestBookEntryOrm) o);
    }

    private boolean isEqualTo(GuestBookEntryOrm o) {
        return Objects.equals(persistentEntry, o.persistentEntry) &&
                Objects.equals(guestBookEntity, o.guestBookEntity);
    }

    @Override public int hashCode() {
        return hash(guestBookEntity, persistentEntry);
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(guestBookEntity).append(persistentEntry).toString();
    }

    @Override public GuestBookEntity getGuestBook() {
        return guestBookEntity;
    }

    @Override public LocalDateTime getCreatedTime() {
        return persistentEntry.getCreatedTime();
    }

    @Override public String getEntry() {
        return persistentEntry.getEntry();
    }

    @Override public Name getCreatorName() {
        return persistentEntry.getCreatorName();
    }

    public void setGuestBook(GuestBookEntity guestBookEntity) {
        this.guestBookEntity = guestBookEntity;
    }

    public void setEntry(String entry) {
        persistentEntry.setEntry(entry);
    }

    public void setCreatorName(String creatorName) {
        persistentEntry.setCreatorName(creatorName);
    }
}
