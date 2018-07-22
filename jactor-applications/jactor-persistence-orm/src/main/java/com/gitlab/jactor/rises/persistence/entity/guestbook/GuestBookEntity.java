package com.gitlab.jactor.rises.persistence.entity.guestbook;

import com.gitlab.jactor.rises.commons.dto.GuestBookDto;
import com.gitlab.jactor.rises.persistence.entity.PersistentEntity;
import com.gitlab.jactor.rises.persistence.entity.user.UserEntity;
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
import java.util.stream.Stream;

import static java.util.Objects.hash;
import static java.util.stream.Collectors.toSet;

@Entity
@Table(name = "T_GUEST_BOOK")
public class GuestBookEntity extends PersistentEntity<Long> {

    private @Id Long id;

    private @Column(name = "TITLE") String title;
    private @JoinColumn(name = "USER_ID") @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY) UserEntity user;
    private @OneToMany(mappedBy = "guestBook", cascade = CascadeType.MERGE, fetch = FetchType.EAGER) Set<GuestBookEntryEntity> entries = new HashSet<>();

    GuestBookEntity() {
        // used by builder
    }

    /**
     * @param guestBook to copy...
     */
    private GuestBookEntity(GuestBookEntity guestBook) {
        super(guestBook);
        title = guestBook.title;
        user = guestBook.copyUser();
        entries = guestBook.entries.stream().map(GuestBookEntryEntity::copy).collect(toSet());
    }

    public GuestBookEntity(GuestBookDto guestBook) {
        super(guestBook);
        title = guestBook.getTitle();
        Optional.ofNullable(guestBook.getUser()).map(UserEntity::new).ifPresent(userEntity -> user = userEntity);
        entries = guestBook.getEntries().stream().map(GuestBookEntryEntity::new).collect(toSet());
    }

    private UserEntity copyUser() {
        return Optional.ofNullable(user).map(UserEntity::copy).orElse(null);
    }

    public GuestBookDto asDto() {
        GuestBookDto guestBook = addPersistentData(new GuestBookDto());
        guestBook.setTitle(title);
        Optional.ofNullable(user).map(UserEntity::asDto).ifPresent(guestBook::setUser);

        return guestBook;
    }

    public @Override GuestBookEntity copy() {
        return new GuestBookEntity(this);
    }

    protected @Override Stream<Optional<PersistentEntity<Long>>> streamSequencedDependencies() {
        return Stream.concat(streamSequencedDependencies(user), entries.stream().map(Optional::of));
    }

    public @Override boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() &&
                Objects.equals(getTitle(), ((GuestBookEntity) o).getTitle()) &&
                Objects.equals(getUser(), ((GuestBookEntity) o).getUser());
    }

    public @Override int hashCode() {
        return hash(title, user);
    }

    public @Override String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .appendSuper(super.toString())
                .append(getTitle())
                .append(getUser())
                .toString();
    }

    public @Override Long getId() {
        return id;
    }

    protected @Override void setId(Long id) {
        this.id = id;
    }

    public Set<GuestBookEntryEntity> getEntries() {
        return entries;
    }

    public String getTitle() {
        return title;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public static GuestBookEntityBuilder aGuestBook() {
        return new GuestBookEntityBuilder();
    }
}
