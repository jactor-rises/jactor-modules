package com.github.jactor.persistence.entity;

import static java.util.Objects.hash;

import com.github.jactor.persistence.dto.BlogDto;
import com.github.jactor.persistence.dto.BlogEntryDto;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "T_BLOG_ENTRY")
public class BlogEntryEntity implements PersistentEntity<BlogEntryEntity> {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blogEntrySeq")
  @SequenceGenerator(name = "blogEntrySeq", sequenceName = "T_BLOG_ENTRY_SEQ", allocationSize = 1)
  private Long id;

  @Embedded
  @AttributeOverride(name = "createdBy", column = @Column(name = "CREATED_BY"))
  @AttributeOverride(name = "timeOfCreation", column = @Column(name = "CREATION_TIME"))
  @AttributeOverride(name = "modifiedBy", column = @Column(name = "UPDATED_BY"))
  @AttributeOverride(name = "timeOfModification", column = @Column(name = "UPDATED_TIME"))
  private PersistentDataEmbeddable persistentDataEmbeddable;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
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
    blog = blogEntryEntity.copyBlog();
    entryEmbeddable = blogEntryEntity.copyEntry();
    id = blogEntryEntity.id;
    persistentDataEmbeddable = new PersistentDataEmbeddable();
  }

  public BlogEntryEntity(BlogEntryDto blogEntryDto) {
    blog = Optional.ofNullable(blogEntryDto.getBlog()).map(BlogEntity::new).orElse(null);
    entryEmbeddable = new EntryEmbeddable(blogEntryDto.getCreatorName(), blogEntryDto.getEntry());
    id = blogEntryDto.getId();
    persistentDataEmbeddable = new PersistentDataEmbeddable(blogEntryDto.fetchPersistentDto());
  }

  private BlogEntity copyBlog() {
    return blog.copyWithoutId();
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

  public void modify(String entry, String modifiedCreator) {
    entryEmbeddable.modify(modifiedCreator, entry);
    persistentDataEmbeddable.modify();
  }

  @Override
  public BlogEntryEntity copyWithoutId() {
    BlogEntryEntity blogEntryEntity = new BlogEntryEntity(this);
    blogEntryEntity.setId(null);

    return blogEntryEntity;
  }

  @Override
  public void modify() {
    persistentDataEmbeddable.modify();
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
  public String getCreatedBy() {
    return persistentDataEmbeddable.getCreatedBy();
  }

  @Override
  public LocalDateTime getTimeOfCreation() {
    return persistentDataEmbeddable.getTimeOfCreation();
  }

  @Override
  public String getModifiedBy() {
    return persistentDataEmbeddable.getModifiedBy();
  }

  @Override
  public LocalDateTime getTimeOfModification() {
    return persistentDataEmbeddable.getTimeOfModification();
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

  public String getEntry() {
    return entryEmbeddable.getEntry();
  }

  public static BlogEntryEntity aBlogEntry(BlogEntryDto blogEntryDto) {
    return new BlogEntryEntity(blogEntryDto);
  }
}
