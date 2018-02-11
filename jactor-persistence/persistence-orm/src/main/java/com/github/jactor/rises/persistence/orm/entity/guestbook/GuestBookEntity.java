package com.github.jactor.rises.persistence.orm.entity.guestbook;

import com.github.jactor.rises.client.dto.GuestBookDto;
import com.github.jactor.rises.persistence.orm.entity.PersistentEntity;
import com.github.jactor.rises.persistence.orm.entity.user.UserEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.util.Objects.hash;
import static java.util.stream.Collectors.toSet;

@Entity
@Table(name = "T_GUEST_BOOK")
public class GuestBookEntity extends PersistentEntity {

    @Column(name = "TITLE") private String title;
    @JoinColumn(name = "USER_ID") @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY) private UserEntity user;
    @OneToMany(mappedBy = "guestBookEntity", fetch = FetchType.EAGER) private Set<GuestBookEntryEntity> entries = new HashSet<>();

    GuestBookEntity() {
    }

    /**
     * @param guestBook to copy...
     */
    private GuestBookEntity(GuestBookEntity guestBook) {
        super(guestBook);
        title = guestBook.title;
        user = guestBook.copyUser();
    }

    public GuestBookEntity(GuestBookDto guestBook) {
        super(guestBook);
        title = guestBook.getTitle();
        user = new UserEntity(guestBook.getUser());
    }

    private UserEntity copyUser() {
        return user != null ? user.copy() : null;
    }

    public GuestBookEntity copy() {
        return new GuestBookEntity(this);
    }

    public GuestBookDto asDto() {
        GuestBookDto guestBook = new GuestBookDto();
        guestBook.setEntries(entries.stream().map(gbee -> gbee.asDto(guestBook)).collect(toSet()));
        guestBook.setTitle(title);
        guestBook.setUser(user.asDto());

        return guestBook;
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
