package com.github.jactorrises.model.persistence.entity.guestbook;

import com.github.jactorrises.client.domain.GuestBook;
import com.github.jactorrises.model.persistence.entity.PersistentEntity;
import com.github.jactorrises.model.persistence.entity.user.UserEntity;
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
@Table(name = "T_GUEST_BOOK ")
public class GuestBookEntity extends PersistentEntity implements GuestBook {

    @Column(name = "TITLE") private String title;
    @JoinColumn(name = "USER_ID") @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY) private UserEntity user;

    public GuestBookEntity() {
    }

    /**
     * @param guestBook will be used to copy an instance...
     */
    public GuestBookEntity(GuestBookEntity guestBook) {
        super(guestBook);
        title = guestBook.title;
        user = guestBook.user != null ? new UserEntity(guestBook.getUser()) : null;
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

    @Override public String getTitle() {
        return title;
    }

    @Override public UserEntity getUser() {
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
