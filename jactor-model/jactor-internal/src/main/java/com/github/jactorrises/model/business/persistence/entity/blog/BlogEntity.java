package com.github.jactorrises.model.business.persistence.entity.blog;

import com.github.jactorrises.client.domain.Blog;
import com.github.jactorrises.model.business.persistence.entity.PersistentEntity;
import com.github.jactorrises.model.business.persistence.entity.user.UserEntity;
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
public class BlogEntity extends PersistentEntity implements Blog {

    @Column(name = BlogMetadata.CREATED) private String created;
    @Column(name = BlogMetadata.TITLE) private String title;
    @JoinColumn(name = BlogMetadata.USER) @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY) private UserEntity userEntity;

    public BlogEntity() {
        created = convertFrom(LocalDate.now(), LocalDate.class);
    }

    public BlogEntity(BlogEntity blogEntity) {
        created = convertFrom(blogEntity.getCreated(), LocalDate.class);
        title = blogEntity.getTitle();
        userEntity = blogEntity.getUser();
    }

    @Override public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() &&
                Objects.equals(title, ((BlogEntity) o).title) &&
                Objects.equals(userEntity, ((BlogEntity) o).userEntity);
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public static BlogEntityBuilder aBlog() {
        return new BlogEntityBuilder();
    }
}
