package com.github.jactor.persistence.repository;

import static com.github.jactor.persistence.entity.UserEntity.aUser;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.github.jactor.persistence.JactorPersistence;
import com.github.jactor.persistence.dto.AddressDto;
import com.github.jactor.persistence.dto.PersonInternalDto;
import com.github.jactor.persistence.dto.UserInternalDto;
import com.github.jactor.persistence.entity.UserEntity;
import com.github.jactor.persistence.entity.UserEntity.UserType;
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
    AddressDto addressDto = new AddressDto(null, "1001", "Test Boulevard 1", null, null, "Testington", null);
    PersonInternalDto personInternalDto = new PersonInternalDto(null, addressDto, null, null, "Solo", null);
    UserEntity userToPersist = aUser(new UserInternalDto(
        null, personInternalDto, "smuggle.fast@tantooine.com", "smuggler"
    ));

    userRepository.save(userToPersist);
    entityManager.flush();
    entityManager.clear();

    Optional<UserEntity> userById = userRepository.findByUsername("smuggler");

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
    AddressDto addressDto = new AddressDto(null, "1001", "Test Boulevard 1", null, null, "Testington", null);
    PersonInternalDto personInternalDto = new PersonInternalDto(null, addressDto, null, null, "AA", null);
    UserEntity userToPersist = aUser(new UserInternalDto(
        null, personInternalDto, "casuel@tantooine.com", "causual"
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
    AddressDto addressDto = new AddressDto(null, "1001", "Test Boulevard 1", null, null, "Testington", null);
    PersonInternalDto spidyPersonInternalDto = new PersonInternalDto(null, addressDto, null, null, "Parker", null);
    PersonInternalDto superPersonInternalDto = new PersonInternalDto(null, addressDto, null, null, "Kent", null);
    userRepository.save(aUser(new UserInternalDto(null, spidyPersonInternalDto, null, "spiderman")));
    userRepository.save(aUser(new UserInternalDto(null, superPersonInternalDto, null, "superman", com.github.jactor.persistence.dto.Usertype.INACTIVE)));
    entityManager.flush();
    entityManager.clear();

    List<String> usernames = userRepository.findByUserTypeIsNot(UserType.INACTIVE).stream()
        .map(UserEntity::getUsername)
        .collect(toList());

    assertThat(usernames).contains("tip", "spiderman", "jactor");
  }
}
