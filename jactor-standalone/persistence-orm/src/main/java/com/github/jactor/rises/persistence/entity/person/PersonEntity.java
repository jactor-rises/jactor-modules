package com.github.jactor.rises.persistence.entity.person;

import com.github.jactor.rises.client.dto.NewPersonDto;
import com.github.jactor.rises.persistence.entity.PersistentEntity;
import com.github.jactor.rises.persistence.entity.address.AddressEntity;
import com.github.jactor.rises.persistence.entity.user.UserEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.hash;

@Entity
@Table(name = "T_PERSON")
public class PersonEntity extends PersistentEntity<Long> {

    @Id private Long id;

    @Column(name = "DESCRIPTION") private String description;
    @Column(name = "FIRST_NAME") private String firstName;
    @Column(name = "LOCALE") private String locale;
    @Column(name = "SURNAME", nullable = false) private String surname;
    @JoinColumn(name = "ADDRESS_ID") @ManyToOne(cascade = CascadeType.ALL, optional = false) private AddressEntity addressEntity;
    @OneToOne(mappedBy = "personEntity", cascade = CascadeType.ALL) private UserEntity userEntity;

    PersonEntity() {
    }

    private PersonEntity(PersonEntity person) {
        super(person);
        addressEntity = Optional.ofNullable(person.getAddressEntity())
                .map(AddressEntity::copy)
                .orElse(null);
        description = person.description;
        firstName = person.firstName;
        surname = person.surname;
        locale = person.locale;
        userEntity = Optional.ofNullable(person.getUserEntity())
                .map(UserEntity::copy)
                .orElse(null);
    }

    public PersonEntity(NewPersonDto person) {
        super(person);
        addressEntity = Optional.ofNullable(person.getAddress())
                .map(AddressEntity::new)
                .orElse(null);
        description = person.getDescription();
        firstName = person.getFirstName();
        surname = person.getSurname();
        locale = person.getLocale();
        userEntity = Optional.ofNullable(person.getUser())
                .map(UserEntity::new)
                .orElse(null);
    }

    public PersonEntity copy() {
        return new PersonEntity(this);
    }

    @Override public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() &&
                Objects.equals(addressEntity, ((PersonEntity) o).addressEntity) &&
                Objects.equals(description, ((PersonEntity) o).description) &&
                Objects.equals(firstName, ((PersonEntity) o).firstName) &&
                Objects.equals(surname, ((PersonEntity) o).surname) &&
                Objects.equals(locale, ((PersonEntity) o).locale);
    }

    public NewPersonDto asDto() {
        NewPersonDto personDto = addPersistentData(new NewPersonDto());
        personDto.setAddress(addressEntity.asDto());
        personDto.setDescription(description);
        personDto.setFirstName(firstName);
        personDto.setSurname(surname);
        personDto.setLocale(locale);
        personDto.setUser(
                Optional.ofNullable(userEntity)
                        .map(UserEntity::asDto)
                        .orElse(null)
        );

        return personDto;
    }

    @Override public int hashCode() {
        return hash(addressEntity, description, firstName, surname, locale);
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString()).append(firstName).append(surname)
                .append(userEntity)
                .append(addressEntity).toString();
    }

    @Override public Long getId() {
        return id;
    }

    @Override public void setId(Long id) {
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

    void setAddressEntity(AddressEntity addressEntity) {
        this.addressEntity = addressEntity;
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

    public static PersonEntityBuilder aPerson() {
        return new PersonEntityBuilder();
    }
}
