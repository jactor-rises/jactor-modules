package com.github.jactor.rises.persistence.entity.blog;

import com.github.jactor.rises.client.dto.NewBlogDto;
import com.github.jactor.rises.client.dto.NewBlogEntryDto;
import com.github.jactor.rises.commons.time.Now;
import com.github.jactor.rises.persistence.entity.EntryEmbeddable;
import com.github.jactor.rises.persistence.entity.PersistentEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

import static java.util.Objects.hash;

@Entity
@Table(name = "T_BLOG_ENTRY")
public class BlogEntryEntity extends PersistentEntity<Long> {

    @Id private Long id;

    @ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = "BLOG_ID") private BlogEntity blog;

    @Embedded @AttributeOverrides({
            @AttributeOverride(name = "creatorName", column = @Column(name = "CREATOR_NAME")),
            @AttributeOverride(name = "entry", column = @Column(name = "ENTRY"))
    }) private EntryEmbeddable entryEmbeddable = new EntryEmbeddable();

    BlogEntryEntity() {
    }

    private BlogEntryEntity(BlogEntryEntity blogEntryEntity) {
        super(blogEntryEntity);
        blog = blogEntryEntity.copyBlog();
        entryEmbeddable = blogEntryEntity.copyEntry();
    }

    public BlogEntryEntity(NewBlogEntryDto blogEntryDto) {
        super(blogEntryDto);
        blog = new BlogEntity(blogEntryDto.getBlog());
        entryEmbeddable = new EntryEmbeddable(blogEntryDto.getCreatorName(), blogEntryDto.getEntry());
    }

    private BlogEntity copyBlog() {
        return blog.copy();
    }

    private EntryEmbeddable copyEntry() {
        return entryEmbeddable.copy();
    }

    BlogEntryEntity copy() {
        return new BlogEntryEntity(this);
    }

    public NewBlogEntryDto asDto() {
        return asDto(blog.asDto());
    }

    NewBlogEntryDto asDto(NewBlogDto blogDto) {
        NewBlogEntryDto blogEntryDto = new NewBlogEntryDto();
        blogEntryDto.setBlog(blogDto);
        blogEntryDto.setCreatorName(entryEmbeddable.getCreatorName());
        blogEntryDto.setEntry(entryEmbeddable.getEntry());

        return blogEntryDto;
    }

    public void create(String entry) {
        setCreationTime(Now.asDateTime());
        entryEmbeddable.setEntry(entry);
    }

    public void update(String entry) {
        setUpdatedTime(Now.asDateTime());
        entryEmbeddable.setEntry(entry);
    }

    @Override public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && isEqualTo((BlogEntryEntity) o);
    }

    private boolean isEqualTo(BlogEntryEntity o) {
        return Objects.equals(entryEmbeddable, o.entryEmbeddable) &&
                Objects.equals(blog, o.blog);
    }

    @Override public int hashCode() {
        return hash(blog, entryEmbeddable);
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(blog).append(entryEmbeddable).toString();
    }

    @Override public Long getId() {
        return id;
    }

    @Override public void setId(Long id) {
        this.id = id;
    }

    public BlogEntity getBlog() {
        return blog;
    }

    public void setBlog(BlogEntity blog) {
        this.blog = blog;
    }

    public String getCreatorName() {
        return entryEmbeddable.getCreatorName();
    }

    public void setCreatorName(String creator) {
        entryEmbeddable.setCreatorName(creator);
    }

    public String getEntry() {
        return entryEmbeddable.getEntry();
    }

    public static BlogEntryEntityBuilder aBlogEntry() {
        return new BlogEntryEntityBuilder();
    }
}
