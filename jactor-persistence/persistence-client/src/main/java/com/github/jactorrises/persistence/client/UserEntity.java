package nu.hjemme.persistence.client;

import nu.hjemme.client.domain.User;

public interface UserEntity extends User {
    void setUserName(String userName);

    void setPersonEntity(PersonEntity personEntity);

    void setPassword(String password);

    void setEmailAddress(String emailAddress);

    void setUserNameAsEmailAddress();
}
