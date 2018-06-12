package com.github.jactor.rises.model.facade;

import com.github.jactor.rises.client.datatype.UserName;
import com.github.jactor.rises.client.domain.User;
import com.github.jactor.rises.client.dto.UserDto;
import com.github.jactor.rises.io.rest.UserRestService;
import com.github.jactor.rises.test.extension.validate.SuppressValidInstanceExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("A UserFacade")
class UserFacadeTest {

    private @InjectMocks UserFacadeImpl testUserFacadeImpl;

    private @Mock UserRestService userRestServiceMock;

    @BeforeEach void initMocking() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("should return an empty Optional when the UserRestService cannot find a dto for the given user name")
    @Test void willNotFindUnknownUser() {
        Optional<User> user = testUserFacadeImpl.findUsing(new UserName("someone"));
        assertThat(user.isPresent()).isEqualTo(false);
    }

    @DisplayName("should find a user when the user name only differs in case")
    @ExtendWith(SuppressValidInstanceExtension.class)
    @Test void willFindUser() {
        when(userRestServiceMock.find(new UserName("jactor"))).thenReturn(Optional.of(new UserDto()));
        Optional<User> optionalUser = testUserFacadeImpl.findUsing(new UserName("JACTOR"));

        assertThat(optionalUser.isPresent()).isEqualTo(true);
    }
}
