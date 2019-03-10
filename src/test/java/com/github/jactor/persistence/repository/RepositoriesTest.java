package com.github.jactor.persistence.repository;

import static com.github.jactor.persistence.entity.blog.BlogEntity.aBlog;
import static com.github.jactor.persistence.entity.user.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.github.jactor.persistence.JactorPersistence;
import com.github.jactor.persistence.dto.AddressDto;
import com.github.jactor.persistence.dto.BlogDto;
import com.github.jactor.persistence.dto.PersonDto;
import com.github.jactor.persistence.dto.UserDto;
import com.github.jactor.persistence.entity.blog.BlogEntity;
import com.github.jactor.persistence.entity.user.UserEntity;
import java.time.LocalDate;
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
@DisplayName("The RepositoriesTest")
class RepositoriesTest {

  @Autowired
  private BlogRepository blogRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private EntityManager entityManager;

  @Test
  @DisplayName("should use a BlogRepository to save a blog with a user that was saved with a UserRepository earlier")
  void shouldSaveBlogWithSavedUser() {
    AddressDto address = new AddressDto(null, 1001, "Test Boulevard 1", null, null, "Testoplis", null);
    PersonDto personDto = new PersonDto(null, address, "no_NO", null, "Skywalker", null);
    UserEntity userToPersist = aUser(new UserDto(null, personDto, "brains@rebels.com", "r2d2"));

    userRepository.save(userToPersist);
    entityManager.flush();
    entityManager.clear();

    UserEntity userById = userRepository.findById(userToPersist.getId()).orElseThrow(() -> new AssertionError("User not found!"));

    BlogEntity blogEntityToSave = aBlog(new BlogDto(null, LocalDate.now(), "Far, far, away...", userById.asDto()));

    blogRepository.save(blogEntityToSave);
    entityManager.flush();
    entityManager.clear();

    BlogEntity blogById = blogRepository.findById(blogEntityToSave.getId()).orElseThrow(() -> new AssertionError("Blog not found"));

    assertAll(
        () -> assertThat(blogById.getTitle()).as("blog.title").isEqualTo("Far, far, away..."),
        () -> assertThat(blogById.getUser()).as("blog.user").isEqualTo(userById)
    );
  }
}
