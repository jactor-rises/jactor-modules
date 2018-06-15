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
    @Column(name = "USER_NAME", nullable = false) private String username;
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
        username = user.username;
    }

    public UserEntity(UserDto user) {
        super(user);
        emailAddress = user.getEmailAddress();
        Optional.ofNullable(user.getPerson()).ifPresent(personDto -> personEntity = new PersonEntity(personDto));
        username = user.getUsername();
    }

    public UserEntity copy() {
        return new UserEntity(this);
    }

    public UserDto asDto() {
        UserDto userDto = addPersistentData(new UserDto());
        userDto.setEmailAddress(emailAddress);
        Optional.ofNullable(personEntity).ifPresent(pen -> userDto.setPerson(pen.asDto()));
        userDto.setUsername(username);

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
                Objects.equals(username, ((UserEntity) o).username);
    }

    @Override public int hashCode() {
        return hash(username, personEntity, emailAddress);
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .appendSuper(super.toString())
                .append(username)
                .append(emailAddress)
                .append(blogs)
                .append("guestbook.id=" + (guestBook != null ? guestBook.getId() : null))
                .append(personEntity)
                .toString();
    }

    @Override public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPersonEntity(PersonEntity personEntity) {
        this.personEntity = personEntity;
    }

    public static UserEntityBuilder aUser() {
        return new UserEntityBuilder();
    }
}
