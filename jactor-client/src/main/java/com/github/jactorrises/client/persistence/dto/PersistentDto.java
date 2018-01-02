package com.github.jactorrises.client.persistence.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class PersistentDto {
    private Serializable id;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime creationTime;
    private LocalDateTime updatedTime;

    PersistentDto() {
        // empty, use setters...
    }

    PersistentDto(PersistentDto persistent) {
        createdBy = persistent.getCreatedBy();
        creationTime = persistent.getCreationTime();
        id = persistent.getId();
        updatedBy = persistent.getUpdatedBy();
        updatedTime = persistent.getUpdatedTime();
    }

    public Serializable getId() {
        return id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setId(Serializable id) {
        this.id = id;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
