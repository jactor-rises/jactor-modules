package com.github.jactorrises.client.domain;

/**
 * Any persistent domain must have an identifier and data about creation from the persistent layer
 */
public interface Persistent<I> {
    I getId();

    default Persistent<I> getPersistent() {
        return this;
    }
}
