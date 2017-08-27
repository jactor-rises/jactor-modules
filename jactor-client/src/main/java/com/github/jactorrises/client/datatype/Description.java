package com.github.jactorrises.client.datatype;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

import static java.util.Objects.hash;

public class Description {
    private final Name itemName;
    private final String description;

    public Description(String description) {
        this(null, description);
    }

    public Description(Name itemName, String description) {
        this.itemName = itemName;
        this.description = description;
    }

    @Override public int hashCode() {
        return hash(itemName, description);
    }

    @Override public boolean equals(Object o) {
        return o == this || o != null && getClass() == o.getClass() &&
                Objects.equals(itemName, ((Description) o).getItemName()) &&
                Objects.equals(description, ((Description) o).getDescription());
    }

    @Override public String toString() {
        return (itemName != null ? new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append(itemName) : new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE))
                .append(getDescription())
                .toString();
    }

    public Name getItemName() {
        return itemName;
    }

    public String getDescription() {
        return description;
    }
}
