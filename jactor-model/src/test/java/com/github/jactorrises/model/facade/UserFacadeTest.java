package com.github.jactorrises.model.facade;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.User;
import com.github.jactorrises.model.domain.person.PersonDomain;
import com.github.jactorrises.model.domain.user.UserDomain;
import com.github.jactorrises.persistence.client.dao.PersistentDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.github.jactorrises.model.domain.address.AddressDomain.anAddress;
import static com.github.jactorrises.model.domain.user.UserDomain.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("A UserFacade")
class UserFacadeTest {

    @InjectMocks
    private UserFacadeImpl testUserFacadeImpl;

    @Mock
    private PersistentDao persistentDaoMock;

    @BeforeEach
    void initMocking() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("should find a user when the user name only differs in case")
    @Test void willFindDefauldUser() {
        UserDomain user = aValidUser();
        when(persistentDaoMock.findUsing(new UserName("jactor"))).thenReturn(Optional.of(user.getEntity()));
        Optional<User> optionalUser = testUserFacadeImpl.findUsing(new UserName("JACTOR"));

        assertThat(optionalUser.isPresent()).isEqualTo(true);
    }

    private UserDomain aValidUser() {
        return aUser()
                .with(PersonDomain.aPerson()
                        .withSurname("jacobsen")
                        .with(anAddress()
                                .withAddressLine1("on the road")
                                .withZipCode(69)
                                .withCountry("NO")
                        )
                ).withUserName("turbo")
                .build();
    }

    @DisplayName("should not return a user when the PersistentDao returns an empty optional ")
    @Test void willNotFindUnknownUser() {
        when(persistentDaoMock.findUsing(new UserName("someone"))).thenReturn(Optional.empty());
        Optional<User> user = testUserFacadeImpl.findUsing(new UserName("someone"));
        assertThat(user.isPresent()).isEqualTo(false);
    }
}
