package com.github.jactorrises.persistence.boot.entity.user;

import com.github.jactorrises.client.datatype.EmailAddress;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.User;
import com.github.jactorrises.persistence.boot.entity.PersistentEntity;
import com.github.jactorrises.persistence.boot.entity.person.PersonEntityImpl;
import com.github.jactorrises.persistence.client.PersonEntity;
import com.github.jactorrises.persistence.client.UserEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;

import static java.util.Objects.hash;

@Entity
@Table(name = UserMetadata.USER_TABLE)
public class UserEntityImpl extends PersistentEntity implements UserEntity {

    @Column(name = UserMetadata.PASSWORD, nullable = false) private String password; // the user password
    @Column(name = UserMetadata.USER_NAME, nullable = false) private String userName; // the user name
    @JoinColumn(name = UserMetadata.PERSON_ID) @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) private PersonEntityImpl personEntity; // the user as a person
    @Column(name = UserMetadata.EMAIL) private String emailAddress; // the email address to the user

    public UserEntityImpl() { }

    /** @param user is used to create an entity */
    public UserEntityImpl(User user) {
        password = user.getPassword();
        userName = convertFrom(user.getUserName(), UserName.class);
        personEntity = castOrInitializeCopyWith(user.getPerson(), PersonEntityImpl.class);
        emailAddress = convertFrom(user.getEmailAddress(), EmailAddress.class);
    }

    @Override public boolean equals(Object o) {
        return o == this || o != null && getClass() == o.getClass() &&
                Objects.equals(userName, ((UserEntityImpl) o).userName) &&
                Objects.equals(personEntity, ((UserEntityImpl) o).personEntity) &&
                Objects.equals(emailAddress, ((UserEntityImpl) o).emailAddress);
    }

    @Override public int hashCode() {
        return hash(userName, personEntity, emailAddress);
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .appendSuper(super.toString())
                .append(userName)
                .append(personEntity)
                .append(emailAddress)
                .toString();
    }

    @Override public String getPassword() {
        return password;
    }

    @Override public UserName getUserName() {
        return convertTo(userName, UserName.class);
    }

    @Override public PersonEntity getPerson() {
        return personEntity;
    }

    @Override public EmailAddress getEmailAddress() {
        return convertTo(emailAddress, EmailAddress.class);
    }

    @Override public boolean isUserNameEmailAddress() {
        return userName.endsWith(emailAddress);
    }

    @Override public void setPassword(String password) {
        this.password = password;
    }

    @Override public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override public void setPersonEntity(PersonEntity personEntity) {
        this.personEntity = personEntity != null ? castOrInitializeCopyWith(personEntity, PersonEntityImpl.class) : null;
    }

    public static UserEntityBuilder aUser() {
        return new UserEntityBuilder();
    }
}
