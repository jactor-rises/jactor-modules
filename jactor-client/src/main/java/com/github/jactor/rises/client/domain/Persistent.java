package com.github.jactorrises.client.domain;

import com.github.jactorrises.client.datatype.Name;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Any persistent domain must have an identifier and data about creation from the persistent layer
 */
public interface Persistent {
    Serializable getId();

    Name getCreatedBy();

    LocalDateTime getCreationTime();

    Name getUpdatedBy();

    LocalDateTime getUpdatedTime();
}
