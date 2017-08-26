package nu.hjemme.client.facade;

import nu.hjemme.client.datatype.UserName;
import nu.hjemme.client.domain.User;

import java.util.Optional;

public interface UserFacade {

    Optional<User> findUsing(UserName userName);
}
