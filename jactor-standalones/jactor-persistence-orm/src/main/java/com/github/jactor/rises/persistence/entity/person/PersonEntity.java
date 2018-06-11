package com.github.jactor.rises.persistence.entity.person;

import com.github.jactor.rises.client.dto.PersonDto;
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
    @JoinColumn(name = "ADDRESS_ID") @ManyToOne(cascade = CascadeType.MERGE, optional = false) private AddressEntity addressEntity;
    @OneToOne(mappedBy = "personEntity", cascade = CascadeType.MERGE) private UserEntity userEntity;

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
        Optional.ofNullable(person.getUser()).ifPresent(udto -> userEntity = new UserEntity(udto));

    }

    public PersonDto asDto() {
        PersonDto personDto = addPersistentData(new PersonDto());
        personDto.setAddress(addressEntity.asDto());
        personDto.setDescription(description);
        personDto.setFirstName(firstName);
        personDto.setSurname(surname);
        personDto.setLocale(locale);
        Optional.ofNullable(userEntity).map(UserEntity::asDto).ifPresent(personDto::setUser);

        return personDto;
    }

    @Override public PersonEntity copy() {
        return new PersonEntity(this);
    }

    @Override public void addSequencedIdAlsoIncludingDependencies(Sequencer sequencer) {
        id = fetchId(sequencer);
        addSequencedIdToDependencies(addressEntity, sequencer);
        addSequencedIdToDependencies(userEntity, sequencer);
    }

    @Override public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() &&
                Objects.equals(addressEntity, ((PersonEntity) o).addressEntity) &&
                Objects.equals(description, ((PersonEntity) o).description) &&
                Objects.equals(firstName, ((PersonEntity) o).firstName) &&
                Objects.equals(surname, ((PersonEntity) o).surname) &&
                Objects.equals(locale, ((PersonEntity) o).locale);
    }

    @Override public int hashCode() {
        return hash(addressEntity, description, firstName, surname, locale);
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString()).append(firstName).append(surname)
                .append("userEntity", userEntity)
                .append("addressEntity", addressEntity)
                .toString();
    }

    @Override public Long getId() {
        return id;
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

    @SuppressWarnings("WeakerAccess") /* used by reflection */ public void setAddressEntity(AddressEntity addressEntity) {
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
