package com.github.jactorrises.persistence.client.dto;

import com.github.jactorrises.client.datatype.EmailAddress;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.User;

public class UserDto extends PersistentDto implements User {
    private EmailAddress emailAddress;
    private PersonDto person;
    private UserName userName;

    public UserDto() {
        // empty, use setters...
    }

    public UserDto(UserDto user) {
        super(user);
        person = user.getPerson();
        emailAddress = user.getEmailAddress();
        userName = user.getUserName();
    }

    @Override public UserName getUserName() {
        return userName;
    }

    @Override public PersonDto getPerson() {
        return person;
    }

    @Override public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    @Override public boolean isUserNameEmailAddress() {
        return emailAddress.isSameAs(userName);
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPerson(PersonDto person) {
        this.person = person;
    }

    public void setUserName(UserName userName) {
        this.userName = userName;
    }
}
