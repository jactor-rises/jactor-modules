package com.github.jactor.rises.persistence.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.util.Objects;

import static java.util.Objects.hash;

@Embeddable
public class EntryEmbeddable {

    private String creatorName;
    @Lob private String entry;

    public EntryEmbeddable() {
    }

    /**
     * @param entryEmbeddable is the entry to create a copy of
     */
    private EntryEmbeddable(EntryEmbeddable entryEmbeddable) {
        creatorName = entryEmbeddable.getCreatorName();
        entry = entryEmbeddable.getEntry();
    }

    public EntryEmbeddable(String creatorName, String entry) {
        this.creatorName = creatorName;
        this.entry = entry;
    }

    public EntryEmbeddable copy() {
        return new EntryEmbeddable(this);
    }

    @Override public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass() && isEqualTo((EntryEmbeddable) obj);
    }

    private boolean isEqualTo(EntryEmbeddable obj) {
        return Objects.equals(entry, obj.entry) &&
                Objects.equals(creatorName, obj.creatorName);
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(creatorName).append(shortEntry()).toString();
    }

    private String shortEntry() {
        if (entry == null || entry.length() < 50) {
            return entry;
        }

        return entry.substring(0, 47) + "...";
    }

    @Override public int hashCode() {
        return hash(creatorName, entry);
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }
}
