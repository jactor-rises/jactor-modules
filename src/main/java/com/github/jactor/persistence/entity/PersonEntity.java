package com.github.jactor.persistence.entity;

import static java.util.Objects.hash;

import com.github.jactor.persistence.dto.PersonDto;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "T_PERSON")
public class PersonEntity implements PersistentEntity<PersonEntity> {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personSeq")
  @SequenceGenerator(name = "personSeq", sequenceName = "T_PERSON_SEQ", allocationSize = 1)
  private Long id;

  @Embedded
  @AttributeOverride(name = "createdBy", column = @Column(name = "CREATED_BY"))
  @AttributeOverride(name = "timeOfCreation", column = @Column(name = "CREATION_TIME"))
  @AttributeOverride(name = "modifiedBy", column = @Column(name = "UPDATED_BY"))
  @AttributeOverride(name = "timeOfModification", column = @Column(name = "UPDATED_TIME"))
  private PersistentDataEmbeddable persistentDataEmbeddable;

  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "FIRST_NAME")
  private String firstName;
  @Column(name = "LOCALE")
  private String locale;
  @Column(name = "SURNAME", nullable = false)
  private String surname;
  @JoinColumn(name = "ADDRESS_ID")
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private AddressEntity addressEntity;
  @OneToMany(mappedBy = "personEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
  private Set<UserEntity> users = new HashSet<>();

  @SuppressWarnings("unused")
  PersonEntity() {
    // used by entity manager
  }

  private PersonEntity(PersonEntity person) {
    addressEntity = person.addressEntity;
    description = person.description;
    firstName = person.firstName;
    locale = person.locale;
    id = person.id;
    persistentDataEmbeddable = new PersistentDataEmbeddable();
    surname = person.surname;
    users = person.users;
  }

  public PersonEntity(@NotNull PersonDto person) {
    addressEntity = Optional.ofNullable(person.getAddress()).map(AddressEntity::new).orElse(null);
    description = person.getDescription();
    firstName = person.getFirstName();
    locale = person.getLocale();
    id = person.getId();
    persistentDataEmbeddable = new PersistentDataEmbeddable(person.fetchPersistentDto());
    surname = person.getSurname();
  }

  public PersonDto asDto() {
    return new PersonDto(persistentDataEmbeddable.asPersistentDto(id), addressEntity.asDto(), locale, firstName, surname, description);
  }

  @Override
  public PersonEntity copyWithoutId() {
    PersonEntity personEntity = new PersonEntity(this);
    personEntity.setId(null);

    return personEntity;
  }

  @Override
  public void modify() {
    persistentDataEmbeddable.modify();
  }

  @Override
  public boolean equals(Object o) {
    return this == o || o != null && getClass() == o.getClass() &&
        Objects.equals(addressEntity, ((PersonEntity) o).getAddressEntity()) &&
        Objects.equals(description, ((PersonEntity) o).getDescription()) &&
        Objects.equals(firstName, ((PersonEntity) o).getFirstName()) &&
        Objects.equals(surname, ((PersonEntity) o).getSurname()) &&
        Objects.equals(locale, ((PersonEntity) o).getLocale());
  }

  @Override
  public int hashCode() {
    return hash(addressEntity, description, firstName, surname, locale);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
        .appendSuper(super.toString())
        .append(getFirstName())
        .append(getSurname())
        .append(getUsers())
        .append(getAddressEntity())
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

  public AddressEntity getAddressEntity() {
    return addressEntity;
  }

  public String getDescription() {
    return description;
  }

  public String getLocale() {
    return locale;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getSurname() {
    return surname;
  }

  public Set<UserEntity> getUsers() {
    return users;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  void addUser(UserEntity user) {
    users.add(user);
  }

  public static PersonEntity aPerson(PersonDto personDto) {
    return new PersonEntity(personDto);
  }
}
