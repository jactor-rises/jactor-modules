package com.github.jactorrises.persistence.boot.entity.blog;

import com.github.jactorrises.persistence.boot.entity.PersistentEntity;
import com.github.jactorrises.persistence.boot.entity.user.UserEntityImpl;
import com.github.jactorrises.persistence.client.BlogEntity;
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
import java.time.LocalDate;
import java.util.Objects;

import static java.util.Objects.hash;

@Entity
@Table(name = BlogMetadata.BLOG_TABLE)
public class BlogEntityImpl extends PersistentEntity implements BlogEntity {

    @Column(name = BlogMetadata.CREATED) private String created;
    @Column(name = BlogMetadata.TITLE) private String title;
    @JoinColumn(name = BlogMetadata.USER) @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY) private UserEntityImpl userEntity;

    public BlogEntityImpl() {
        created = convertFrom(LocalDate.now(), LocalDate.class);
    }

    public BlogEntityImpl(BlogEntity blogEntity) {
        created = convertFrom(blogEntity.getCreated(), LocalDate.class);
        title = blogEntity.getTitle();
        userEntity = castOrInitializeCopyWith(blogEntity.getUser(), UserEntityImpl.class);
    }

    @Override public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() &&
                Objects.equals(title, ((BlogEntityImpl) o).title) &&
                Objects.equals(userEntity, ((BlogEntityImpl) o).userEntity);
    }

    @Override public int hashCode() {
        return hash(getTitle(), getUser());
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append(getCreated())
                .append(getTitle())
                .append(getUser())
                .toString();
    }

    @Override public String getTitle() {
        return title;
    }

    @Override public UserEntity getUser() {
        return userEntity;
    }

    @Override public LocalDate getCreated() {
        return convertTo(created, LocalDate.class);
    }

    @Override public void setTitle(String title) {
        this.title = title;
    }

    @Override public void setUserEntity(UserEntity userEntity) {
        this.userEntity = castOrInitializeCopyWith(userEntity, UserEntityImpl.class);
    }

    public static BlogEntityBuilder aBlog() {
        return new BlogEntityBuilder();
    }
}
