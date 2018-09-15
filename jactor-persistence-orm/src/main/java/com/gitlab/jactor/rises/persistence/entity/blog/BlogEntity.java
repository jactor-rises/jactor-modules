package com.gitlab.jactor.rises.persistence.entity.blog;

import com.gitlab.jactor.rises.commons.dto.BlogDto;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Objects.hash;
import static java.util.stream.Collectors.toSet;

@Entity
@Table(name = "T_BLOG")
public class BlogEntity extends PersistentEntity<Long> {

    private @Id Long id;

    private @Column(name = "CREATED") LocalDate created;
    private @Column(name = "TITLE") String title;
    private @JoinColumn(name = "USER_ID") @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY) UserEntity userEntity;
    private @OneToMany(mappedBy = "blog", fetch = FetchType.EAGER, cascade = CascadeType.MERGE) Set<BlogEntryEntity> entries = new HashSet<>();

    BlogEntity() {
        created = LocalDate.now();
    }

    private BlogEntity(BlogEntity blogEntity) {
        super(blogEntity);
        created = blogEntity.created;
        entries = blogEntity.entries.stream().map(BlogEntryEntity::copy).collect(toSet());
        title = blogEntity.getTitle();
        Optional.ofNullable(blogEntity.getUser()).ifPresent(user -> userEntity = user.copy());
    }

    public BlogEntity(BlogDto blogDto) {
        super(blogDto);
        created = blogDto.getCreated();
        title = blogDto.getTitle();
        Optional.ofNullable(blogDto.getUser()).ifPresent(user -> userEntity = new UserEntity(user));
    }

    public BlogDto asDto() {
        BlogDto blogDto = addPersistentData(new BlogDto());
        blogDto.setCreated(created);
        blogDto.setTitle(title);
        Optional.ofNullable(userEntity).ifPresent(usr -> blogDto.setUser(usr.asDto()));

        return blogDto;
    }

    public void add(BlogEntryEntity blogEntryEntity) {
        blogEntryEntity.setBlog(this);
        entries.add(blogEntryEntity);
    }

    public @Override BlogEntity copy() {
        return new BlogEntity(this);
    }

    protected @Override Stream<Optional<PersistentEntity<Long>>> streamSequencedDependencies() {
        return Stream.concat(streamSequencedDependencies(userEntity), entries.stream().map(Optional::ofNullable));
    }

    public @Override boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() &&
                Objects.equals(getTitle(), ((BlogEntity) o).getTitle()) &&
                Objects.equals(getUser(), ((BlogEntity) o).getUser());
    }

    public @Override int hashCode() {
        return hash(getTitle(), getUser());
    }

    public @Override String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append(getCreated())
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
