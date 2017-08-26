package nu.hjemme.persistence.client.converter;

import nu.hjemme.client.datatype.UserName;

public class UserNameConverter implements TypeConverter<UserName, String> {
    @Override public UserName convertTo(String from) {
        return from != null ? new UserName(from) : null;
    }

    @Override public String convertFrom(UserName userName) {
        return userName != null ? userName.getName() : null;
    }
}
