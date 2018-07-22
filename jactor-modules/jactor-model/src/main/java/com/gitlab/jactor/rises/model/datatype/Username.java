package com.gitlab.jactor.rises.model.datatype;

import java.util.Objects;

import static java.util.Objects.hash;

public class Username extends Name {
    public Username(String name) {
        super(name);
    }

    @Override public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        String thisName = asString() != null ? asString().toUpperCase() : null;
        String thatName = ((Username) obj).asString().toUpperCase();

        return Objects.equals(thisName, thatName);
    }

    @Override public int hashCode() {
        return hash(asString() != null ? asString().toUpperCase() : null);
    }
}
