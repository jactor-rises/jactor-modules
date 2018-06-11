package com.github.jactor.rises.model.domain.user;

import com.github.jactor.rises.client.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("A UserDomain")
class UserDomainTest {

    @DisplayName("should verify if its username is the user's email address")
    @Test void shouldVerifyUsernameVsEmail() {
        UserDto userDto = new UserDto();
        userDto.setEmailAddress("someone@out.there");
        userDto.setUserName("someone@out.there");

        assertThat(new UserDomain(userDto).isUserNameEmailAddress()).isTrue();
    }
}