package com.github.jactorrises.persistence.entity.blog;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.persistence.entity.PersistentEntity;
import com.github.jactorrises.persistence.entity.entry.PersistentEntryImpl;
import com.github.jactorrises.persistence.client.BlogEntity;
import com.github.jactorrises.persistence.client.BlogEntryEntity;
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
public class BlogEntryEntityImpl extends PersistentEntity implements BlogEntryEntity {

    @ManyToOne() @JoinColumn(name = BlogEntryMetadata.BLOG) private BlogEntityImpl blogEntity;

    @Embedded @AttributeOverrides({
            @AttributeOverride(name = "createdTime", column = @Column(name = BlogEntryMetadata.CREATED_TIME)),
            @AttributeOverride(name = "creatorName", column = @Column(name = BlogEntryMetadata.CREATOR_NAME)),
            @AttributeOverride(name = "entry", column = @Column(name = BlogEntryMetadata.ENTRY))
    }) private PersistentEntryImpl persistentEntry = new PersistentEntryImpl();

    public BlogEntryEntityImpl() { }

    public BlogEntryEntityImpl(BlogEntryEntity blogEntryEntity) {
        blogEntity = castOrInitializeCopyWith(blogEntryEntity.getBlog(), BlogEntityImpl.class);
        blogEntity = castOrInitializeCopyWith(blogEntryEntity.getBlog(), BlogEntityImpl.class);
        persistentEntry = new PersistentEntryImpl(convertFrom(blogEntryEntity.getCreatedTime(), LocalDateTime.class));
        persistentEntry.setCreatorName(convertFrom(blogEntryEntity.getCreatorName(), Name.class));
        persistentEntry.setEntry(blogEntryEntity.getEntry());
    }

    @Override public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && isEqualTo((BlogEntryEntityImpl) o);
    }

    private boolean isEqualTo(BlogEntryEntityImpl o) {
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
        this.blogEntity = castOrInitializeCopyWith(blog, BlogEntityImpl.class);
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

    public static BlogEntryEntityBuilder aBlogEntry() {
        return new BlogEntryEntityBuilder();
    }
}
