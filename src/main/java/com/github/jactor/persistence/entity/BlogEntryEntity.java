package com.github.jactor.persistence.entity;

import static java.util.Objects.hash;

import com.github.jactor.persistence.dto.BlogDto;
import com.github.jactor.persistence.dto.BlogEntryDto;
import com.github.jactor.persistence.dto.PersistentDto;
import com.github.jactor.persistence.time.Now;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "T_BLOG_ENTRY")
public class BlogEntryEntity extends PersistentEntity {

  @Id
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "BLOG_ID")
  private BlogEntity blog;

  @Embedded
  @AttributeOverride(name = "creatorName", column = @Column(name = "CREATOR_NAME"))
  @AttributeOverride(name = "entry", column = @Column(name = "ENTRY"))
  private EntryEmbeddable entryEmbeddable = new EntryEmbeddable();

  @SuppressWarnings("unused")
  BlogEntryEntity() {
    // used by entity manager
  }

  private BlogEntryEntity(BlogEntryEntity blogEntryEntity) {
    super(blogEntryEntity);
    blog = blogEntryEntity.copyBlog();
    entryEmbeddable = blogEntryEntity.copyEntry();
  }

  public BlogEntryEntity(@NotNull BlogEntryDto blogEntryDto) {
    super(blogEntryDto.fetchPersistentDto());
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

  @Override
  public BlogEntryEntity copy() {
    return new BlogEntryEntity(this);
  }

  @Override
  public PersistentDto initPersistentDto() {
    return new PersistentDto(getId(), getCreatedBy(), getCreationTime(), getUpdatedBy(), getUpdatedTime());
  }

  @Override
  public Stream<Optional<PersistentEntity>> streamSequencedDependencies() {
    return streamSequencedDependencies(blog);
  }

  @Override
  public boolean equals(Object o) {
    return this == o || o != null && getClass() == o.getClass() && isEqualTo((BlogEntryEntity) o);
  }

  private boolean isEqualTo(BlogEntryEntity o) {
    return Objects.equals(entryEmbeddable, o.entryEmbeddable) &&
        Objects.equals(blog, o.getBlog());
  }

  @Override
  public int hashCode() {
    return hash(blog, entryEmbeddable);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
        .appendSuper(super.toString())
        .append(getBlog())
        .append(entryEmbeddable)
        .toString();
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
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

  public static BlogEntryEntity aBlogEntry(BlogEntryDto blogEntryDto) {
    return new BlogEntryEntity(blogEntryDto);
  }
}
