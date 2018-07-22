package com.gitlab.jactor.rises.persistence.entity.user;

import com.gitlab.jactor.rises.commons.dto.UserDto;
import com.gitlab.jactor.rises.persistence.entity.PersistentEntity;
import com.gitlab.jactor.rises.persistence.entity.blog.BlogEntity;
import com.gitlab.jactor.rises.persistence.entity.guestbook.GuestBookEntity;
import com.gitlab.jactor.rises.persistence.entity.person.PersonEntity;
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
import java.util.stream.Stream;

import static java.util.Objects.hash;

@Entity
@Table(name = "T_USER")
public class UserEntity extends PersistentEntity<Long> {

    private @Id Long id;

    private @Column(name = "EMAIL") String emailAddress;
    private @Column(name = "USER_NAME", nullable = false) String username;
    private @JoinColumn(name = "PERSON_ID") @OneToOne(cascade = CascadeType.MERGE) PersonEntity personEntity;
    private @OneToOne(mappedBy = "user", cascade = CascadeType.MERGE, fetch = FetchType.LAZY) GuestBookEntity guestBook;
    private @OneToMany(mappedBy = "userEntity", cascade = CascadeType.MERGE, fetch = FetchType.LAZY) Set<BlogEntity> blogs = new HashSet<>();
    private @Column(name = "INACTIVE") boolean inactive;

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
        emailAddress = user.emailAddress;
        Optional.ofNullable(user.personEntity).ifPresent(pen -> personEntity = pen.copy());
        username = user.username;
    }

    public UserEntity(UserDto user) {
        super(user);
        emailAddress = user.getEmailAddress();
        Optional.ofNullable(user.getPerson()).ifPresent(personDto -> personEntity = new PersonEntity(personDto));
        username = user.getUsername();
    }

    public UserDto asDto() {
        UserDto userDto = addPersistentData(new UserDto());
        userDto.setEmailAddress(emailAddress);
        Optional.ofNullable(personEntity).ifPresent(pen -> userDto.setPerson(pen.asDto()));
        userDto.setUsername(username);

        return userDto;
    }

    public @Override UserEntity copy() {
        return new UserEntity(this);
    }

    protected @Override Stream<Optional<PersistentEntity<Long>>> streamSequencedDependencies() {
        return Stream.concat(streamSequencedDependencies(personEntity, guestBook), blogs.stream().map(Optional::of));
    }

    public @Override boolean equals(Object o) {
        return o == this || o != null && getClass() == o.getClass() &&
                Objects.equals(emailAddress, ((UserEntity) o).getEmailAddress()) &&
                Objects.equals(personEntity, ((UserEntity) o).getPerson()) &&
                Objects.equals(username, ((UserEntity) o).getUsername());
    }

    public @Override int hashCode() {
        return hash(username, personEntity, emailAddress);
    }

    public @Override String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .appendSuper(super.toString())
                .append(getUsername())
                .append(getEmailAddress())
                .append(blogs)
                .append("guestbook.id=" + (guestBook != null ? guestBook.getId() : null))
                .append(getPerson())
                .toString();
    }

    public @Override Long getId() {
        return id;
    }

    protected @Override void setId(Long id) {
        this.id = id;
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

    public @SuppressWarnings("unused") /* used by reflection */ void setGuestBook(GuestBookEntity guestBook) {
        this.guestBook = guestBook;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPersonEntity(PersonEntity personEntity) {
        this.personEntity = personEntity;
    }

    void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

    public static UserEntityBuilder aUser() {
        return new UserEntityBuilder();
    }

    public boolean getInactive() {
        return inactive;
    }
}
