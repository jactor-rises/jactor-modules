package com.github.jactorrises.persistence.boot.entity.entry;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.Entry;
import com.github.jactorrises.persistence.client.PersistentEntry;
import com.github.jactorrises.persistence.client.converter.LocalDateTimeConverter;
import com.github.jactorrises.persistence.client.converter.NameConverter;
import com.github.jactorrises.persistence.client.time.Now;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import static java.util.Objects.hash;

@Embeddable
public class PersistentEntryImpl implements PersistentEntry {
    private static final LocalDateTimeConverter TIME_CONVERTER = new LocalDateTimeConverter();
    private static final NameConverter NAME_CONVERTER = new NameConverter();

    private Date createdTime;
    private String creatorName;
    private String entry;

    PersistentEntryImpl() {
        createdTime = Now.asDate();
    }

    PersistentEntryImpl(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @param entry will be used to copy an instance...
     */
    PersistentEntryImpl(Entry entry) {
        createdTime = TIME_CONVERTER.convertFrom(entry.getCreatedTime());
        this.entry = entry.getEntry();
        creatorName = NAME_CONVERTER.convertFrom(entry.getCreatorName());
    }

    @Override public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass() && isEqualTo((PersistentEntryImpl) obj);
    }

    private boolean isEqualTo(PersistentEntryImpl obj) {
        return Objects.equals(createdTime, obj.createdTime) &&
                Objects.equals(entry, obj.entry) &&
                Objects.equals(creatorName, obj.creatorName);
    }

    @Override
    public String toString() {
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

    @Override public String getEntry() {
        return entry;
    }

    @Override public Name getCreatorName() {
        return NAME_CONVERTER.convertTo(creatorName);
    }

    @Override public void setEntry(String entry) {
        this.entry = entry;
    }

    @Override public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @Override public LocalDateTime getCreatedTime() {
        return TIME_CONVERTER.convertTo(createdTime);
    }
}
