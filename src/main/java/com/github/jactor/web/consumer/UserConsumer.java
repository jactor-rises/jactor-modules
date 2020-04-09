package com.github.jactor.web.consumer;

import com.github.jactor.shared.dto.UserDto;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class UserConsumer {

  private final RestTemplate restTemplate;

  public UserConsumer(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public Optional<UserDto> find(String username) {
    ResponseEntity<UserDto> responseEntity = restTemplate.getForEntity("/user/name/" + username, UserDto.class);

    return Optional.ofNullable(bodyOf(responseEntity));
  }

  public List<String> findAllUsernames() {
    ResponseEntity<String[]> responseEntity = restTemplate.getForEntity(
        "/user/active/usernames", String[].class
    );

    return List.of(Objects.requireNonNull(responseEntity.getBody()));
  }

  private UserDto bodyOf(ResponseEntity<UserDto> responseEntity) {
    if (responseEntity == null) {
      throw new IllegalStateException("No response from REST service");
    }

    if (isNot2xxSuccessful(responseEntity.getStatusCode())) {
      var badConfiguredResponseMesssage = String.format("Bad configuration of consumer! ResponseCode: %s(%d)",
          responseEntity.getStatusCode().name(),
          responseEntity.getStatusCodeValue()
      );

      throw new IllegalStateException(badConfiguredResponseMesssage);
    }

    return responseEntity.getBody();
  }

  private boolean isNot2xxSuccessful(HttpStatus statusCode) {
    return !statusCode.is2xxSuccessful();
  }
}
