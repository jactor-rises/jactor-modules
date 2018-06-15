package com.github.jactor.rises.persistence.entity;

import com.github.jactor.rises.client.dto.PersistentDto;
import com.github.jactor.rises.commons.time.Now;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class PersistentEntity<I extends Serializable> { // the type of id

    private @Column(name = "CREATION_TIME") LocalDateTime creationTime;
    private @Column(name = "CREATED_BY") String createdBy;
    private @Column(name = "UPDATED_TIME") LocalDateTime updatedTime;
    private @Column(name = "UPDATED_BY") String updatedBy;

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

    protected PersistentEntity(PersistentDto<I> persistentDto) {
        setId(persistentDto.getId());
        createdBy = persistentDto.getCreatedBy();
        creationTime = persistentDto.getCreationTime();
        updatedBy = persistentDto.getUpdatedBy();
        updatedTime = persistentDto.getUpdatedTime();
    }

    protected <T extends PersistentDto<I>> T addPersistentData(T persistentDto) {
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

    public @Override String toString() {
        return "id=" + getId();
    }

    public abstract void addSequencedIdAlsoIncludingDependencies(Sequencer sequencer);

    public abstract PersistentEntity<I> copy();

    public abstract I getId();

    protected abstract void setId(I id);

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
