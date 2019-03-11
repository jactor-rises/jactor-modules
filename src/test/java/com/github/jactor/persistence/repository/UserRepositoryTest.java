package com.github.jactor.persistence.repository;

import static com.github.jactor.persistence.entity.user.UserEntity.aUser;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.github.jactor.persistence.JactorPersistence;
import com.github.jactor.persistence.dto.AddressDto;
import com.github.jactor.persistence.dto.PersonDto;
import com.github.jactor.persistence.dto.UserDto;
import com.github.jactor.persistence.entity.user.UserEntity;
import com.github.jactor.persistence.entity.user.UserEntity.UserType;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@Transactional
@DisplayName("A UserRepository")
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private EntityManager entityManager;

  @Test
  @DisplayName("should find user with username jactor")
  void shouldFindJactor() {
    Optional<UserEntity> userByName = userRepository.findByUsername("jactor");

    assertAll(
        () -> assertThat(userByName).as("default user").isPresent(),
        () -> {
          UserEntity userEntity = userByName.orElseThrow(this::userNotFound);
          assertAll(
              () -> assertThat(userEntity.getEmailAddress()).as("user email").isEqualTo("tor.egil.jacobsen@gmail.com"),
              () -> assertThat(userEntity.getPerson().getFirstName()).as("user first name").isEqualTo("Tor Egil")
          );
        }
    );
  }

  @Test
  @DisplayName("should write then read a user entity")
  void shouldWriteThenReadUserEntity() {
    AddressDto addressDto = new AddressDto(null, 1001, "Test Boulevard 1", null, null, "Testington", null);
    PersonDto personDto = new PersonDto(null, addressDto, null, null, "Solo", null);
    UserEntity userToPersist = aUser(new UserDto(
        null, personDto, "smuggle.fast@tantooine.com", "smuggler"
    ));

    userRepository.save(userToPersist);
    entityManager.flush();
    entityManager.clear();

    Optional<UserEntity> userById = userRepository.findById(userToPersist.getId());

    assertAll(
        () -> assertThat(userById).isPresent(),
        () -> {
          UserEntity userEntity = userById.orElseThrow(this::userNotFound);
          assertAll(
              () -> assertThat(userEntity.getPerson()).as("person").isEqualTo(userToPersist.getPerson()),
              () -> assertThat(userEntity.getUsername()).as("username").isEqualTo("smuggler"),
              () -> assertThat(userEntity.getEmailAddress()).as("emailAddress").isEqualTo("smuggle.fast@tantooine.com")
          );
        }
    );
  }

  @Test
  @DisplayName("should write then update and read a user entity")
  void shouldWriteThenUpdateAndReadUserEntity() {
    AddressDto addressDto = new AddressDto(null, 1001, "Test Boulevard 1", null, null, "Testington", null);
    PersonDto personDto = new PersonDto(null, addressDto, null, null, "AA", null);
    UserEntity userToPersist = aUser(new UserDto(
        null, personDto, "casuel@tantooine.com", "causual"
    ));

    userRepository.save(userToPersist);
    entityManager.flush();
    entityManager.clear();

    String lukewarm = "lukewarm";
    userToPersist.setUsername(lukewarm);
    userToPersist.setEmailAddress("luke@force.com");

    userRepository.save(userToPersist);
    entityManager.flush();
    entityManager.clear();

    Optional<UserEntity> userByName = userRepository.findByUsername(lukewarm);

    assertAll(
        () -> assertThat(userByName).isPresent(),
        () -> {
          UserEntity userEntity = userByName.orElseThrow(this::userNotFound);
          assertAll(
              () -> assertThat(userEntity.getUsername()).as("username").isEqualTo(lukewarm),
              () -> assertThat(userEntity.getEmailAddress()).as("emailAddress").isEqualTo("luke@force.com")
          );
        }
    );
  }

  private AssertionError userNotFound() {
    return new AssertionError("no user found");
  }

  @Test
  @DisplayName("should find all active users")
  void shouldFindAllActiveUsers() {
    AddressDto addressDto = new AddressDto(null, 1001, "Test Boulevard 1", null, null, "Testington", null);
    PersonDto spidyPersonDto = new PersonDto(null, addressDto, null, null, "Parker", null);
    PersonDto superPersonDto = new PersonDto(null, addressDto, null, null, "Kent", null);
    userRepository.save(aUser(new UserDto(null, spidyPersonDto, null, "spiderman")));
    userRepository.save(aUser(new UserDto(null, superPersonDto, null, "superman", com.github.jactor.persistence.dto.UserType.INACTIVE)));
    entityManager.flush();
    entityManager.clear();

    List<String> usernames = userRepository.findByUserType(UserType.ACTIVE).stream()
        .map(UserEntity::getUsername)
        .collect(toList());

    assertThat(usernames).containsExactly("tip", "spiderman");

    usernames = userRepository.findByUserType(UserType.ADMIN).stream()
        .map(UserEntity::getUsername)
        .collect(toList());

    assertThat(usernames).containsExactly("jactor");
  }
}
