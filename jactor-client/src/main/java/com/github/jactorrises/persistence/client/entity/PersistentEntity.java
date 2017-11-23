package com.github.jactorrises.persistence.client.entity;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.Persistent;

import java.time.LocalDateTime;

/**
 * Any persistent domain must have an identifier and data about creation from the persistent layer
 */
public interface PersistentEntity<I> extends Persistent<I> {

    Name getCreatedBy();

    LocalDateTime getCreationTime();

    Name getUpdatedBy();

    LocalDateTime getUpdatedTime();
}
