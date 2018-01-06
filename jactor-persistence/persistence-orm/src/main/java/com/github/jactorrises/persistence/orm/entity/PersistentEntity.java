package com.github.jactorrises.persistence.orm.entity;

import com.github.jactorrises.client.converter.FieldConverter;
import com.github.jactorrises.client.dto.PersistentDto;
import com.github.jactorrises.commons.time.Now;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class PersistentEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID") Long id;

    @Column(name = "CREATION_TIME") private String creationTime;
    @Column(name = "CREATED_BY") private String createdBy;
    @Column(name = "UPDATED_TIME") private String updatedTime;
    @Column(name = "UPDATED_BY") private String updatedBy;

    protected PersistentEntity() {
        createdBy = "todo #156";
        creationTime = FieldConverter.convert(Now.asDateTime());
        updatedBy = "todo #156";
        updatedTime = FieldConverter.convert(Now.asDateTime());
    }

    protected PersistentEntity(PersistentEntity persistentOrm) {
        createdBy = persistentOrm.createdBy;
        creationTime = persistentOrm.creationTime;
        updatedBy = persistentOrm.updatedBy;
        updatedTime = persistentOrm.updatedTime;
    }

    protected PersistentEntity(PersistentDto persistentDto) {
        createdBy = persistentDto.getCreatedBy();
        creationTime = persistentDto.getCreationTime();
        updatedBy = persistentDto.getUpdatedBy();
        updatedTime = persistentDto.getUpdatedTime();
    }

    protected <T extends PersistentDto> T addPersistentData(T persistentDto) {
        persistentDto.setId(id);
        persistentDto.setCreatedBy(createdBy);
        persistentDto.setCreationTime(creationTime);
        persistentDto.setUpdatedBy(updatedBy);
        persistentDto.setUpdatedTime(updatedTime);

        return persistentDto;
    }

    @Override public String toString() {
        return "id=" + id;
    }

    public Long getId() {
        return id;
    }
}
