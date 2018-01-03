package com.github.jactorrises.client.persistence.dto;

public class UserDto extends PersistentDto {
    private BlogDto blog;
    private GuestBookDto guestBook;
    private PersonDto person;
    private String emailAddress;
    private String userName;

    public UserDto() {
        // empty, use setters...
    }

    public UserDto(UserDto user) {
        super(user);
        blog = user.getBlog();
        guestBook = user.getGuestBook();
        emailAddress = user.getEmailAddress();
        person = user.getPerson();
        userName = user.getUserName();
    }

    public BlogDto getBlog() {
        return blog;
    }

    public GuestBookDto getGuestBook() {
        return guestBook;
    }

    public PersonDto getPerson() {
        return person;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setBlog(BlogDto blog) {
        this.blog = blog;
    }

    public void setGuestBook(GuestBookDto guestBook) {
        this.guestBook = guestBook;
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
