package com.github.jactorrises.client.persistence.dto;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.Person;

import java.util.Locale;

public class PersonDto extends PersistentDto implements Person {
    private AddressDto address;
    private Locale locale;
    private Name firstName;
    private Name surname;
    private String description;
    private UserDto user;

    public PersonDto() {
        // empty, use setters
    }

    PersonDto(PersonDto person) {
        super(person);
        address = person.getAddress();
        locale = person.getLocale();
        firstName = person.getFirstName();
        surname = person.getSurname();
        description = person.getDescription();
        user = person.getUser();
    }

    @Override public String getDescription() {
        return description;
    }

    @Override public UserDto getUser() {
        return user;
    }

    @Override public AddressDto getAddress() {
        return address;
    }

    @Override public Name getFirstName() {
        return firstName;
    }

    @Override public Name getSurname() {
        return surname;
    }

    @Override public Locale getLocale() {
        return locale;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setFirstName(Name firstName) {
        this.firstName = firstName;
    }

    public void setSurname(Name surname) {
        this.surname = surname;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
