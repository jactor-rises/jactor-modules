package com.github.jactorrises.model.business.persistence.entity.person;

import com.github.jactorrises.client.datatype.Description;
import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.Person;
import com.github.jactorrises.model.business.persistence.entity.PersistentEntity;
import com.github.jactorrises.model.business.persistence.entity.address.AddressEntity;
import com.github.jactorrises.model.business.persistence.entity.user.UserEntity;
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
public class PersonEntity extends PersistentEntity implements Person {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = PersonMetadata.ADDRESS_ID) private AddressEntity addressEntity;
    @Column(name = PersonMetadata.DESCRIPTION) private String description;
    @OneToOne(mappedBy = "personEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL) private UserEntity userEntity;
    @Transient private String firstName;
    @Transient private String lastName;
    @Transient private Locale locale;

    public PersonEntity() {
    }

    public PersonEntity(PersonEntity person) {
        addressEntity = person.getAddress() != null ? new AddressEntity(person.getAddress()) : null;
        description = person.description;
        firstName = person.firstName;
        lastName = person.lastName;
        locale = person.getLocale();
        userEntity = person.userEntity != null ? new UserEntity(person.userEntity) : null;
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

    public void setAddressEntity(AddressEntity addressEntity) {
        this.addressEntity = addressEntity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
