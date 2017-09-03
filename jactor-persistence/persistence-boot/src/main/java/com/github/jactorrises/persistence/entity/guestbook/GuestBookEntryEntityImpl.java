package com.github.jactorrises.persistence.entity.guestbook;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.GuestBookEntry;
import com.github.jactorrises.persistence.entity.PersistentEntity;
import com.github.jactorrises.persistence.entity.entry.PersistentEntryImpl;
import com.github.jactorrises.persistence.client.GuestBookEntity;
import com.github.jactorrises.persistence.client.GuestBookEntryEntity;
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
@Table(name = GuestBookEntryMetadata.GUEST_BOOK_ENTRY_TABLE)
public class GuestBookEntryEntityImpl extends PersistentEntity implements GuestBookEntryEntity {
    @ManyToOne() @JoinColumn(name = GuestBookEntryMetadata.GUEST_BOOK_ID) private GuestBookEntityImpl guestBookEntity;

    @Embedded @AttributeOverrides({
            @AttributeOverride(name = "createdTime", column = @Column(name = GuestBookEntryMetadata.CREATED_TIME)),
            @AttributeOverride(name = "creatorName", column = @Column(name = GuestBookEntryMetadata.GUEST_NAME)),
            @AttributeOverride(name = "entry", column = @Column(name = GuestBookEntryMetadata.ENTRY))
    }) private PersistentEntryImpl persistentEntry = new PersistentEntryImpl();

    public GuestBookEntryEntityImpl() {    }

    public GuestBookEntryEntityImpl(GuestBookEntry guestBookEntry) {
        guestBookEntity = castOrInitializeCopyWith(guestBookEntry.getGuestBook(), GuestBookEntityImpl.class);
        persistentEntry = new PersistentEntryImpl(convertFrom(guestBookEntry.getCreatedTime(), LocalDateTime.class));
        persistentEntry.setCreatorName(convertFrom(guestBookEntry.getCreatorName(), Name.class));
        persistentEntry.setEntry(guestBookEntry.getEntry());
    }

    @Override public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && isEqualTo((GuestBookEntryEntityImpl) o);
    }

    private boolean isEqualTo(GuestBookEntryEntityImpl o) {
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

    @Override public void setGuestBook(GuestBookEntity guestBookEntity) {
        this.guestBookEntity = castOrInitializeCopyWith(guestBookEntity, GuestBookEntityImpl.class);
    }

    @Override public void setEntry(String entry) {
        persistentEntry.setEntry(entry);
    }

    @Override public void setCreatorName(String creatorName) {
        persistentEntry.setCreatorName(creatorName);
    }

    public static GuestBookEntryEntityBuilder aGuestBookEntry() {
        return new GuestBookEntryEntityBuilder();
    }
}
