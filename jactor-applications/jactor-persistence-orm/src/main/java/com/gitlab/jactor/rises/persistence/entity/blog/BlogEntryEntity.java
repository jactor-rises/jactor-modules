package com.gitlab.jactor.rises.persistence.entity.blog;

import com.gitlab.jactor.rises.commons.dto.BlogDto;
import com.gitlab.jactor.rises.commons.dto.BlogEntryDto;
import com.gitlab.jactor.rises.commons.time.Now;
import com.gitlab.jactor.rises.persistence.entity.EntryEmbeddable;
import com.gitlab.jactor.rises.persistence.entity.PersistentEntity;
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
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.hash;

@Entity
@Table(name = "T_BLOG_ENTRY")
public class BlogEntryEntity extends PersistentEntity<Long> {

    private @Id Long id;

    private @ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = "BLOG_ID") BlogEntity blog;
    private @Embedded @AttributeOverrides({
            @AttributeOverride(name = "creatorName", column = @Column(name = "CREATOR_NAME")),
            @AttributeOverride(name = "entry", column = @Column(name = "ENTRY"))
    }) EntryEmbeddable entryEmbeddable = new EntryEmbeddable();

    BlogEntryEntity() {
        // used by builder
    }

    private BlogEntryEntity(BlogEntryEntity blogEntryEntity) {
        super(blogEntryEntity);
        blog = blogEntryEntity.copyBlog();
        entryEmbeddable = blogEntryEntity.copyEntry();
    }

    public BlogEntryEntity(BlogEntryDto blogEntryDto) {
        super(blogEntryDto);
        Optional.ofNullable(blogEntryDto.getBlog()).ifPresent(blogDto -> blog = new BlogEntity(blogDto));
        entryEmbeddable = new EntryEmbeddable(blogEntryDto.getCreatorName(), blogEntryDto.getEntry());
    }

    private BlogEntity copyBlog() {
        return blog.copy();
    }

    private EntryEmbeddable copyEntry() {
        return entryEmbeddable.copy();
    }

    public BlogEntryDto asDto() {
        return asDto(blog.asDto());
    }

    private BlogEntryDto asDto(BlogDto blogDto) {
        BlogEntryDto blogEntryDto = new BlogEntryDto();
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

    public @Override BlogEntryEntity copy() {
        return new BlogEntryEntity(this);
    }

    protected @Override Stream<Optional<PersistentEntity<Long>>> streamSequencedDependencies() {
        return streamSequencedDependencies(blog);
    }

    public @Override boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && isEqualTo((BlogEntryEntity) o);
    }

    private boolean isEqualTo(BlogEntryEntity o) {
        return Objects.equals(entryEmbeddable, o.entryEmbeddable) &&
                Objects.equals(blog, o.getBlog());
    }

    public @Override int hashCode() {
        return hash(blog, entryEmbeddable);
    }

    public @Override String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .appendSuper(super.toString())
                .append(getBlog())
                .append(entryEmbeddable)
                .toString();
    }

    public @Override Long getId() {
        return id;
    }

    protected @Override void setId(Long id) {
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
