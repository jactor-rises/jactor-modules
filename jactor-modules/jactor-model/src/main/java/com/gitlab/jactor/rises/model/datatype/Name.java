package com.gitlab.jactor.rises.model.datatype;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static org.apache.commons.lang3.Validate.notEmpty;

/** A bean representing a name */
public class Name implements Comparable<Name> {
    static final String A_NAME_MUST_BE_GIVEN = "A name must be given";

    private final String text;

    public Name(String name) {
        notEmpty(name, A_NAME_MUST_BE_GIVEN);
        text = name;
    }

    @Override public int compareTo(Name name) {
        return asString().compareTo(name.asString());
    }

    @Override public boolean equals(Object obj) {
        return this == obj || (obj != null && obj.getClass() == getClass() && asString().equals(((Name) obj).asString()));
    }

    @Override public int hashCode() {
        return text.hashCode();
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append(text).toString();
    }

    public String asString() {
        return text;
    }
}
