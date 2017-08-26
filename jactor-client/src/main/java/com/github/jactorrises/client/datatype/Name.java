package nu.hjemme.client.datatype;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static org.apache.commons.lang3.Validate.notEmpty;

/** A bean representing a name */
public class Name implements Comparable<Name> {
    static final String A_NAME_MUST_BE_GIVEN = "A name must be given";

    private final String nameToWrap;

    public Name(String name) {
        notEmpty(name, A_NAME_MUST_BE_GIVEN);
        this.nameToWrap = name;
    }

    @Override public int compareTo(Name name) {
        return getName().compareTo(name.getName());
    }

    @Override public boolean equals(Object obj) {
        return this == obj || (obj != null && obj.getClass() == getClass() && getName().equals(((Name) obj).getName()));
    }

    @Override public int hashCode() {
        return nameToWrap.hashCode();
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append(nameToWrap).toString();
    }

    public String getName() {
        return nameToWrap;
    }
}
