package nu.hjemme.persistence.orm.domain;

import nu.hjemme.client.datatype.Name;
import nu.hjemme.persistence.client.BlogEntity;
import nu.hjemme.persistence.client.BlogEntryEntity;
import nu.hjemme.persistence.orm.meta.BlogEntryMetadata;
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
import java.util.Date;
import java.util.Objects;

import static java.util.Objects.hash;

@Entity
@Table(name = BlogEntryMetadata.BLOG_ENTRY_TABLE)
public class DefaultBlogEntryEntity extends DefaultPersistentEntity implements BlogEntryEntity {

    @ManyToOne() @JoinColumn(name = BlogEntryMetadata.BLOG) private DefaultBlogEntity blogEntity;

    @Embedded @AttributeOverrides({
            @AttributeOverride(name = "createdTime", column = @Column(name = BlogEntryMetadata.CREATED_TIME)),
            @AttributeOverride(name = "creatorName", column = @Column(name = BlogEntryMetadata.CREATOR_NAME)),
            @AttributeOverride(name = "entry", column = @Column(name = BlogEntryMetadata.ENTRY))
    }) private DefaultPersistentEntry persistentEntry;

    public DefaultBlogEntryEntity() {
        persistentEntry = new DefaultPersistentEntry();
    }

    public DefaultBlogEntryEntity(BlogEntryEntity blogEntryEntity) {
        blogEntity = castOrInitializeCopyWith(blogEntryEntity.getBlog(), DefaultBlogEntity.class);
        blogEntity = castOrInitializeCopyWith(blogEntryEntity.getBlog(), DefaultBlogEntity.class);
        persistentEntry = new DefaultPersistentEntry((Date) convertFrom(blogEntryEntity.getCreatedTime(), LocalDateTime.class));
        persistentEntry.setCreatorName(convertFrom(blogEntryEntity.getCreatorName(), Name.class));
        persistentEntry.setEntry(blogEntryEntity.getEntry());
    }

    @Override public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && isEqualTo((DefaultBlogEntryEntity) o);
    }

    private boolean isEqualTo(DefaultBlogEntryEntity o) {
        return Objects.equals(getId(), o.getId()) &&
                Objects.equals(persistentEntry, o.persistentEntry) &&
                Objects.equals(blogEntity, o.blogEntity);
    }

    @Override public int hashCode() {
        return hash(blogEntity, persistentEntry);
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(blogEntity).append(persistentEntry).toString();
    }

    @Override public BlogEntity getBlog() {
        return blogEntity;
    }

    @Override public void setBlog(BlogEntity blog) {
        this.blogEntity = castOrInitializeCopyWith(blog, DefaultBlogEntity.class);
    }

    @Override public LocalDateTime getCreatedTime() {
        return persistentEntry.getCreatedTime();
    }

    @Override public Name getCreatorName() {
        return persistentEntry.getCreatorName();
    }

    @Override public void setCreatorName(String creator) {
        persistentEntry.setCreatorName(creator);
    }

    @Override public String getEntry() {
        return persistentEntry.getEntry();
    }

    @Override public void setEntry(String entry) {
        persistentEntry.setEntry(entry);
    }
}
