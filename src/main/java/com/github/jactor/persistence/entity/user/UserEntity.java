package com.github.jactor.persistence.entity.user;

import static java.util.Objects.hash;

import com.github.jactor.persistence.dto.UserDto;
import com.github.jactor.persistence.entity.PersistentEntity;
import com.github.jactor.persistence.entity.blog.BlogEntity;
import com.github.jactor.persistence.entity.guestbook.GuestBookEntity;
import com.github.jactor.persistence.entity.person.PersonEntity;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "T_USER")
public class UserEntity extends PersistentEntity {

  @Id
  private Long id;

  @Column(name = "EMAIL")
  private String emailAddress;
  @Column(name = "USER_NAME", nullable = false)
  private String username;
  @JoinColumn(name = "PERSON_ID")
  @OneToOne(cascade = CascadeType.MERGE)
  private PersonEntity personEntity;
  @OneToOne(mappedBy = "user", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  private GuestBookEntity guestBook;
  @OneToMany(mappedBy = "userEntity", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  private Set<BlogEntity> blogs = new HashSet<>();
  @Column(name = "USER_TYPE")
  @Enumerated(EnumType.STRING)
  private UserType userType;

  UserEntity() {
    // used by builder
  }

  /**
   * @param user is used to create an entity
   */
  private UserEntity(UserEntity user) {
    super(user);
    blogs = user.blogs.stream().map(BlogEntity::copy).collect(Collectors.toSet());
    emailAddress = user.emailAddress;
    guestBook = Optional.ofNullable(user.guestBook).map(GuestBookEntity::copy).orElse(null);
    userType = user.userType;
    personEntity = Optional.ofNullable(user.personEntity).map(PersonEntity::copy).orElse(null);
    username = user.username;
  }

  public UserEntity(@NotNull UserDto user) {
    super(user.fetchPersistentDto());
    emailAddress = user.getEmailAddress();
    personEntity = Optional.ofNullable(user.getPerson()).map(PersonEntity::new).orElse(null);
    username = user.getUsername();
    userType = Arrays.stream(UserType.values())
        .filter(aUserType -> aUserType.name().equals(user.getUserType().name()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Unknown UserType: " + user.getUserType().name()));
  }

  public UserDto asDto() {
    return new UserDto(
        initPersistentDto(),
        Optional.ofNullable(personEntity).map(PersonEntity::asDto).orElse(null),
        emailAddress,
        username
    );
  }

  public PersonEntity fetchPerson() {
    personEntity.setUserEntity(this);
    return personEntity;
  }

  @Override
  public UserEntity copy() {
    return new UserEntity(this);
  }

  @Override
  protected Stream<Optional<PersistentEntity>> streamSequencedDependencies() {
    return Stream.concat(streamSequencedDependencies(personEntity, guestBook), blogs.stream().map(Optional::of));
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
  public Long getId() {
    return id;
  }

  @Override
  protected void setId(Long id) {
    this.id = id;
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

  public @SuppressWarnings("unused") /* used by reflection */ void setGuestBook(GuestBookEntity guestBook) {
    this.guestBook = guestBook;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPersonEntity(PersonEntity personEntity) {
    this.personEntity = personEntity;
  }

  void setUserType(UserType userType) {
    this.userType = userType;
  }

  public static UserEntity aUser(UserDto userDto) {
    return new UserEntity(userDto);
  }

  public enum UserType {
    ADMIN, ACTIVE, INACTIVE
  }
}
