package com.github.jactorrises.persistence.entity;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.commons.time.Now;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import static java.util.Objects.hash;

@Embeddable
public class EntryEmbeddable {

    private Date createdTime;
    private String creatorName;
    private String entry;

    public EntryEmbeddable() {
        createdTime = Now.asDate();
    }

    /**
     * @param entryEmbeddable is the entry to create a copy of
     */
    EntryEmbeddable(EntryEmbeddable entryEmbeddable) {
        creatorName = entryEmbeddable.creatorName;
        createdTime = entryEmbeddable.copyCreatedTime();
        entry = entryEmbeddable.entry;
    }

    private Date copyCreatedTime() {
        Date creation = new Date();
        creation.setTime(this.createdTime.getTime());

        return creation;
    }

    public EntryEmbeddable(LocalDateTime createdTime, Name creatorName, String entry) {
        this.createdTime = Date.from(createdTime.atZone(ZoneId.systemDefault()).toInstant());
        this.creatorName = creatorName.asString();
        this.entry = entry;
    }

    public EntryEmbeddable copy() {
        return new EntryEmbeddable(this);
    }

    @Override public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass() && isEqualTo((EntryEmbeddable) obj);
    }

    private boolean isEqualTo(EntryEmbeddable obj) {
        return Objects.equals(createdTime, obj.createdTime) &&
                Objects.equals(entry, obj.entry) &&
                Objects.equals(creatorName, obj.creatorName);
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(getCreatedTime()).append(creatorName).append(shortEntry()).toString();
    }

    private String shortEntry() {
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
