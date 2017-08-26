package nu.hjemme.persistence.client.dao;

import nu.hjemme.client.datatype.UserName;
import nu.hjemme.persistence.client.UserEntity;

import java.util.Optional;

public interface UserDao {

    Optional<UserEntity> findUsing(UserName userName);

    void save(UserEntity userEntity);
}
