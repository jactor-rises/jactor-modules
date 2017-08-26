package nu.hjemme.client.datatype;

import java.util.Objects;

import static java.util.Objects.hash;

public class UserName extends Name {
    public UserName(String name) {
        super(name);
    }

    @Override public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        String thisName = getName() != null ? getName().toUpperCase() : null;
        String thatName = ((UserName) obj).getName().toUpperCase();

        return Objects.equals(thisName, thatName);
    }

    @Override public int hashCode() {
        return hash(getName() != null ? getName().toUpperCase() : null);
    }
}
