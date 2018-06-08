package com.github.jactor.rises.persistence.entity;

import com.github.jactor.rises.client.dto.NewPersistentDto;
import com.github.jactor.rises.commons.time.Now;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class PersistentEntity<I extends Serializable> { // the type of id

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

    protected PersistentEntity(PersistentEntity<I> persistentEntity) {
        createdBy = persistentEntity.createdBy;
        creationTime = persistentEntity.creationTime;
        updatedBy = persistentEntity.updatedBy;
        updatedTime = persistentEntity.updatedTime;
    }

    protected PersistentEntity(NewPersistentDto<Long> persistentDto) {
        createdBy = persistentDto.getCreatedBy();
        creationTime = persistentDto.getCreationTime();
        updatedBy = persistentDto.getUpdatedBy();
        updatedTime = persistentDto.getUpdatedTime();
    }

    protected <T extends NewPersistentDto<I>> T addPersistentData(T persistentDto) {
        persistentDto.setId(getId());
        persistentDto.setCreatedBy(createdBy);
        persistentDto.setCreationTime(creationTime);
        persistentDto.setUpdatedBy(updatedBy);
        persistentDto.setUpdatedTime(updatedTime);

        return persistentDto;
    }

    @SuppressWarnings("unchecked") public PersistentEntity<Long> addSequencedId(Sequencer sequencer) {
        addSequencedIdAlsoIncludingDependencies(sequencer);
        return (PersistentEntity<Long>) this;
    }

    protected <E extends PersistentEntity<Long>> void addSequencedIdToDependencies(E entity, Sequencer sequencer) {
        if (entity != null && entity.getId() == null) {
            entity.addSequencedIdAlsoIncludingDependencies(sequencer);
        }
    }

    @Override public String toString() {
        return "id=" + getId();
    }

    public abstract I getId();

    public abstract void addSequencedIdAlsoIncludingDependencies(Sequencer sequencer);

    public abstract PersistentEntity<I> copy();

    protected Long fetchId(Sequencer sequencer) {
        return getId() == null ? sequencer.nextVal(getClass()) : (Long) getId();
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    protected void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    protected void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public interface Sequencer {
        Long nextVal(Class<?> entityClass);
    }
}
