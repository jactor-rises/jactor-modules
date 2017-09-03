package com.github.jactorrises.persistence.entity.blog;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.persistence.entity.PersistentEntity;
import com.github.jactorrises.persistence.entity.entry.PersistentEntry;
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
@Table(name = BlogEntryMetadata.BLOG_ENTRY_TABLE)
public class BlogEntryEntity extends PersistentEntity {

    @ManyToOne() @JoinColumn(name = BlogEntryMetadata.BLOG) private BlogEntity blogEntity;

    @Embedded @AttributeOverrides({
            @AttributeOverride(name = "createdTime", column = @Column(name = BlogEntryMetadata.CREATED_TIME)),
            @AttributeOverride(name = "creatorName", column = @Column(name = BlogEntryMetadata.CREATOR_NAME)),
            @AttributeOverride(name = "entry", column = @Column(name = BlogEntryMetadata.ENTRY))
    }) private PersistentEntry persistentEntry = new PersistentEntry();

    public BlogEntryEntity() { }

    public BlogEntryEntity(BlogEntryEntity blogEntryEntity) {
        blogEntity = blogEntryEntity.getBlog(), BlogEntity.class);
        blogEntity = blogEntryEntity.getBlog(), BlogEntity.class);
        persistentEntry = new PersistentEntry(convertFrom(blogEntryEntity.getCreatedTime(), LocalDateTime.class));
        persistentEntry.setCreatorName(convertFrom(blogEntryEntity.getCreatorName(), Name.class));
        persistentEntry.setEntry(blogEntryEntity.getEntry());
    }

    @Override public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && isEqualTo((BlogEntryEntity) o);
    }

    private boolean isEqualTo(BlogEntryEntity o) {
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

    public BlogEntity getBlog() {
        return blogEntity;
    }

    public void setBlog(BlogEntity blog) {
        this.blogEntity = blog;
    }

    public LocalDateTime getCreatedTime() {
        return persistentEntry.getCreatedTime();
    }

    public Name getCreatorName() {
        return persistentEntry.getCreatorName();
    }

    public void setCreatorName(String creator) {
        persistentEntry.setCreatorName(creator);
    }

    public String getEntry() {
        return persistentEntry.getEntry();
    }

    public void setEntry(String entry) {
        persistentEntry.setEntry(entry);
    }

    public static BlogEntryEntityBuilder aBlogEntry() {
        return new BlogEntryEntityBuilder();
    }
}
