package com.github.jactorrises.persistence.orm.entity.guestbook;

import com.github.jactorrises.client.persistence.dto.GuestBookDto;
import com.github.jactorrises.client.persistence.dto.GuestBookEntryDto;
import com.github.jactorrises.persistence.orm.entity.EntryEmbeddable;
import com.github.jactorrises.persistence.orm.entity.PersistentEntity;
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
public class GuestBookEntryEntity extends PersistentEntity {
    @ManyToOne() @JoinColumn(name = "GUEST_BOOK_ID") private GuestBookEntity guestBookEntity;

    @Embedded @AttributeOverrides({
            @AttributeOverride(name = "createdTime", column = @Column(name = "CREATED_TIME")),
            @AttributeOverride(name = "creatorName", column = @Column(name = "GUEST_NAME")),
            @AttributeOverride(name = "entry", column = @Column(name = "ENTRY"))
    }) private EntryEmbeddable persistentEntry = new EntryEmbeddable();

    GuestBookEntryEntity() {
        // empty...
    }

    private GuestBookEntryEntity(GuestBookEntryEntity guestBookEntry) {
        super(guestBookEntry);
        guestBookEntity = guestBookEntry.copyGuestBook();
        persistentEntry = guestBookEntry.copyEntry();
    }

    public GuestBookEntryEntity copy() {
        return new GuestBookEntryEntity(this);
    }

    private GuestBookEntity copyGuestBook() {
        return guestBookEntity.copy();
    }

    private EntryEmbeddable copyEntry() {
        return persistentEntry.copy();
    }

    GuestBookEntryDto asDto(GuestBookDto guestBook) {
        GuestBookEntryDto guestBookEntry = new GuestBookEntryDto();
        guestBookEntry.setCreatorName(persistentEntry.getCreatorName());
        guestBookEntry.setCreatedTime(persistentEntry.getCreatedTime());
        guestBookEntry.setEntry(persistentEntry.getEntry());
        guestBookEntry.setGuestBook(guestBook);

        return guestBookEntry;
    }

    @Override public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && isEqualTo((GuestBookEntryEntity) o);
    }

    private boolean isEqualTo(GuestBookEntryEntity o) {
        return Objects.equals(persistentEntry, o.persistentEntry) &&
                Objects.equals(guestBookEntity, o.guestBookEntity);
    }

    @Override public int hashCode() {
        return hash(guestBookEntity, persistentEntry);
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(guestBookEntity).append(persistentEntry).toString();
    }

    public GuestBookEntity getGuestBook() {
        return guestBookEntity;
    }

    public LocalDateTime getCreatedTime() {
        return persistentEntry.getCreatedTime();
    }

    public String getEntry() {
        return persistentEntry.getEntry();
    }

    public String getCreatorName() {
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

    public static GuestBookEntryEntityBuilder aGuestBookEntry() {
        return new GuestBookEntryEntityBuilder();
    }
}
