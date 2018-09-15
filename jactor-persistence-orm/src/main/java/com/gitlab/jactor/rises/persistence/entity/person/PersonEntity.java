package com.gitlab.jactor.rises.persistence.entity.person;

import com.gitlab.jactor.rises.commons.dto.PersonDto;
import com.gitlab.jactor.rises.persistence.entity.PersistentEntity;
import com.gitlab.jactor.rises.persistence.entity.address.AddressEntity;
import com.gitlab.jactor.rises.persistence.entity.user.UserEntity;
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
import java.util.stream.Stream;

import static java.util.Objects.hash;

@Entity
@Table(name = "T_PERSON")
public class PersonEntity extends PersistentEntity<Long> {

    private @Id Long id;

    private @Column(name = "DESCRIPTION") String description;
    private @Column(name = "FIRST_NAME") String firstName;
    private @Column(name = "LOCALE") String locale;
    private @Column(name = "SURNAME", nullable = false) String surname;
    private @JoinColumn(name = "ADDRESS_ID") @ManyToOne(cascade = CascadeType.MERGE, optional = false) AddressEntity addressEntity;
    private @OneToOne(mappedBy = "personEntity", cascade = CascadeType.MERGE) UserEntity userEntity;

    PersonEntity() {
        // used by builder
    }

    private PersonEntity(PersonEntity person) {
        super(person);
        Optional.ofNullable(person.getAddressEntity()).ifPresent(ae -> addressEntity = ae.copy());
        description = person.description;
        firstName = person.firstName;
        surname = person.surname;
        locale = person.locale;
    }

    public PersonEntity(PersonDto person) {
        super(person);
        Optional.ofNullable(person.getAddress()).ifPresent(adto -> addressEntity = new AddressEntity(adto));
        description = person.getDescription();
        firstName = person.getFirstName();
        surname = person.getSurname();
        locale = person.getLocale();

    }

    public PersonDto asDto() {
        PersonDto personDto = addPersistentData(new PersonDto());
        personDto.setAddress(addressEntity.asDto());
        personDto.setDescription(description);
        personDto.setFirstName(firstName);
        personDto.setSurname(surname);
        personDto.setLocale(locale);

        return personDto;
    }

    public @Override PersonEntity copy() {
        return new PersonEntity(this);
    }

    public @Override Stream<Optional<PersistentEntity<Long>>> streamSequencedDependencies() {
        return streamSequencedDependencies(addressEntity, userEntity);
    }

    public @Override boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() &&
                Objects.equals(addressEntity, ((PersonEntity) o).getAddressEntity()) &&
                Objects.equals(description, ((PersonEntity) o).getDescription()) &&
                Objects.equals(firstName, ((PersonEntity) o).getFirstName()) &&
                Objects.equals(surname, ((PersonEntity) o).getSurname()) &&
                Objects.equals(locale, ((PersonEntity) o).getLocale());
    }

    public @Override int hashCode() {
        return hash(addressEntity, description, firstName, surname, locale);
    }

    public @Override String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .appendSuper(super.toString())
                .append(getFirstName())
                .append(getSurname())
                .append(getUserEntity())
                .append(getAddressEntity())
                .toString();
    }

    public @Override Long getId() {
        return id;
    }

    protected @Override void setId(Long id) {
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

    public @SuppressWarnings("WeakerAccess") /* used by reflection */ void setAddressEntity(AddressEntity addressEntity) {
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
