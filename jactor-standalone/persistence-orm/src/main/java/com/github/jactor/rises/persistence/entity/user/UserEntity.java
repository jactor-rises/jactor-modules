package com.github.jactor.rises.persistence.entity.user;

import com.github.jactor.rises.client.dto.NewUserDto;
import com.github.jactor.rises.persistence.entity.PersistentEntity;
import com.github.jactor.rises.persistence.entity.blog.BlogEntity;
import com.github.jactor.rises.persistence.entity.person.PersonEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.hash;

@Entity
@Table(name = "T_USER")
public class UserEntity extends PersistentEntity<Long> {

    @Id private Long id;

    @Column(name = "EMAIL") private String emailAddress;
    @Column(name = "USER_NAME", nullable = false) private String userName;
    @JoinColumn(name = "PERSON_ID") @OneToOne(cascade = CascadeType.ALL) private PersonEntity personEntity;
    //    @OneToOne(mappedBy = "user") private GuestBookEntity guestBook;
    @OneToMany(mappedBy = "userEntity") private Set<BlogEntity> blogs = new HashSet<>();

    UserEntity() {
    }

    /**
     * @param user is used to create an entity
     */
    private UserEntity(UserEntity user) {
        super(user);
        blogs = user.getBlogs().stream().map(BlogEntity::copy).collect(Collectors.toSet());
//        guestBook = user.guestBook != null ? user.guestBook.copy() : null;
        emailAddress = user.emailAddress;
        personEntity = user.personEntity != null ? user.personEntity.copy() : null;
        userName = user.userName;
    }

    public UserEntity(NewUserDto user) {
        super(user);
        blogs = user.getBlogs().stream().map(BlogEntity::new).collect(Collectors.toSet());
//        guestBook = user.getGuestBook() != null ? new GuestBookEntity(user.getGuestBook()) : null;
        emailAddress = user.getEmailAddress();
        personEntity = new PersonEntity(user.getPerson());
        userName = user.getUserName();
    }

    public UserEntity copy() {
        return new UserEntity(this);
    }

    public NewUserDto asDto() {
        NewUserDto userDto = addPersistentData(new NewUserDto());
        blogs.forEach(blogEntity -> userDto.addBlog(blogEntity.asDto()));
//        userDto.setGuestBook(guestBook != null ? guestBook.asDto() : null);
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
                .append(blogs)
//                .append(guestBook)
                .append(personEntity)
                .toString();
    }

    @Override public Long getId() {
        return id;
    }

    @Override public void setId(Long id) {
        this.id = id;
    }

    public Set<BlogEntity> getBlogs() {
        return blogs;
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
