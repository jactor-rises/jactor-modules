package com.github.jactorrises.client.dto;

import com.github.jactorrises.client.converter.FieldConverter;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class PersistentDto implements Serializable {
    private Long id;
    private String createdBy;
    private String creationTime;
    private String updatedBy;
    private String updatedTime;

    PersistentDto() {
        createdBy = "todo: #156";
        creationTime = FieldConverter.convert(LocalDateTime.now());
        updatedBy = "todo: #156";
        updatedTime = FieldConverter.convert(LocalDateTime.now());
    }

    PersistentDto(PersistentDto persistent) {
        createdBy = persistent.getCreatedBy();
        creationTime = persistent.getCreationTime();
        id = persistent.getId();
        updatedBy = persistent.getUpdatedBy();
        updatedTime = persistent.getUpdatedTime();
    }

    public Long getId() {
        return id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }
}
