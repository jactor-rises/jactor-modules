package com.github.jactor.persistence.service;

import com.github.jactor.persistence.command.CreateUserCommand;
import com.github.jactor.persistence.dto.UserInternalDto;
import com.github.jactor.persistence.entity.UserEntity;
import com.github.jactor.persistence.entity.UserEntity.UserType;
import com.github.jactor.persistence.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private final PersonService personService;
  private final UserRepository userRepository;

  @Autowired
  public UserService(PersonService personService, UserRepository userRepository) {
    this.personService = personService;
    this.userRepository = userRepository;
  }

  public Optional<UserInternalDto> find(String username) {
    return userRepository.findByUsername(username).map(UserEntity::asDto);
  }

  public Optional<UserInternalDto> find(Long id) {
    return userRepository.findById(id).map(UserEntity::asDto);
  }

  @Transactional
  public Optional<UserInternalDto> update(UserInternalDto userInternalDto) {
    return userRepository.findById(Objects.requireNonNull(userInternalDto.getId()))
        .map(userEntity -> userEntity.update(userInternalDto))
        .map(UserEntity::asDto);
  }

  public UserInternalDto create(CreateUserCommand createUserCommand) {
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

  public List<String> findUsernames(UserType userType) {
    return userRepository.findByUserTypeIn(List.of(userType)).stream()
        .map(UserEntity::getUsername)
        .collect(Collectors.toList());
  }
}
