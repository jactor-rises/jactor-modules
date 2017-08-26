package nu.hjemme.business.facade;

import nu.hjemme.client.datatype.UserName;
import nu.hjemme.client.domain.User;
import nu.hjemme.client.facade.UserFacade;
import nu.hjemme.persistence.client.UserEntity;
import nu.hjemme.persistence.client.dao.UserDao;

import java.util.Optional;

public class UserFacadeImpl implements UserFacade {

    private final UserDao userDao;

    public UserFacadeImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override public Optional<User> findUsing(UserName userName) {
        Optional<UserEntity> userEntityOptional = userDao.findUsing(userName);

        if (!userEntityOptional.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(userEntityOptional.get());
    }
}
