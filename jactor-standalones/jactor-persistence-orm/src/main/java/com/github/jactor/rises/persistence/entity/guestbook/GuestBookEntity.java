package com.github.jactor.rises.persistence.entity.guestbook;

import com.github.jactor.rises.client.dto.NewGuestBookDto;
import com.github.jactor.rises.persistence.entity.PersistentEntity;
import com.github.jactor.rises.persistence.entity.user.UserEntity;
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

import static java.util.Objects.hash;
import static java.util.stream.Collectors.toSet;

@Entity
@Table(name = "T_GUEST_BOOK")
public class GuestBookEntity extends PersistentEntity<Long> {

    @Id private Long id;

    @Column(name = "TITLE") private String title;
    @JoinColumn(name = "USER_ID") @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY) private UserEntity user;
    @OneToMany(mappedBy = "guestBook", cascade = CascadeType.MERGE, fetch = FetchType.EAGER) private Set<GuestBookEntryEntity> entries = new HashSet<>();

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

    public GuestBookEntity(NewGuestBookDto guestBook) {
        super(guestBook);
        title = guestBook.getTitle();
        Optional.ofNullable(guestBook.getUser()).map(UserEntity::new).ifPresent(userEntity -> user = userEntity);
        entries = guestBook.getEntries().stream().map(GuestBookEntryEntity::new).collect(toSet());
    }

    private UserEntity copyUser() {
        return Optional.ofNullable(user).map(UserEntity::copy).orElse(null);
    }

    public NewGuestBookDto asDto() {
        NewGuestBookDto guestBook = new NewGuestBookDto();
        guestBook.setEntries(entries.stream().map(gbee -> gbee.asDto(guestBook)).collect(toSet()));
        guestBook.setTitle(title);
        Optional.ofNullable(user).map(UserEntity::asDto).ifPresent(guestBook::setUser);

        return guestBook;
    }

    @Override public GuestBookEntity copy() {
        return new GuestBookEntity(this);
    }

    @Override public void addSequencedIdAlsoIncludingDependencies(Sequencer sequencer) {
        id = fetchId(sequencer);
        addSequencedIdToDependencies(user, sequencer);
    }

    @Override public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() &&
                Objects.equals(title, ((GuestBookEntity) o).title) &&
                Objects.equals(user, ((GuestBookEntity) o).user);
    }

    @Override public int hashCode() {
        return hash(title, user);
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .appendSuper(super.toString())
                .append(title)
                .append(user)
                .toString();
    }

    @Override public Long getId() {
        return id;
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
