package com.github.jactorrises.persistence.entity.blog;

import com.github.jactorrises.persistence.entity.DateTextEmbeddable;
import com.github.jactorrises.persistence.entity.PersistentEntity;
import com.github.jactorrises.persistence.entity.user.UserOrm;
import com.github.jactorrises.persistence.client.entity.BlogEntity;
import com.github.jactorrises.persistence.client.entity.UserEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;

import static java.util.Objects.hash;

@Entity
@Table(name = "T_BLOG")
public class BlogOrm extends PersistentEntity implements BlogEntity {

    @Embedded @AttributeOverride(name = "dateAsText", column = @Column(name = "CREATED")) private DateTextEmbeddable created;
    @Column(name = "TITLE") private String title;
    @JoinColumn(name = "USER_ID") @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY) private UserOrm userEntity;

    BlogOrm() {
        created = new DateTextEmbeddable(LocalDate.now());
    }

    private BlogOrm(BlogOrm blogEntity) {
        super(blogEntity);
        created = blogEntity.created;
        title = blogEntity.getTitle();
        userEntity = blogEntity.copyUser();
    }

    BlogOrm copy() {
        return new BlogOrm(this);
    }

    private UserOrm copyUser() {
        return userEntity != null ? userEntity.copy() : null;
    }

    @Override public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() &&
                Objects.equals(title, ((BlogOrm) o).title) &&
                Objects.equals(userEntity, ((BlogOrm) o).userEntity);
    }

    @Override public int hashCode() {
        return hash(getTitle(), getUser());
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append(created.toString())
                .append(getTitle())
                .append(getUser())
                .toString();
    }

    @Override public String getTitle() {
        return title;
    }

    @Override public UserOrm getUser() {
        return userEntity;
    }

    @Override public LocalDate getCreated() {
        return created.fetchLocalDate();
    }

    @Override public void setTitle(String title) {
        this.title = title;
    }

    @Override public void setUserEntity(UserEntity userEntity) {
        this.userEntity = (UserOrm) userEntity;
    }
}
