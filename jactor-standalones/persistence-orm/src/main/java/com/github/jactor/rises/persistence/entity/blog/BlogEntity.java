package com.github.jactor.rises.persistence.entity.blog;

import com.github.jactor.rises.client.dto.NewBlogDto;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.hash;
import static java.util.stream.Collectors.toSet;

@Entity
@Table(name = "T_BLOG")
public class BlogEntity extends PersistentEntity<Long> {

    @Id private Long id;

    @Column(name = "CREATED") private LocalDate created;
    @Column(name = "TITLE") private String title;
    @JoinColumn(name = "USER_ID") @ManyToOne(cascade = CascadeType.MERGE) private UserEntity userEntity;
    @OneToMany(mappedBy = "blog", fetch = FetchType.EAGER, cascade = CascadeType.MERGE) private Set<BlogEntryEntity> entries = new HashSet<>();

    BlogEntity() {
        created = LocalDate.now();
    }

    private BlogEntity(BlogEntity blogEntity) {
        super(blogEntity);
        created = blogEntity.created;
        entries = blogEntity.entries.stream().map(BlogEntryEntity::copy).collect(toSet());
        title = blogEntity.getTitle();
        userEntity = Optional.ofNullable(blogEntity.getUser()).map(UserEntity::copy).orElse(null);
    }

    public BlogEntity(NewBlogDto blogDto) {
        super(blogDto);
        created = blogDto.getCreated();
        entries = blogDto.getEntries().stream().map(BlogEntryEntity::new).collect(toSet());
        title = blogDto.getTitle();
        userEntity = new UserEntity(blogDto.getUser());
    }

    public BlogEntity copy() {
        return new BlogEntity(this);
    }

    public NewBlogDto asDto() {
        NewBlogDto blogDto = addPersistentData(new NewBlogDto());
        blogDto.setCreated(created);
        blogDto.setEntries(entries.stream().map(bee -> bee.asDto(blogDto)).collect(toSet()));
        blogDto.setTitle(title);
        blogDto.setUser(userEntity.asDto());

        return blogDto;
    }

    public void add(BlogEntryEntity blogEntryEntity) {
        blogEntryEntity.setBlog(this);
        entries.add(blogEntryEntity);
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
                .append(created)
                .append(getTitle())
                .append(getUser())
                .toString();
    }

    @Override public Long getId() {
        return id;
    }

    @Override public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreated() {
        return created;
    }

    public Set<BlogEntryEntity> getEntries() {
        return entries;
    }

    public String getTitle() {
        return title;
    }

    public UserEntity getUser() {
        return userEntity;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @SuppressWarnings("WeakerAccess") /* used by reflection */ public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public static BlogEntityBuilder aBlog() {
        return new BlogEntityBuilder();
    }
}
