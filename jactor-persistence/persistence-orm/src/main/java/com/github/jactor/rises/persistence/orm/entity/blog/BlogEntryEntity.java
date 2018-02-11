package com.github.jactor.rises.persistence.orm.entity.blog;

import com.github.jactor.rises.client.dto.BlogDto;
import com.github.jactor.rises.client.dto.BlogEntryDto;
import com.github.jactor.rises.persistence.orm.entity.EntryEmbeddable;
import com.github.jactor.rises.persistence.orm.entity.PersistentEntity;
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
import java.util.Objects;

import static java.util.Objects.hash;

@Entity
@Table(name = "T_BLOG_ENTRY")
public class BlogEntryEntity extends PersistentEntity {

    @ManyToOne() @JoinColumn(name = "BLOG_ID") private BlogEntity blogEntity;

    @Embedded @AttributeOverrides({
            @AttributeOverride(name = "createdTime", column = @Column(name = "CREATED_TIME")),
            @AttributeOverride(name = "creatorName", column = @Column(name = "CREATOR_NAME")),
            @AttributeOverride(name = "entry", column = @Column(name = "ENTRY"))
    }) private EntryEmbeddable persistentEntry = new EntryEmbeddable();

    BlogEntryEntity() {
    }

    private BlogEntryEntity(BlogEntryEntity blogEntryEntity) {
        super(blogEntryEntity);
        blogEntity = blogEntryEntity.copyBlog();
        persistentEntry = blogEntryEntity.copyEntry();
    }

    private BlogEntity copyBlog() {
        return blogEntity.copy();
    }

    private EntryEmbeddable copyEntry() {
        return persistentEntry.copy();
    }

    public BlogEntryEntity(BlogEntryDto blogEntryDto) {
        super(blogEntryDto);
        blogEntity = new BlogEntity(blogEntryDto.getBlog());
        persistentEntry = new EntryEmbeddable(blogEntryDto.getCreatedTime(), blogEntryDto.getCreatorName(), blogEntryDto.getEntry());
    }

    public BlogEntryEntity copy() {
        return new BlogEntryEntity(this);
    }

    BlogEntryDto asDto(BlogDto blogDto) {
        BlogEntryDto blogEntryDto = new BlogEntryDto();
        blogEntryDto.setBlog(blogDto);
        blogEntryDto.setCreatorName(persistentEntry.getCreatorName());
        blogEntryDto.setCreatedTime(persistentEntry.getCreatedTime());
        blogEntryDto.setEntry(persistentEntry.getEntry());

        return blogEntryDto;
    }

    public BlogEntryDto asDto() {
        return asDto(blogEntity.asDto());
    }

    @Override public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && isEqualTo((BlogEntryEntity) o);
    }

    private boolean isEqualTo(BlogEntryEntity o) {
        return Objects.equals(persistentEntry, o.persistentEntry) &&
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

    public String getCreatorName() {
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
