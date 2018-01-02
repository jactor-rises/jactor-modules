package com.github.jactorrises.client.persistence.dto;

public class UserDto extends PersistentDto {
    private String emailAddress;
    private PersonDto person;
    private String userName;

    public UserDto() {
        // empty, use setters...
    }

    public UserDto(UserDto user) {
        super(user);
        person = user.getPerson();
        emailAddress = user.getEmailAddress();
        userName = user.getUserName();
    }

    public String getUserName() {
        return userName;
    }

    public PersonDto getPerson() {
        return person;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPerson(PersonDto person) {
        this.person = person;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
