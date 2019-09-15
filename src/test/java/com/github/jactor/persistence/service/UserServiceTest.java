package com.github.jactor.persistence.service;

import static com.github.jactor.persistence.entity.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.jactor.persistence.command.CreateUserCommand;
import com.github.jactor.persistence.dto.AddressDto;
import com.github.jactor.persistence.dto.PersistentDto;
import com.github.jactor.persistence.dto.PersonDto;
import com.github.jactor.persistence.dto.UserDto;
import com.github.jactor.persistence.dto.UserType;
import com.github.jactor.persistence.entity.PersonEntity;
import com.github.jactor.persistence.entity.UserEntity;
import com.github.jactor.persistence.repository.PersonRepository;
import com.github.jactor.persistence.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("A UserService")
@SpringBootTest
class UserServiceTest {

  @Autowired
  private UserService userServiceToTest;
  @MockBean
  private PersonRepository personRepository;
  @MockBean
  private UserRepository userRepositoryMock;

  @Test
  @DisplayName("should map a user entity to a dto")
  void shouldMapUserToDto() {
    var addressDto = new AddressDto();
    addressDto.setPersistentDto(new PersistentDto());

    var personDto = new PersonDto();
    personDto.setPersistentDto(new PersistentDto());
    personDto.setAddress(addressDto);

    when(userRepositoryMock.findByUsername("jactor"))
        .thenReturn(
            Optional.of(aUser(
                new UserDto(null, personDto, null, "jactor", UserType.ACTIVE)
            ))
        );

    var user = userServiceToTest.find("jactor").orElseThrow(() -> new AssertionError("mocking?"));

    assertAll(
        () -> assertThat(user).as("user").isNotNull(),
        () -> assertThat(user.getUsername()).as("user.username").isEqualTo("jactor")
    );
  }

  @Test
  @DisplayName("should also map a user entity to a dto when finding by id")
  void shouldMapUserToDtoWhenFindingById() {
    var addressDto = new AddressDto();
    addressDto.setPersistentDto(new PersistentDto());

    var personDto = new PersonDto();
    personDto.setPersistentDto(new PersistentDto());
    personDto.setAddress(addressDto);

    when(userRepositoryMock.findById(69L))
        .thenReturn(
            Optional.of(aUser(
                new UserDto(null, personDto, null, "jactor", UserType.ACTIVE)
            ))
        );

    var user = userServiceToTest.find(69L).orElseThrow(() -> new AssertionError("mocking?"));

    assertAll(
        () -> assertThat(user).as("user").isNotNull(),
        () -> assertThat(user.getUsername()).as("user.username").isEqualTo("jactor")
    );
  }

  @Test
  @DisplayName("should update a UserDto with an UserEntity")
  void shouldSavedUserDtoAsUserEntity() {
    var userDto = new UserDto();
    userDto.setId(1L);
    userDto.setUsername("marley");
    userDto.setPersistentDto(new PersistentDto());

    userServiceToTest.update(userDto);

    var argCaptor = ArgumentCaptor.forClass(UserEntity.class);
    verify(userRepositoryMock).save(argCaptor.capture());
    var userEntity = argCaptor.getValue();

    assertThat(userEntity.getUsername()).as("username").isEqualTo("marley");
  }

  @Test
  @DisplayName("should create and save person for the user")
  void shouldCreateAndSavePersonForTheUser() {
    var createUserCommand = new CreateUserCommand("jactor", "Jacobsen");
    var userDto = new UserDto();
    var userEntityMock = mockUserEntityWith(userDto);

    when(userRepositoryMock.save(any())).thenReturn(userEntityMock);
    when(personRepository.save(any())).thenReturn(new PersonEntity(new PersonDto()));

    var user = userServiceToTest.create(createUserCommand);

    assertAll(
        () -> assertThat(user).as("user").isEqualTo(userDto),
        () -> {
          var personCaptor = ArgumentCaptor.forClass(PersonEntity.class);
          verify(personRepository).save(personCaptor.capture());

          assertThat(personCaptor.getValue()).as("person to save").isNotNull();
        }
    );
  }

  private UserEntity mockUserEntityWith(UserDto userDto) {
    var userEntityMock = mock(UserEntity.class);

    when(userEntityMock.asDto()).thenReturn(userDto);

    return userEntityMock;
  }
}
