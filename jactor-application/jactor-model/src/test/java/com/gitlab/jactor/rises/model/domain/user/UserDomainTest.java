package com.gitlab.jactor.rises.model.domain.user;

import com.gitlab.jactor.rises.commons.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("A UserDomain")
class UserDomainTest {

    @DisplayName("should verify if its username is the user's email address")
    @Test void shouldVerifyUsernameVsEmail() {
        UserDto userDto = new UserDto();
        userDto.setEmailAddress("someone@out.there");
        userDto.setUsername("someone@out.there");

        assertThat(new UserDomain(userDto).isUserNameEmailAddress()).isTrue();
    }
}