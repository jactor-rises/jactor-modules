package com.github.jactorrises.persistence.entity.blog;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.BlogEntry;
import com.github.jactorrises.persistence.client.dto.BlogEntryDto;
import com.github.jactorrises.persistence.entity.PersistentEntry;
import com.github.jactorrises.persistence.entity.PersistentOrm;
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
@Table(name = "T_BLOG_ENTRY")
public class BlogEntryOrm extends PersistentOrm implements BlogEntry {

    @ManyToOne() @JoinColumn(name = "BLOG_ID") private BlogOrm blogEntity;

    @Embedded @AttributeOverrides({
            @AttributeOverride(name = "createdTime", column = @Column(name = "CREATED_TIME")),
            @AttributeOverride(name = "creatorName", column = @Column(name = "CREATOR_NAME")),
            @AttributeOverride(name = "entry", column = @Column(name = "ENTRY"))
    }) private PersistentEntry persistentEntry = new PersistentEntry();

    public BlogEntryOrm() {
    }

    private BlogEntryOrm(BlogEntryOrm blogEntryOrm) {
        super(blogEntryOrm);
        blogEntity = blogEntryOrm.copyBlog();
        persistentEntry = blogEntryOrm.copyEntry();
    }

    private BlogOrm copyBlog() {
        return blogEntity.copy();
    }

    private PersistentEntry copyEntry() {
        return persistentEntry.copy();
    }

    public BlogEntryOrm(BlogEntryDto blogEntryDto) {
        super(blogEntryDto);
        blogEntity = new BlogOrm(blogEntryDto.getBlog());
        persistentEntry = new PersistentEntry(blogEntryDto.getCreatedTime(), blogEntryDto.getCreatorName(), blogEntryDto.getEntry());
    }

    public BlogEntryOrm copy() {
        return new BlogEntryOrm(this);
    }

    @Override public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && isEqualTo((BlogEntryOrm) o);
    }

    private boolean isEqualTo(BlogEntryOrm o) {
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

    @Override public BlogOrm getBlog() {
        return blogEntity;
    }

    public void setBlog(BlogOrm blog) {
        this.blogEntity = blog;
    }

    @Override public LocalDateTime getCreatedTime() {
        return persistentEntry.getCreatedTime();
    }

    @Override public Name getCreatorName() {
        return persistentEntry.getCreatorName();
    }

    public void setCreatorName(String creator) {
        persistentEntry.setCreatorName(creator);
    }

    @Override public String getEntry() {
        return persistentEntry.getEntry();
    }

    public void setEntry(String entry) {
        persistentEntry.setEntry(entry);
    }
}
