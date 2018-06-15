package com.github.jactor.rises.model.facade;

import com.github.jactor.rises.client.datatype.Username;
import com.github.jactor.rises.client.domain.User;
import com.github.jactor.rises.model.service.UserDomainService;
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

@DisplayName("A UserFacade")
class UserFacadeTest {

    private @InjectMocks UserFacadeImpl testUserFacadeImpl;

    private @Mock UserDomainService userDomainServiceMock;

    @BeforeEach void initMocking() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("should return an empty Optional when the UserRestService cannot find a dto for the given user name")
    @Test void willNotFindUnknownUser() {
        Optional<User> user = testUserFacadeImpl.find(new Username("someone"));
        assertThat(user.isPresent()).isEqualTo(false);
    }

    @DisplayName("should find a user when the user name only differs in case")
    @ExtendWith(SuppressValidInstanceExtension.class)
    @Test void willFindUser() {
        when(userDomainServiceMock.find(new Username("jactor"))).thenReturn(Optional.of(aUser().build()));
        Optional<User> optionalUser = testUserFacadeImpl.find(new Username("JACTOR"));

        assertThat(optionalUser.isPresent()).isEqualTo(true);
    }

    @DisplayName("should fetch user by id")
    @ExtendWith(SuppressValidInstanceExtension.class)
    @Test void shouldFetchUserById() {
        when(userDomainServiceMock.ferch(1L)).thenReturn(Optional.of(aUser().build()));
        Optional<User> optionalUser = testUserFacadeImpl.fetch(1L);

        assertThat(optionalUser.isPresent()).isEqualTo(true);
    }
}
