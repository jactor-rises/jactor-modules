package nu.hjemme.persistence.orm.domain;

import nu.hjemme.client.datatype.Name;
import nu.hjemme.client.domain.Entry;
import nu.hjemme.persistence.client.PersistentEntry;
import nu.hjemme.persistence.client.converter.LocalDateTimeConverter;
import nu.hjemme.persistence.client.converter.NameConverter;
import nu.hjemme.persistence.orm.time.Now;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import static java.util.Objects.hash;

@Embeddable
public class DefaultPersistentEntry implements PersistentEntry {
    private static final LocalDateTimeConverter TIME_CONVERTER = new LocalDateTimeConverter();
    private static final NameConverter NAME_CONVERTER = new NameConverter();

    private Date createdTime;
    private String creatorName;
    private String entry;

    DefaultPersistentEntry() {
        createdTime = Now.asDate();
    }

    DefaultPersistentEntry(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @param entry will be used to copy an instance...
     */
    DefaultPersistentEntry(Entry entry) {
        createdTime = TIME_CONVERTER.convertFrom(entry.getCreatedTime());
        this.entry = entry.getEntry();
        creatorName = NAME_CONVERTER.convertFrom(entry.getCreatorName());
    }

    @Override public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass() && isEqualTo((DefaultPersistentEntry) obj);
    }

    private boolean isEqualTo(DefaultPersistentEntry obj) {
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
