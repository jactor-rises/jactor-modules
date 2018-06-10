package com.github.jactor.rises.persistence.service;

import com.github.jactor.rises.client.dto.NewUserDto;
import com.github.jactor.rises.persistence.entity.user.UserEntity;
import com.github.jactor.rises.persistence.repository.UserRepository;
import com.github.jactor.rises.test.extension.validate.SuppressValidInstanceExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.github.jactor.rises.persistence.entity.user.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("A UserService")
@ExtendWith(SuppressValidInstanceExtension.class)
class UserServiceTest {

    private @InjectMocks UserService userServiceToTest;
    private @Mock UserRepository userRepositoryMock;

    @BeforeEach void initMocking() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("should map a user entity to a dto")
    @Test void shouldMapUserToDto() {
        when(userRepositoryMock.findByUserName("jactor")).thenReturn(Optional.of(aUser().withUserName("jactor").build()));
        NewUserDto user = userServiceToTest.find("jactor").orElseThrow(() -> new AssertionError("mocking?"));

        assertAll(
                () -> assertThat(user).as("user").isNotNull(),
                () -> assertThat(user.getUserName()).as("user.userName").isEqualTo("jactor")
        );
    }

    @DisplayName("should also map a user entity to a dto when finding by id")
    @Test void shouldMapUserToDtoWhenFindingById() {
        when(userRepositoryMock.findById(69L)).thenReturn(Optional.of(aUser().withUserName("jactor").build()));
        NewUserDto user = userServiceToTest.find(69L).orElseThrow(() -> new AssertionError("mocking?"));

        assertAll(
                () -> assertThat(user).as("user").isNotNull(),
                () -> assertThat(user.getUserName()).as("user.userName").isEqualTo("jactor")
        );
    }

    @DisplayName("should save UserDto as UserEntity")
    @Test void shouldSavedUserDtoAsUserEntity() {
        NewUserDto userDto = new NewUserDto();
        userDto.setUserName("marley");

        userServiceToTest.saveOrUpdate(userDto);

        ArgumentCaptor<UserEntity> argCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepositoryMock).save(argCaptor.capture());
        UserEntity userEntity = argCaptor.getValue();

        assertThat(userEntity.getUserName()).as("username").isEqualTo("marley");
    }
}