package nu.hjemme.client.domain;

import nu.hjemme.client.datatype.EmailAddress;
import nu.hjemme.client.datatype.UserName;

public interface User extends Persistent<Long> {

    String getPassword();

    UserName getUserName();

    Person getPerson();

    EmailAddress getEmailAddress();

    boolean isUserNameEmailAddress();
}
