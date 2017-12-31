package com.github.jactorrises.persistence.entity.guestbook;

import com.github.jactorrises.client.domain.GuestBook;
import com.github.jactorrises.persistence.client.dto.GuestBookDto;
import com.github.jactorrises.persistence.entity.PersistentEntity;
import com.github.jactorrises.persistence.entity.user.UserOrm;
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
@Table(name = "T_GUEST_BOOK")
public class GuestBookEntity extends PersistentEntity implements GuestBook {

    @Column(name = "TITLE") private String title;
    @JoinColumn(name = "USER_ID") @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY) private UserOrm user;

    public GuestBookEntity() {
    }

    /**
     * @param guestBook to copy...
     */
    private GuestBookEntity(GuestBookEntity guestBook) {
        super(guestBook);
        title = guestBook.title;
        user = guestBook.copyUser();
    }

    GuestBookEntity(GuestBookDto guestBook) {
        super(guestBook);
        title = guestBook.getTitle();
        user = new UserOrm(guestBook.getUser());
    }

    private UserOrm copyUser() {
        return user != null ? user.copy() : null;
    }

    GuestBookEntity copy() {
        return new GuestBookEntity(this);
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

    @Override public UserOrm getUser() {
        return user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(UserOrm user) {
        this.user = user;
    }
}
