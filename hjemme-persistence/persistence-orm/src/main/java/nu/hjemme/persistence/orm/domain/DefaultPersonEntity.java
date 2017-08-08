package nu.hjemme.persistence.orm.domain;

import nu.hjemme.client.datatype.Description;
import nu.hjemme.client.datatype.Name;
import nu.hjemme.client.domain.Person;
import nu.hjemme.persistence.client.AddressEntity;
import nu.hjemme.persistence.client.PersonEntity;
import nu.hjemme.persistence.client.UserEntity;
import nu.hjemme.persistence.orm.meta.PersonMetadata;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

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
public class DefaultPersonEntity extends DefaultPersistentEntity implements PersonEntity {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = PersonMetadata.ADDRESS_ID) private DefaultAddressEntity addressEntity;
    @Column(name = PersonMetadata.DESCRIPTION) private String description;
    @OneToOne(mappedBy = "personEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL) private DefaultUserEntity userEntity;
    @Transient private String firstName;
    @Transient private String lastName;
    @Transient private Locale locale;

    public DefaultPersonEntity() {
    }

    public DefaultPersonEntity(Person person) {
        addressEntity = person.getAddress() != null ? new DefaultAddressEntity(person.getAddress()) : null;
        description = convertFrom(person.getDescription(), Description.class);
        firstName = convertFrom(person.getFirstName(), Name.class);
        lastName = convertFrom(person.getLastName(), Name.class);
        userEntity = person.getUser() != null ? new DefaultUserEntity(person.getUser()) : null;
        locale = person.getLocale();
    }

    @Override public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() &&
                Objects.equals(getId(), ((DefaultPersonEntity) o).getId()) &&
                Objects.equals(addressEntity, ((DefaultPersonEntity) o).addressEntity) &&
                Objects.equals(description, ((DefaultPersonEntity) o).description) &&
                Objects.equals(firstName, ((DefaultPersonEntity) o).firstName) &&
                Objects.equals(lastName, ((DefaultPersonEntity) o).lastName) &&
                Objects.equals(userEntity, ((DefaultPersonEntity) o).userEntity) &&
                Objects.equals(locale, ((DefaultPersonEntity) o).locale);
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
        this.addressEntity = castOrInitializeCopyWith(addressEntity, DefaultAddressEntity.class);
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
        this.userEntity = castOrInitializeCopyWith(userEntity, DefaultUserEntity.class);
    }

    @Override public void setLocale(Locale locale) {
        this.locale = locale;
    }
}