package com.github.jactor.rises.persistence.entity;

import com.github.jactor.rises.client.dto.NewPersistentDto;
import com.github.jactor.rises.commons.time.Now;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class PersistentEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID") private Long id;

    @Column(name = "CREATION_TIME") private LocalDateTime creationTime;
    @Column(name = "CREATED_BY") private String createdBy;
    @Column(name = "UPDATED_TIME") private LocalDateTime updatedTime;
    @Column(name = "UPDATED_BY") private String updatedBy;

    protected PersistentEntity() {
        createdBy = "todo #156";
        creationTime = Now.asDateTime();
        updatedBy = "todo #156";
        updatedTime = Now.asDateTime();
    }

    protected PersistentEntity(PersistentEntity persistentEntity) {
        createdBy = persistentEntity.createdBy;
        creationTime = persistentEntity.creationTime;
        updatedBy = persistentEntity.updatedBy;
        updatedTime = persistentEntity.updatedTime;
    }

    protected PersistentEntity(NewPersistentDto persistentDto) {
        createdBy = persistentDto.getCreatedBy();
        creationTime = persistentDto.getCreationTime();
        updatedBy = persistentDto.getUpdatedBy();
        updatedTime = persistentDto.getUpdatedTime();
    }

    protected <T extends NewPersistentDto> T addPersistentData(T persistentDto) {
        persistentDto.setId(id);
        persistentDto.setCreatedBy(createdBy);
        persistentDto.setCreationTime(creationTime);
        persistentDto.setUpdatedBy(updatedBy);
        persistentDto.setUpdatedTime(updatedTime);

        return persistentDto;
    }

    @Override public String toString() {
        return "id=" + getId();
    }

    public Long getId() {
        return id;
    }

    @SuppressWarnings("unchecked")
    protected <T extends PersistentEntity> T asCopy() {
        createdBy = "todo #156 as copy";
        return (T) this;
    }
}
