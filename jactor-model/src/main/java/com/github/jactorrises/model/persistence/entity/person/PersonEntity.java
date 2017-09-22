package com.github.jactorrises.model.persistence.entity.person;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.Person;
import com.github.jactorrises.model.persistence.entity.NameEmbeddable;
import com.github.jactorrises.model.persistence.entity.PersistentEntity;
import com.github.jactorrises.model.persistence.entity.address.AddressEntity;
import com.github.jactorrises.model.persistence.entity.user.UserEntity;
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
import javax.persistence.Transient;
import java.util.Locale;
import java.util.Objects;

import static java.util.Objects.hash;

@Entity
@Table(name = "T_PERSON")
public class PersonEntity extends PersistentEntity implements Person {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ADDRESS_ID") private AddressEntity addressEntity;
    @Column(name = "DESCRIPTION") private String description;
    @OneToOne(mappedBy = "personEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL) private UserEntity userEntity;
    @Transient private NameEmbeddable firstName;
    @Transient private NameEmbeddable lastName;
    @Transient private Locale locale;

    PersonEntity() {
    }

    PersonEntity(PersonEntity person) {
        super(person);
        addressEntity = person.copyAddress();
        description = person.description;
        firstName = person.firstName;
        lastName = person.lastName;
        locale = person.getLocale();
        userEntity = person.copyUser();
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
                Objects.equals(firstName, ((PersonEntity) o).firstName) &&
                Objects.equals(lastName, ((PersonEntity) o).lastName) &&
                Objects.equals(userEntity, ((PersonEntity) o).userEntity) &&
                Objects.equals(locale, ((PersonEntity) o).locale);
    }

    @Override public int hashCode() {
        return hash(addressEntity, description, firstName, lastName, userEntity, locale);
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString()).append(firstName).append(lastName).append(userEntity).append(addressEntity).toString();
    }

    @Override public AddressEntity getAddress() {
        return addressEntity;
    }

    @Override public Name getFirstName() {
        return firstName.fetchName();
    }

    @Override public Name getLastName() {
        return lastName.fetchName();
    }

    @Override public Locale getLocale() {
        return locale;
    }

    @Override public String getDescription() {
        return description;
    }

    @Override public UserEntity getUser() {
        return userEntity;
    }

    public void setAddressEntity(AddressEntity addressEntity) {
        this.addressEntity = addressEntity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    void setFirstName(Name firstName) {
        this.firstName = new NameEmbeddable(firstName);
    }

    void setLastName(Name lastName) {
        this.lastName = new NameEmbeddable(lastName);
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    void setLocale(Locale locale) {
        this.locale = locale;
    }

    public static PersonEntityBuilder aPerson() {
        return new PersonEntityBuilder();
    }
}
