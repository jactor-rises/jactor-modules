package com.github.jactor.persistence.entity;

import static java.util.Objects.hash;

import com.github.jactor.persistence.dto.PersistentDto;
import com.github.jactor.persistence.dto.PersonDto;
import java.time.LocalDateTime;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "T_PERSON")
public class PersonEntity implements PersistentEntity<PersonEntity> {

  @Id
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
  @ManyToOne(cascade = CascadeType.MERGE, optional = false)
  private AddressEntity addressEntity;
  @OneToOne(mappedBy = "personEntity", cascade = CascadeType.MERGE)
  private UserEntity userEntity;

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
    userEntity = person.userEntity;
  }

  PersonEntity(@NotNull PersonDto person) {
    addressEntity = Optional.ofNullable(person.getAddress()).map(AddressEntity::new).orElse(null);
    description = person.getDescription();
    firstName = person.getFirstName();
    locale = person.getLocale();
    id = person.getId();
    persistentDataEmbeddable = new PersistentDataEmbeddable(person.fetchPersistentDto());
    surname = person.getSurname();
  }

  public PersonDto asDto() {
    return new PersonDto(initPersistentDto(), addressEntity.asDto(), locale, firstName, surname, description);
  }

  @Override
  public PersonEntity copyWithoutId() {
    PersonEntity personEntity = new PersonEntity(this);
    personEntity.setId(null);

    return personEntity;
  }

  @Override
  public PersistentDto initPersistentDto() {
    return new PersistentDto(getId(), getCreatedBy(), getTimeOfCreation(), getModifiedBy(), getTimeOfModification());
  }

  @Override
  public void modify() {
    persistentDataEmbeddable.modify();
  }

  @Override
  public Stream<PersistentEntity> streamSequencedDependencies() {
    return streamSequencedDependencies(addressEntity, userEntity);
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
        .append(getUserEntity())
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

  public UserEntity getUserEntity() {
    return userEntity;
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

  public void setUserEntity(UserEntity userEntity) {
    this.userEntity = userEntity;
  }

  public static PersonEntity aPerson(PersonDto personDto) {
    return new PersonEntity(personDto);
  }
}
