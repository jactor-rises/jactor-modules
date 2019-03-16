package com.github.jactor.persistence.entity;

import static java.util.Objects.hash;
import static java.util.stream.Collectors.toSet;

import com.github.jactor.persistence.dto.BlogDto;
import com.github.jactor.persistence.dto.PersistentDto;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "T_BLOG")
public class BlogEntity extends PersistentEntity {

  @Id
  private Long id;

  @Column(name = "CREATED")
  private LocalDate created;
  @Column(name = "TITLE")
  private String title;
  @JoinColumn(name = "USER_ID")
  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  private UserEntity userEntity;
  @OneToMany(mappedBy = "blog", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  private Set<BlogEntryEntity> entries = new HashSet<>();

  @SuppressWarnings("unused")
  BlogEntity() {
    // used by entity manager
  }

  private BlogEntity(BlogEntity blogEntity) {
    super(blogEntity);
    created = blogEntity.created;
    entries = blogEntity.entries.stream().map(BlogEntryEntity::copy).collect(toSet());
    title = blogEntity.getTitle();
    Optional.ofNullable(blogEntity.getUser()).ifPresent(user -> userEntity = user.copy());
  }

  public BlogEntity(@NotNull BlogDto blogDto) {
    super(blogDto.fetchPersistentDto());
    created = blogDto.getCreated();
    title = blogDto.getTitle();
    Optional.ofNullable(blogDto.getUser()).ifPresent(user -> userEntity = new UserEntity(user));
  }

  public BlogDto asDto() {
    return new BlogDto(
        initPersistentDto(),
        created, title, Optional.ofNullable(userEntity).map(UserEntity::asDto).orElse(null)
    );
  }

  public void add(BlogEntryEntity blogEntryEntity) {
    blogEntryEntity.setBlog(this);
    entries.add(blogEntryEntity);
  }

  @Override
  public BlogEntity copy() {
    return new BlogEntity(this);
  }

  @Override
  public PersistentDto initPersistentDto() {
    return new PersistentDto(getId(), getCreatedBy(), getCreationTime(), getUpdatedBy(), getUpdatedTime());
  }

  @Override
  public Stream<Optional<PersistentEntity>> streamSequencedDependencies() {
    return Stream.concat(streamSequencedDependencies(userEntity), entries.stream().map(Optional::ofNullable));
  }

  @Override
  public boolean equals(Object o) {
    return this == o || o != null && getClass() == o.getClass() &&
        Objects.equals(getTitle(), ((BlogEntity) o).getTitle()) &&
        Objects.equals(getUser(), ((BlogEntity) o).getUser());
  }

  @Override
  public int hashCode() {
    return hash(getTitle(), getUser());
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .appendSuper(super.toString())
        .append(getCreated())
        .append(getTitle())
        .append(getUser())
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

  public LocalDate getCreated() {
    return created;
  }

  public Set<BlogEntryEntity> getEntries() {
    return entries;
  }

  public String getTitle() {
    return title;
  }

  public UserEntity getUser() {
    return userEntity;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public static BlogEntity aBlog(BlogDto blogDto) {
    return new BlogEntity(blogDto);
  }
}
