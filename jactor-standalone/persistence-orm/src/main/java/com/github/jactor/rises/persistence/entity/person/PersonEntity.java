package com.github.jactor.rises.persistence.entity.person;

import com.github.jactor.rises.client.dto.NewPersonDto;
import com.github.jactor.rises.persistence.entity.PersistentEntity;
import com.github.jactor.rises.persistence.entity.address.AddressEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

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
//    @OneToOne(mappedBy = "personEntity", cascade = CascadeType.ALL) private UserEntity userEntity;

    PersonEntity() {
    }

    private PersonEntity(PersonEntity person) {
        super(person);
        addressEntity = person.copyAddress();
        description = person.description;
        firstName = person.firstName;
        surname = person.surname;
        locale = person.locale;
//        userEntity = person.copyUser();
    }

    public PersonEntity(NewPersonDto person) {
        super(person);
        addressEntity = person.getAddress() != null ? new AddressEntity(person.getAddress()) : null;
        description = person.getDescription();
        firstName = person.getFirstName();
        surname = person.getSurname();
        locale = person.getLocale();
//        userEntity = person.getUser() != null ? new UserEntity(person.getUser()) : null;
    }

    private AddressEntity copyAddress() {
        return addressEntity != null ? addressEntity.copy() : null;
    }

//    private UserEntity copyUser() {
//        return userEntity != null ? userEntity.copy() : null;
//    }

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
//        personDto.setUser(userEntity != null ? userEntity.asDto() : null);

        return personDto;
    }

    @Override public int hashCode() {
        return hash(addressEntity, description, firstName, surname, locale);
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString()).append(firstName).append(surname)
//                .append(userEntity)
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

//    public UserEntity getUserEntity() {
//        return userEntity;
//    }

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

//    public void setUserEntity(UserEntity userEntity) {
//        this.userEntity = userEntity;
//    }

        this.locale = locale;
    }

    public static PersonEntityBuilder aPerson() {
        return new PersonEntityBuilder();
    }
}
