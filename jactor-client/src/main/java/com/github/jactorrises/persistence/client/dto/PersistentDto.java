package com.github.jactorrises.persistence.client.dto;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.Persistent;

import java.time.LocalDateTime;

public abstract class PersistentDto implements Persistent<Long> {
    private Long id;
    private Name createdBy;
    private Name updatedBy;
    private LocalDateTime creationTime;
    private LocalDateTime updatedTime;

    PersistentDto() {
        // empty, use setters...
    }

    PersistentDto(Persistent<Long> persistent) {
        createdBy = persistent.getCreatedBy();
        creationTime = persistent.getCreationTime();
        id = persistent.getId();
        updatedBy = persistent.getUpdatedBy();
        updatedTime = persistent.getUpdatedTime();
    }

    @Override public Long getId() {
        return id;
    }

    @Override public Name getCreatedBy() {
        return createdBy;
    }

    @Override public LocalDateTime getCreationTime() {
        return creationTime;
    }

    @Override public Name getUpdatedBy() {
        return updatedBy;
    }

    @Override public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatedBy(Name createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedBy(Name updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
