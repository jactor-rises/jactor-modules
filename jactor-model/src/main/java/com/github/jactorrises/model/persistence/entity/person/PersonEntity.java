package com.github.jactorrises.model.persistence.entity.person;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.Person;
import com.github.jactorrises.model.persistence.entity.NameEmbeddable;
import com.github.jactorrises.model.persistence.entity.PersistentEntity;
import com.github.jactorrises.model.persistence.entity.address.AddressOrm;
import com.github.jactorrises.model.persistence.entity.user.UserEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Locale;
import java.util.Objects;

import static java.util.Objects.hash;

@Entity
@Table(name = "T_PERSON")
public class PersonEntity extends PersistentEntity implements Person {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ADDRESS_ID") private AddressOrm addressEntity;
    @Column(name = "DESCRIPTION") private String description;
    @OneToOne(mappedBy = "personEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL) private UserEntity userEntity;
    @Embedded @AttributeOverride(name = "name", column = @Column(name = "FIRST_NAME")) private NameEmbeddable firstName;
    @Embedded @AttributeOverride(name = "name", column = @Column(name = "SURNAME", nullable = false)) private NameEmbeddable surname;
    @Embedded @AttributeOverride(name = "locale", column = @Column(name = "LOCALE")) private LocaleEmbeddable locale;

    PersonEntity() {
    }

    PersonEntity(PersonEntity person) {
        super(person);
        addressEntity = person.copyAddress();
        description = person.description;
        firstName = person.firstName;
        surname = person.surname;
        locale = person.locale;
        userEntity = person.copyUser();
    }

    private AddressOrm copyAddress() {
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
                Objects.equals(getFirstName(), fetchName(((PersonEntity) o).firstName)) &&
                Objects.equals(getSurname(), fetchName(((PersonEntity) o).surname)) &&
                Objects.equals(getLocale(), fetchLocale((PersonEntity) o));
    }

    private Name fetchName(NameEmbeddable nameEmbeddable) {
        return nameEmbeddable != null ? nameEmbeddable.fetchName() : null;
    }

    private Locale fetchLocale(PersonEntity personEntity) {
        return personEntity != null ? personEntity.getLocale() : null;
    }

    @Override public int hashCode() {
        return hash(addressEntity, description, firstName, surname, locale);
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString()).append(firstName).append(surname).append(userEntity).append(addressEntity).toString();
    }

    @Override public AddressOrm getAddress() {
        return addressEntity;
    }

    @Override public Name getFirstName() {
        return firstName != null ? firstName.fetchName() : null;
    }

    @Override public Name getSurname() {
        return surname != null ? surname.fetchName() : null;
    }

    @Override public Locale getLocale() {
        return locale != null ? locale.fetchLocale() : null;
    }

    @Override public String getDescription() {
        return description;
    }

    @Override public UserEntity getUser() {
        return userEntity;
    }

    void setAddressEntity(AddressOrm addressEntity) {
        this.addressEntity = addressEntity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    void setFirstName(Name firstName) {
        this.firstName = new NameEmbeddable(firstName);
    }

    void setSurname(Name surname) {
        this.surname = new NameEmbeddable(surname);
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    void setLocale(Locale locale) {
        this.locale = new LocaleEmbeddable(locale);
    }

    public static PersonEntityBuilder aPerson() {
        return new PersonEntityBuilder();
    }
}
