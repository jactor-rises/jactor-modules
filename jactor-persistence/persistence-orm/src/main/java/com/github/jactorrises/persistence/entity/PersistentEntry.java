package com.github.jactorrises.persistence.entity;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.time.Now;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import static java.util.Objects.hash;

@Embeddable
public class PersistentEntry {

    private Date createdTime;
    private String creatorName;
    private String entry;

    public PersistentEntry() {
        createdTime = Now.asDate();
    }

    /**
     * @param persistentEntry is the entry to create a copy of
     */
    PersistentEntry(PersistentEntry persistentEntry) {
        creatorName = persistentEntry.creatorName;
        createdTime = persistentEntry.copyCreatedTime();
        entry = persistentEntry.entry;
    }

    private Date copyCreatedTime() {
        Date creation = new Date();
        creation.setTime(this.createdTime.getTime());

        return creation;
    }

    public PersistentEntry copy() {
        return new PersistentEntry(this);
    }

    @Override public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass() && isEqualTo((PersistentEntry) obj);
    }

    private boolean isEqualTo(PersistentEntry obj) {
        return Objects.equals(createdTime, obj.createdTime) &&
                Objects.equals(entry, obj.entry) &&
                Objects.equals(creatorName, obj.creatorName);
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(getCreatedTime()).append(creatorName).append(entryAsString()).toString();
    }

    private String entryAsString() {
        if (entry == null || entry.length() < 50) {
            return entry;
        }

        return entry.substring(0, 47) + "...";
    }

    @Override public int hashCode() {
        return hash(createdTime, creatorName, entry);
    }

    public String getEntry() {
        return entry;
    }

    public Name getCreatorName() {
        return creatorName != null ? new Name(creatorName) : null;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public LocalDateTime getCreatedTime() {
        return new DateTimeEmbeddable(createdTime).fetchLocalDateTime();
    }
}
