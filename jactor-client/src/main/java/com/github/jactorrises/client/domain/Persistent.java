package nu.hjemme.client.domain;

import nu.hjemme.client.datatype.Name;

import java.time.LocalDateTime;

/**
 * Any persistent domain must have an identifier and data about creation from the persistent layer
 */
public interface Persistent<Id> {
    Id getId();

    Name getCreatedBy();

    LocalDateTime getCreationTime();

    Name getUpdatedBy();

    LocalDateTime getUpdatedTime();
}
