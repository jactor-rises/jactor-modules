package com.gitlab.jactor.rises.persistence.entity;

import com.gitlab.jactor.rises.commons.dto.PersistentDto;
import com.gitlab.jactor.rises.commons.time.Now;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@MappedSuperclass
public abstract class PersistentEntity<I extends Serializable> {

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

    public PersistentEntity<Long> addSequencedId(Sequencer sequencer) {
        @SuppressWarnings("unchecked") PersistentEntity<Long> persistentEntity = (PersistentEntity<Long>) this; // already type checked when invoked by aspect

        if (persistentEntity.getId() == null) {
            persistentEntity.addSequencedId(persistentEntity, sequencer);
        }

        persistentEntity.fetchAllSequencedDependencies().stream()
                .filter(dependency -> dependency.getId() == null)
                .forEach(depencency -> addSequencedId(depencency, sequencer));

        return persistentEntity;
    }

    private void addSequencedId(PersistentEntity<Long> entity, Sequencer sequencer) {
        Long id = sequencer.nextVal(entity.getClass());
        entity.setId(id);
    }

    protected Stream<Optional<PersistentEntity<Long>>> streamSequencedDependencies(PersistentEntity<Long>... persistentEntities) {
        if (persistentEntities == null) {
            return Stream.empty();
        }

        return Arrays.stream(persistentEntities)
                .map(Optional::ofNullable);
    }

    List<PersistentEntity<Long>> fetchAllSequencedDependencies() {
        List<PersistentEntity<Long>> sequencedDependencies = fetchSequencedDependencies(this);
        List<PersistentEntity<Long>> allSequencedDependencies = new ArrayList<>();

        for (PersistentEntity<Long> persistentEntity : sequencedDependencies) {
            addAllSequencedDependencis(persistentEntity, allSequencedDependencies);
        }

        return allSequencedDependencies;
    }

    private void addAllSequencedDependencis(PersistentEntity<Long> persistentEntity, List<PersistentEntity<Long>> allSequencedDependencies) {
        allSequencedDependencies.add(persistentEntity);
        List<PersistentEntity<Long>> otherSequencedDependencies = fetchSequencedDependencies(persistentEntity);

        otherSequencedDependencies.forEach(dependency -> {
            if (!allSequencedDependencies.contains(dependency)) {
                addAllSequencedDependencis(dependency, allSequencedDependencies);
            }
        });
    }

    private List<PersistentEntity<Long>> fetchSequencedDependencies(PersistentEntity<?> persistentEntity) {
        return persistentEntity.streamSequencedDependencies()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    public @Override String toString() {
        return String.format("%s.id<%s>", fetchEntityShortName(), getId());
    }

    private String fetchEntityShortName() {
        String simpleName = getClass().getSimpleName();

        return simpleName.substring(0, simpleName.indexOf("Entity"));
    }

    public abstract PersistentEntity<I> copy();

    protected abstract Stream<Optional<PersistentEntity<Long>>> streamSequencedDependencies();

    public abstract I getId();

    protected abstract void setId(I id);

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
