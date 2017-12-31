package com.github.jactorrises.persistence.entity;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.Persistent;
import com.github.jactorrises.persistence.client.dto.PersistentDto;
import com.github.jactorrises.commons.time.Now;
import org.hibernate.annotations.Type;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class PersistentEntity implements Persistent<Long> {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID") Long id;

    @Embedded @AttributeOverride(name = "timestamp", column = @Column(name = "CREATION_TIME")) @Type(type = "timestamp") private DateTimeEmbeddable creationTime;
    @Embedded @AttributeOverride(name = "name", column = @Column(name = "CREATED_BY")) private NameEmbeddable createdBy;
    @Embedded @AttributeOverride(name = "timestamp", column = @Column(name = "UPDATED_TIME")) @Type(type = "timestamp") private DateTimeEmbeddable updatedTime;
    @Embedded @AttributeOverride(name = "name", column = @Column(name = "UPDATED_BY")) private NameEmbeddable updatedBy;

    protected PersistentEntity() {
        createdBy = new NameEmbeddable(new Name("todo #156"));
        creationTime = new DateTimeEmbeddable(Now.asDateTime());
        updatedBy = new NameEmbeddable(new Name("todo #156"));
        updatedTime = new DateTimeEmbeddable(Now.asDateTime());
    }

    protected PersistentEntity(PersistentEntity persistentOrm) {
        createdBy = persistentOrm.createdBy;
        creationTime = persistentOrm.creationTime;
        updatedBy = persistentOrm.updatedBy;
        updatedTime = persistentOrm.updatedTime;
    }

    protected PersistentEntity(PersistentDto persistentDto) {
        createdBy = new NameEmbeddable(persistentDto.getCreatedBy());
        creationTime = new DateTimeEmbeddable(persistentDto.getCreationTime());
        updatedBy = new NameEmbeddable(persistentDto.getUpdatedBy());
        updatedTime = new DateTimeEmbeddable(persistentDto.getUpdatedTime());
    }

    @Override public String toString() {
        return "id=" + id;
    }

    @Override public Name getCreatedBy() {
        return createdBy.fetchName();
    }

    @Override public LocalDateTime getCreationTime() {
        return creationTime.fetchLocalDateTime();
    }

    @Override public Long getId() {
        return id;
    }

    @Override public Name getUpdatedBy() {
        return updatedBy.fetchName();
    }

    @Override public LocalDateTime getUpdatedTime() {
        return updatedTime.fetchLocalDateTime();
    }
}
