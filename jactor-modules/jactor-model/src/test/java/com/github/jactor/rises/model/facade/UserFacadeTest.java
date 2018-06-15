package com.github.jactor.rises.model.facade;

import com.github.jactor.rises.client.datatype.Username;
import com.github.jactor.rises.client.domain.User;
import com.github.jactor.rises.model.service.rest.UserRestService;
import com.github.jactor.rises.test.extension.validate.SuppressValidInstanceExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.github.jactor.rises.model.domain.user.UserDomain.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SuppressValidInstanceExtension.class)
@DisplayName("A UserFacade")
class UserFacadeTest {

    private @InjectMocks UserFacadeImpl testUserFacadeImpl;
    private @Mock UserRestService userRestServiceMock;

    @BeforeEach void initMocking() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("should return an empty Optional when the UserRestService cannot find a dto for the given user name")
    @Test void shouldlNotFindUnknownUser() {
        Optional<User> user = testUserFacadeImpl.find(new Username("someone"));
        assertThat(user).isNotPresent();
    }

    @DisplayName("should find a user when the user name only differs in case")
    @Test void shouldFindUser() {
        when(userRestServiceMock.find(new Username("jactor"))).thenReturn(Optional.of(aUser().build()));
        Optional<User> optionalUser = testUserFacadeImpl.find(new Username("JACTOR"));

        assertThat(optionalUser.isPresent()).isEqualTo(true);
    }
}
