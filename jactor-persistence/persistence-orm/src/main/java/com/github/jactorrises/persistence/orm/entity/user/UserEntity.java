package com.github.jactorrises.persistence.orm.entity.user;

import com.github.jactor.rises.client.dto.UserDto;
import com.github.jactorrises.persistence.orm.entity.PersistentEntity;
import com.github.jactorrises.persistence.orm.entity.blog.BlogEntity;
import com.github.jactorrises.persistence.orm.entity.guestbook.GuestBookEntity;
import com.github.jactorrises.persistence.orm.entity.person.PersonEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;

import static java.util.Objects.hash;

@Entity
@Table(name = "T_USER")
public class UserEntity extends PersistentEntity {

    @Column(name = "EMAIL") private String emailAddress;
    @Column(name = "USER_NAME", nullable = false) private String userName;
    @JoinColumn(name = "PERSON_ID") @OneToOne(cascade = CascadeType.ALL) private PersonEntity personEntity;
    @OneToOne(mappedBy = "user") private GuestBookEntity guestBook;
    @OneToOne(mappedBy = "userEntity") private BlogEntity blog;

    UserEntity() {
    }

    /**
     * @param user is used to create an entity
     */
    private UserEntity(UserEntity user) {
        super(user);
        blog = user.blog != null ? user.blog.copy() : null;
        guestBook = user.guestBook != null ? user.guestBook.copy() : null;
        emailAddress = user.emailAddress;
        personEntity = user.personEntity != null ? user.personEntity.copy() : null;
        userName = user.userName;
    }

    public UserEntity(UserDto user) {
        super(user);
        blog = user.getBlog() != null ? new BlogEntity(user.getBlog()) : null;
        guestBook = user.getGuestBook() != null ? new GuestBookEntity(user.getGuestBook()) : null;
        emailAddress = user.getEmailAddress();
        personEntity = new PersonEntity(user.getPerson());
        userName = user.getUserName();
    }

    public UserEntity copy() {
        return new UserEntity(this);
    }

    public UserDto asDto() {
        UserDto userDto = addPersistentData(new UserDto());
        userDto.setBlog(blog != null ? blog.asDto() : null);
        userDto.setGuestBook(guestBook != null ? guestBook.asDto() : null);
        userDto.setEmailAddress(emailAddress);
        userDto.setPerson(personEntity.asDto());
        userDto.setUserName(userName);

        return userDto;
    }

    @Override public boolean equals(Object o) {
        return o == this || o != null && getClass() == o.getClass() &&
                Objects.equals(emailAddress, ((UserEntity) o).emailAddress) &&
                Objects.equals(personEntity, ((UserEntity) o).personEntity) &&
                Objects.equals(userName, ((UserEntity) o).userName);
    }

    @Override public int hashCode() {
        return hash(userName, personEntity, emailAddress);
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .appendSuper(super.toString())
                .append(userName)
                .append(emailAddress)
                .append(blog)
                .append(guestBook)
                .append(personEntity)
                .toString();
    }

    public String getUserName() {
        return userName;
    }

    public PersonEntity getPerson() {
        return personEntity;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPersonEntity(PersonEntity personEntity) {
        this.personEntity = personEntity;
    }

    public static UserEntityBuilder aUser() {
        return new UserEntityBuilder();
    }
}
