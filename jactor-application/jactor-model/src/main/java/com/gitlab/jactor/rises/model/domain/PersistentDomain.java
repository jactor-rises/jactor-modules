package com.gitlab.jactor.rises.model.domain;

import com.gitlab.jactor.rises.commons.datatype.Name;
import com.gitlab.jactor.rises.commons.dto.PersistentDto;
import org.apache.commons.lang3.Validate;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class PersistentDomain implements Persistent {
    static final String THE_PERSISTENT_DATA_ON_THE_DOMAIN_CANNOT_BE_NULL = "The persistent data the domain cannot be null!";

    PersistentDto fetchDto() {
        PersistentDto dto = getDto();
        Validate.notNull(dto, THE_PERSISTENT_DATA_ON_THE_DOMAIN_CANNOT_BE_NULL);

        return dto;
    }

    public abstract PersistentDto getDto();

    @Override public Serializable getId() {
        return fetchDto().getId();
    }

    @Override public Name getCreatedBy() {
        return new Name(fetchDto().getCreatedBy());
    }

    @Override public LocalDateTime getCreationTime() {
        return fetchDto().getCreationTime();
    }

    @Override public Name getUpdatedBy() {
        return new Name(fetchDto().getUpdatedBy());
    }

    @Override public LocalDateTime getUpdatedTime() {
        return fetchDto().getUpdatedTime();
    }
}
