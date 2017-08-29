package com.github.jactorrises.persistence.boot.entity.person;

import com.github.jactorrises.client.datatype.Description;
import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.Person;
import com.github.jactorrises.persistence.boot.entity.PersistentEntity;
import com.github.jactorrises.persistence.boot.entity.address.AddressEntityImpl;
import com.github.jactorrises.persistence.boot.entity.user.UserEntityImpl;
import com.github.jactorrises.persistence.client.AddressEntity;
import com.github.jactorrises.persistence.client.PersonEntity;
import com.github.jactorrises.persistence.client.UserEntity;
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
@Table(name = PersonMetadata.PERSON_TABLE)
public class PersonEntityImpl extends PersistentEntity implements PersonEntity {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = PersonMetadata.ADDRESS_ID) private AddressEntityImpl addressEntity;
    @Column(name = PersonMetadata.DESCRIPTION) private String description;
    @OneToOne(mappedBy = "personEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL) private UserEntityImpl userEntity;
    @Transient private String firstName;
    @Transient private String lastName;
    @Transient private Locale locale;

    public PersonEntityImpl() {
    }

    public PersonEntityImpl(Person person) {
        addressEntity = person.getAddress() != null ? new AddressEntityImpl(person.getAddress()) : null;
        description = convertFrom(person.getDescription(), Description.class);
        firstName = convertFrom(person.getFirstName(), Name.class);
        lastName = convertFrom(person.getLastName(), Name.class);
        userEntity = person.getUser() != null ? new UserEntityImpl(person.getUser()) : null;
        locale = person.getLocale();
    }

    @Override public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() &&
                Objects.equals(addressEntity, ((PersonEntityImpl) o).addressEntity) &&
                Objects.equals(description, ((PersonEntityImpl) o).description) &&
                Objects.equals(firstName, ((PersonEntityImpl) o).firstName) &&
                Objects.equals(lastName, ((PersonEntityImpl) o).lastName) &&
                Objects.equals(userEntity, ((PersonEntityImpl) o).userEntity) &&
                Objects.equals(locale, ((PersonEntityImpl) o).locale);
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
        return convertTo(firstName, Name.class);
    }

    @Override public Name getLastName() {
        return convertTo(lastName, Name.class);
    }

    @Override public Locale getLocale() {
        return locale;
    }

    @Override public Description getDescription() {
        return convertTo(description, Description.class);
    }

    @Override public UserEntity getUser() {
        return userEntity;
    }

    @Override public void setAddressEntity(AddressEntity addressEntity) {
        this.addressEntity = castOrInitializeCopyWith(addressEntity, AddressEntityImpl.class);
    }

    @Override public void setDescription(String description) {
        this.description = description;
    }

    @Override public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override public void setUserEntity(UserEntity userEntity) {
        this.userEntity = castOrInitializeCopyWith(userEntity, UserEntityImpl.class);
    }

    @Override public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
