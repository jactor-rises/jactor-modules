package com.github.jactor.rises.persistence.entity.user;

import com.github.jactor.rises.client.dto.UserDto;
import com.github.jactor.rises.persistence.entity.PersistentEntity;
import com.github.jactor.rises.persistence.entity.blog.BlogEntity;
import com.github.jactor.rises.persistence.entity.guestbook.GuestBookEntity;
import com.github.jactor.rises.persistence.entity.person.PersonEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.hash;

@Entity
@Table(name = "T_USER")
public class UserEntity extends PersistentEntity<Long> {

    @Id private Long id;

    @Column(name = "EMAIL") private String emailAddress;
    @Column(name = "USER_NAME", nullable = false) private String userName;
    @JoinColumn(name = "PERSON_ID") @OneToOne(cascade = CascadeType.MERGE) private PersonEntity personEntity;
    @OneToOne(mappedBy = "user", cascade = CascadeType.MERGE, fetch = FetchType.LAZY) private GuestBookEntity guestBook;
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.MERGE, fetch = FetchType.LAZY) private Set<BlogEntity> blogs = new HashSet<>();

    UserEntity() {
        // used by builder
    }

    /**
     * @param user is used to create an entity
     */
    private UserEntity(UserEntity user) {
        super(user);
        blogs = user.blogs.stream().map(BlogEntity::copy).collect(Collectors.toSet());
        Optional.ofNullable(user.guestBook).ifPresent(gb -> guestBook = gb.copy());
        guestBook = user.guestBook != null ? user.guestBook.copy() : null;
        emailAddress = user.emailAddress;
        personEntity = user.personEntity != null ? user.personEntity.copy() : null;
        userName = user.userName;
    }

    public UserEntity(UserDto user) {
        super(user);
        blogs = user.getBlogs().stream().map(BlogEntity::new).collect(Collectors.toSet());
        Optional.ofNullable(user.getGuestBook()).ifPresent(gb -> guestBook = new GuestBookEntity(gb));
        guestBook = user.getGuestBook() != null ? new GuestBookEntity(user.getGuestBook()) : null;
        emailAddress = user.getEmailAddress();
        Optional.ofNullable(user.getPerson()).ifPresent(personDto -> personEntity = new PersonEntity(personDto));
        userName = user.getUserName();
    }

    public UserEntity copy() {
        return new UserEntity(this);
    }

    public UserDto asDto() {
        UserDto userDto = addPersistentData(new UserDto());
        blogs.forEach(blogEntity -> userDto.addBlog(blogEntity.asDto()));
        Optional.ofNullable(guestBook).ifPresent(gb -> userDto.setGuestBook(gb.asDto()));
        userDto.setEmailAddress(emailAddress);
        Optional.ofNullable(personEntity).ifPresent(pen -> userDto.setPerson(pen.asDto()));
        userDto.setUserName(userName);

        return userDto;
    }

    @Override public void addSequencedIdAlsoIncludingDependencies(Sequencer sequencer) {
        id = fetchId(sequencer);
        addSequencedIdToDependencies(personEntity, sequencer);
        addSequencedIdToDependencies(guestBook, sequencer);
        blogs.forEach(blogEntity -> addSequencedIdToDependencies(blogEntity, sequencer));
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
                .append("guestbook.id=" + (guestBook != null ? guestBook.getId() : null))
                .append(personEntity)
                .toString();
    }

    @Override public Long getId() {
        return id;
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

    public void setGuestBook(GuestBookEntity guestBook) {
        this.guestBook = guestBook;
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
