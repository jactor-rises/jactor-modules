package com.github.jactorrises.model.business.facade;

import com.github.jactorrises.model.business.domain.PersonDomain;
import com.github.jactorrises.model.business.domain.UserDomain;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.User;
import com.github.jactorrises.model.business.persistence.client.dao.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.github.jactorrises.model.business.domain.AddressDomain.anAddress;
import static com.github.jactorrises.model.business.domain.UserDomain.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("A UserFacade")
class UserFacadeTest {

    @InjectMocks
    private UserFacadeImpl testUserFacadeImpl;

    @Mock
    private UserDao userDaoMock;

    @BeforeEach
    void initMocking() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("should find a user when the user name only differs in case")
    @Test void willFindDefauldUser() {
        UserDomain user = aValidUser();
        when(userDaoMock.findUsing(new UserName("jactor"))).thenReturn(Optional.of(user.getEntity()));
        Optional<User> optionalUser = testUserFacadeImpl.findUsing(new UserName("JACTOR"));

        assertThat(optionalUser.isPresent()).isEqualTo(true);
    }

    private UserDomain aValidUser() {
        return aUser()
                .with(PersonDomain.aPerson()
                        .with(anAddress()
                                .withAddressLine1As("on the road")
                                .withZipCodeAs(69)
                                .withCountryAs("NO")
                        )
                ).withUserNameAs("turbo")
                .withPasswordAs("something")
                .build();
    }

    @DisplayName("should not return a user when the UserDao returns an empty optional ")
    @Test void willNotFindUnknownUser() {
        when(userDaoMock.findUsing(new UserName("someone"))).thenReturn(Optional.empty());
        Optional<User> user = testUserFacadeImpl.findUsing(new UserName("someone"));
        assertThat(user.isPresent()).isEqualTo(false);
    }
}
