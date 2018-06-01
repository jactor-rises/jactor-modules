package com.github.jactor.rises.persistence.orm.entity;

import com.github.jactor.rises.client.converter.FieldConverter;
import com.github.jactor.rises.commons.time.Now;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Embeddable;
import java.util.Objects;

import static java.util.Objects.hash;

@Embeddable
public class EntryEmbeddable {

    private String creatorName;
    private String createdTime;
    private String entry;

    public EntryEmbeddable() {
        createdTime = FieldConverter.convert(Now.asDateTime());
    }

    /**
     * @param entryEmbeddable is the entry to create a copy of
     */
    EntryEmbeddable(EntryEmbeddable entryEmbeddable) {
        creatorName = entryEmbeddable.getCreatorName();
        createdTime = entryEmbeddable.getCreatedTime();
        entry = entryEmbeddable.getEntry();
    }

    public EntryEmbeddable(String createdTime, String creatorName, String entry) {
        this.creatorName = creatorName;
        this.createdTime = createdTime;
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatedTime() {
        return createdTime;
    }
}
