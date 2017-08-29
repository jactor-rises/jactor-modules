package com.github.jactorrises.persistence.boot.entity.guestbook;

import com.github.jactorrises.client.domain.GuestBook;
import com.github.jactorrises.client.domain.User;
import com.github.jactorrises.persistence.boot.entity.PersistentEntity;
import com.github.jactorrises.persistence.boot.entity.user.UserEntityImpl;
import com.github.jactorrises.persistence.client.GuestBookEntity;
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

import static com.github.jactorrises.persistence.boot.entity.guestbook.GuestBookMetadata.GUEST_BOOK_TABLE;
import static com.github.jactorrises.persistence.boot.entity.guestbook.GuestBookMetadata.TITLE;
import static com.github.jactorrises.persistence.boot.entity.guestbook.GuestBookMetadata.USER;
import static java.util.Objects.hash;

@Entity
@Table(name = GUEST_BOOK_TABLE)
public class GuestBookEntityImpl extends PersistentEntity implements GuestBookEntity {

    @Column(name = TITLE) private String title;
    @JoinColumn(name = USER) @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY) private UserEntityImpl user;

    public GuestBookEntityImpl() {
    }

    /**
     * @param guestBook will be used to copy an instance...
     */
    public GuestBookEntityImpl(GuestBook guestBook) {
        title = guestBook.getTitle();
        user = castOrInitializeCopyWith(guestBook.getUser(), UserEntityImpl.class);
    }

    @Override public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() &&
                Objects.equals(getId(), ((GuestBookEntityImpl) o).getId()) &&
                Objects.equals(title, ((GuestBookEntityImpl) o).title) &&
                Objects.equals(user, ((GuestBookEntityImpl) o).user);
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

    @Override public String getTitle() {
        return title;
    }

    @Override public User getUser() {
        return user;
    }

    @Override public void setTitle(String title) {
        this.title = title;
    }

    @Override public void setUser(UserEntity user) {
        this.user = castOrInitializeCopyWith(user, UserEntityImpl.class);
    }

    public static GuestBookEntityBuilder aGuestBook() {
        return new GuestBookEntityBuilder();
    }
}
