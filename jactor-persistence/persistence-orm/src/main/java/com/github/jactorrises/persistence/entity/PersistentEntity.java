package com.github.jactorrises.persistence.entity;

import com.github.jactorrises.client.persistence.dto.PersistentDto;
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
public abstract class PersistentEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID") Long id;

    @Embedded @AttributeOverride(name = "timestamp", column = @Column(name = "CREATION_TIME")) @Type(type = "timestamp") private DateTimeEmbeddable creationTime;
    @Column(name = "CREATED_BY") private String createdBy;
    @Embedded @AttributeOverride(name = "timestamp", column = @Column(name = "UPDATED_TIME")) @Type(type = "timestamp") private DateTimeEmbeddable updatedTime;
    @Column(name = "UPDATED_BY") private String updatedBy;

    protected PersistentEntity() {
        createdBy = "todo #156";
        creationTime = new DateTimeEmbeddable(Now.asDateTime());
        updatedBy = "todo #156";
        updatedTime = new DateTimeEmbeddable(Now.asDateTime());
    }

    protected PersistentEntity(PersistentEntity persistentOrm) {
        createdBy = persistentOrm.createdBy;
        creationTime = persistentOrm.creationTime;
        updatedBy = persistentOrm.updatedBy;
        updatedTime = persistentOrm.updatedTime;
    }

    protected PersistentEntity(PersistentDto persistentDto) {
        createdBy = persistentDto.getCreatedBy();
        creationTime = new DateTimeEmbeddable(persistentDto.getCreationTime());
        updatedBy = persistentDto.getUpdatedBy();
        updatedTime = new DateTimeEmbeddable(persistentDto.getUpdatedTime());
    }

    @Override public String toString() {
        return "id=" + id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreationTime() {
        return creationTime.fetchLocalDateTime();
    }

    public Long getId() {
        return id;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime.fetchLocalDateTime();
    }
}
