package com.github.jactor.web.consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import com.github.jactor.web.dto.PersonDto;
import com.github.jactor.web.dto.UserDto;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@DisplayName("The UserConsumer")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserConsumerIntegrationTest {

  private static final String EXPECTED_BASE_URL = "http://localhost:1099/jactor-persistence";

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private UserConsumer userConsumerToTest;

  @Value("${jactor-persistence.url.root}")
  private String baseUrl;

  @BeforeEach
  void assumeJactorPersistenceIsRunning() {
    ResponseEntity<String> response = null;

    try {
      response = testRestTemplate.getForEntity(EXPECTED_BASE_URL + "/actuator/health", String.class);
    } catch (RestClientException e) {
      Assumptions.assumeTrue(false, "Failure with rest api: " + e.getMessage());
    }

    assumeTrue(response.getStatusCode().is2xxSuccessful(), response.getStatusCode().getReasonPhrase());
    assumeTrue(Objects.requireNonNull(response.getBody()).contains("UP"), response.getBody());
  }

  @Test
  @DisplayName("should verify expected base url")
  void shouldVerifyExpectedBaseUrl() throws URISyntaxException {
    URI uri = restTemplate.getUriTemplateHandler().expand("/user");
    assertThat(uri).isEqualTo(new URI(EXPECTED_BASE_URL + "/user"));
  }

  @Test
  @DisplayName("should find the default persisted user")
  void shouldFindTheDefaultPersistedUser() {
    var usernames = userConsumerToTest.findAllUsernames();

    assertThat(usernames).contains("tip");
  }

  @Test
  @DisplayName("should find the user named jactor")
  void shouldFindTheUserNamedJactor() {
    var possibleUser = userConsumerToTest.find("jactor");

    assertAll(
        () -> assertThat(possibleUser).as("possible user").isPresent(),
        () -> {
          var user = possibleUser.orElse(new UserDto());

          assertAll(
              () -> assertThat(user.getPerson()).extracting(PersonDto::getFirstName).as("user.person.firstName").isEqualTo("Tor Egil"),
              () -> assertThat(user.getEmailAddress()).as("user.emailaddress").isEqualTo("tor.egil.jacobsen@gmail.com")
          );
        }
    );
  }
}