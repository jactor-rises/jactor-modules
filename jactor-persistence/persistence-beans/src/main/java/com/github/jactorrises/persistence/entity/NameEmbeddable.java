package com.github.jactorrises.model.persistence.entity;

import com.github.jactorrises.client.datatype.Name;

import java.util.Objects;

import static java.util.Objects.hash;

public class NameEmbeddable {

    private final String name;

    @SuppressWarnings("unused") // used by hibernate
    NameEmbeddable() {
        name = null;
    }

    public NameEmbeddable(Name name) {
        this.name = name != null ? name.asString() : null;
    }

    public Name fetchName() {
        return name != null ? new Name(name) : null;
    }

    @Override public int hashCode() {
        return hash(name);
    }

    @Override public boolean equals(Object o) {
        return o == this || o != null && o.getClass().equals(getClass()) && Objects.equals(name, ((NameEmbeddable) o).name);
    }

    @Override public String toString() {
        return name != null ? new Name(name).toString() : "null";
    }
}
