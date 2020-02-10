package com.github.jactor.persistence.entity;

import static java.util.Objects.hash;

import com.github.jactor.persistence.dto.UserInternalDto;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "T_USER")
public class UserEntity implements PersistentEntity<UserEntity> {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeq")
  @SequenceGenerator(name = "userSeq", sequenceName = "T_USER_SEQ", allocationSize = 1)
  private Long id;

  @Embedded
  @AttributeOverride(name = "createdBy", column = @Column(name = "CREATED_BY"))
  @AttributeOverride(name = "timeOfCreation", column = @Column(name = "CREATION_TIME"))
  @AttributeOverride(name = "modifiedBy", column = @Column(name = "UPDATED_BY"))
  @AttributeOverride(name = "timeOfModification", column = @Column(name = "UPDATED_TIME"))
  private PersistentDataEmbeddable persistentDataEmbeddable;

  @Column(name = "EMAIL")
  private String emailAddress;
  @Column(name = "USER_NAME", nullable = false)
  private String username;
  @JoinColumn(name = "PERSON_ID")
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private PersonEntity personEntity;
  @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
  private GuestBookEntity guestBook;
  @OneToMany(mappedBy = "userEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
  private Set<BlogEntity> blogs = new HashSet<>();
  @Column(name = "USER_TYPE")
  @Enumerated(EnumType.STRING)
  private UserType userType;

  @SuppressWarnings("unused")
  UserEntity() {
    // used by entity manager
  }

  /**
   * @param user is used to create an entity
   */
  private UserEntity(UserEntity user) {
    blogs = user.blogs.stream().map(BlogEntity::copyWithoutId).collect(Collectors.toSet());
    emailAddress = user.emailAddress;
    guestBook = Optional.ofNullable(user.guestBook).map(GuestBookEntity::copyWithoutId).orElse(null);
    id = user.id;
    persistentDataEmbeddable = new PersistentDataEmbeddable();
    personEntity = Optional.ofNullable(user.personEntity).map(PersonEntity::copyWithoutId).orElse(null);
    username = user.username;
    userType = user.userType;
  }

  public UserEntity(@NotNull UserInternalDto user) {
    emailAddress = user.getEmailAddress();
    id = user.getId();
    persistentDataEmbeddable = new PersistentDataEmbeddable(user.fetchPersistentDto());
    personEntity = Optional.ofNullable(user.getPersonInternal()).map(PersonEntity::new).orElse(null);
    username = user.getUsername();
    userType = Arrays.stream(UserType.values())
        .filter(aUserType -> aUserType.name().equals(user.getUsertype().name()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Unknown UserType: " + user.getUsertype()));
  }

  public UserInternalDto asDto() {
    return new UserInternalDto(
        persistentDataEmbeddable.asPersistentDto(id),
        Optional.ofNullable(personEntity).map(PersonEntity::asDto).orElse(null),
        emailAddress,
        username
    );
  }

  public PersonEntity fetchPerson() {
    personEntity.addUser(this);
    return personEntity;
  }

  @Override
  public UserEntity copyWithoutId() {
    UserEntity userEntity = new UserEntity(this);
    userEntity.setId(null);

    return userEntity;
  }

  @Override
  public void modify() {
    persistentDataEmbeddable.modify();
  }

  public void add(BlogEntity blogEntity) {
    blogs.add(blogEntity);
    blogEntity.setUser(this);
  }

  @Override
  public boolean equals(Object o) {
    return o == this || o != null && getClass() == o.getClass() &&
        Objects.equals(emailAddress, ((UserEntity) o).getEmailAddress()) &&
        Objects.equals(personEntity, ((UserEntity) o).getPerson()) &&
        Objects.equals(username, ((UserEntity) o).getUsername());
  }

  @Override
  public int hashCode() {
    return hash(username, personEntity, emailAddress);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
        .appendSuper(super.toString())
        .append(getUsername())
        .append(getEmailAddress())
        .append(blogs)
        .append("guestbook.id=" + (guestBook != null ? guestBook.getId() : null))
        .append(getPerson())
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

  public Set<BlogEntity> getBlogs() {
    return Collections.unmodifiableSet(blogs);
  }

  public GuestBookEntity getGuestBook() {
    return guestBook;
  }

  public void setGuestBook(GuestBookEntity guestBook) {
    this.guestBook = guestBook;
    guestBook.setUser(this);
  }

  public String getUsername() {
    return username;
  }

  public PersonEntity getPerson() {
    return personEntity;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPersonEntity(PersonEntity personEntity) {
    this.personEntity = personEntity;
  }

  public static UserEntity aUser(UserInternalDto userInternalDto) {
    return new UserEntity(userInternalDto);
  }

  public enum UserType {
    ADMIN, ACTIVE, INACTIVE
  }
}
