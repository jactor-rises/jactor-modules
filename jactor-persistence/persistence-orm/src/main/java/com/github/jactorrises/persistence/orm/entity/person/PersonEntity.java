package com.github.jactorrises.persistence.orm.entity.person;

import com.github.jactorrises.client.persistence.dto.PersonDto;
import com.github.jactorrises.persistence.orm.entity.PersistentEntity;
import com.github.jactorrises.persistence.orm.entity.address.AddressEntity;
import com.github.jactorrises.persistence.orm.entity.user.UserEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;

import static java.util.Objects.hash;

@Entity
@Table(name = "T_PERSON")
public class PersonEntity extends PersistentEntity {

    @Column(name = "DESCRIPTION") private String description;
    @Column(name = "FIRST_NAME") private String firstName;
    @Column(name = "LOCALE") private String locale;
    @Column(name = "SURNAME", nullable = false) private String surname;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ADDRESS_ID") private AddressEntity addressEntity;
    @OneToOne(mappedBy = "personEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL) private UserEntity userEntity;

    PersonEntity() {
    }

    private PersonEntity(PersonEntity person) {
        super(person);
        addressEntity = person.copyAddress();
        description = person.description;
        firstName = person.firstName;
        surname = person.surname;
        locale = person.locale;
        userEntity = person.copyUser();
    }

    public PersonEntity(PersonDto person) {
        super(person);
        addressEntity = person.getAddress() != null ? new AddressEntity(person.getAddress()) : null;
        description = person.getDescription();
        firstName = person.getFirstName();
        surname = person.getSurname();
        locale = person.getLocale();
        userEntity = person.getUser() != null ? new UserEntity(person.getUser()) : null;
    }

    private AddressEntity copyAddress() {
        return addressEntity != null ? addressEntity.copy() : null;
    }

    private UserEntity copyUser() {
        return userEntity != null ? userEntity.copy() : null;
    }

    public PersonEntity copy() {
        return new PersonEntity(this);
    }

    @Override public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() &&
                Objects.equals(addressEntity, ((PersonEntity) o).addressEntity) &&
                Objects.equals(description, ((PersonEntity) o).description) &&
                Objects.equals(getFirstName(), ((PersonEntity) o).firstName) &&
                Objects.equals(getSurname(), ((PersonEntity) o).surname) &&
                Objects.equals(getLocale(), ((PersonEntity) o).getLocale());
    }

    public PersonDto asDto() {
        PersonDto personDto = addPersistentData(new PersonDto());
        personDto.setAddress(addressEntity.asDto());
        personDto.setDescription(description);
        personDto.setFirstName(firstName);
        personDto.setSurname(surname);
        personDto.setLocale(locale);
        personDto.setUser(userEntity != null ? userEntity.asDto() : null);

        return personDto;
    }

    @Override public int hashCode() {
        return hash(addressEntity, description, firstName, surname, locale);
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString()).append(firstName).append(surname).append(userEntity).append(addressEntity).toString();
    }

    public AddressEntity getAddress() {
        return addressEntity;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getLocale() {
        return locale;
    }

    public String getDescription() {
        return description;
    }

    public UserEntity getUser() {
        return userEntity;
    }

    public void setAddressEntity(AddressEntity addressEntity) {
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

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public static PersonEntityBuilder aPerson() {
        return new PersonEntityBuilder();
    }
}
