package com.github.jactor.persistence.service;

import com.github.jactor.persistence.command.CreateUserCommand;
import com.github.jactor.persistence.dto.UserDto;
import com.github.jactor.persistence.entity.UserEntity;
import com.github.jactor.persistence.entity.UserEntity.UserType;
import com.github.jactor.persistence.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final PersonService personService;
  private final UserRepository userRepository;

  @Autowired
  public UserService(PersonService personService, UserRepository userRepository) {
    this.personService = personService;
    this.userRepository = userRepository;
  }

  public Optional<UserDto> find(String username) {
    return userRepository.findByUsername(username).map(UserEntity::asDto);
  }

  public Optional<UserDto> find(Long id) {
    return userRepository.findById(id).map(UserEntity::asDto);
  }

  public UserDto update(UserDto userDto) {
    Objects.requireNonNull(userDto.getId());

    UserEntity userEntity = new UserEntity(userDto);
    userRepository.save(userEntity);

    return userEntity.asDto();
  }

  public UserDto create(CreateUserCommand createUserCommand) {
    UserEntity userEntity = createNewFrom(createUserCommand);
    userEntity = userRepository.save(userEntity);

    return userEntity.asDto();
  }

  private UserEntity createNewFrom(CreateUserCommand createUserCommand) {
    var personDto = createUserCommand.fetchPersonDto();
    var personEntity = personService.createWhenNotExists(personDto);
    var userEntity = new UserEntity(createUserCommand.fetchUserDto());

    userEntity.setPersonEntity(personEntity);

    return userEntity;
  }

  public List<String> findUsernamesOnActiveUsers() {
    return userRepository.findByUserTypeIsNot(UserType.INACTIVE).stream()
        .map(UserEntity::getUsername)
        .collect(Collectors.toList());
  }
}
