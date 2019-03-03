package com.gitlab.jactor.persistence.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gitlab.jactor.persistence.JactorPersistence;
import com.gitlab.jactor.persistence.dto.GuestBookDto;
import com.gitlab.jactor.persistence.dto.GuestBookEntryDto;
import com.gitlab.jactor.persistence.service.GuestBookService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JactorPersistence.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("A GuestBookController")
class GuestBookControllerTest {

  @LocalServerPort
  private int port;
  @Value("${server.servlet.context-path}")
  private String contextPath;

  @MockBean
  private GuestBookService guestBookServiceMock;
  @Autowired
  private TestRestTemplate testRestTemplate;

  @Test
  @DisplayName("should build full path")
  void shouldBuildFullPath() {
    assertThat(buildFullPath("/somewhere")).isEqualTo("http://localhost:" + port + "/jactor-persistence-orm/somewhere");
  }

  @Test
  @DisplayName("should not get a guest book")
  void shouldNotGetGuestBook() {
    when(guestBookServiceMock.find(1L)).thenReturn(Optional.empty());

    var guestBookRespnse = testRestTemplate.getForEntity(buildFullPath("/guestBook/1"), GuestBookDto.class);

    assertAll(
        () -> assertThat(guestBookRespnse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.NO_CONTENT),
        () -> assertThat(guestBookRespnse).extracting(ResponseEntity::getBody).as("guest book").isNull()
    );
  }

  @Test
  @DisplayName("should get a guest book")
  void shouldGetGuestBook() {
    when(guestBookServiceMock.find(1L)).thenReturn(Optional.of(new GuestBookDto()));

    var guestBookRespnse = testRestTemplate.getForEntity(buildFullPath("/guestBook/1"), GuestBookDto.class);

    assertAll(
        () -> assertThat(guestBookRespnse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.OK),
        () -> assertThat(guestBookRespnse).extracting(ResponseEntity::getBody).as("guest book").isNotNull()
    );
  }

  @Test
  @DisplayName("should not get a guest book entry")
  void shouldNotGetGuestBookEntry() {
    when(guestBookServiceMock.findEntry(1L)).thenReturn(Optional.empty());

    var guestBookEntryRespnse = testRestTemplate.getForEntity(buildFullPath("/guestBook/entry/1"), GuestBookDto.class);

    assertAll(
        () -> assertThat(guestBookEntryRespnse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.NO_CONTENT),
        () -> assertThat(guestBookEntryRespnse).extracting(ResponseEntity::getBody).as("guest book entry").isNull()
    );
  }

  @Test
  @DisplayName("should get a guest book entry")
  void shouldGetGuestBookEntry() {
    when(guestBookServiceMock.findEntry(1L)).thenReturn(Optional.of(new GuestBookEntryDto()));

    var guestBookEntryRespnse = testRestTemplate.getForEntity(buildFullPath("/guestBook/entry/1"), GuestBookDto.class);

    assertAll(
        () -> assertThat(guestBookEntryRespnse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.OK),
        () -> assertThat(guestBookEntryRespnse).extracting(ResponseEntity::getBody).as("guest book entry").isNotNull()
    );
  }

  @Test
  @DisplayName("should modify existing guest book")
  void shouldModifyExistingGuestBook() {
    GuestBookDto guestBookDto = new GuestBookDto();
    guestBookDto.setId(1L);

    when(guestBookServiceMock.saveOrUpdate(any(GuestBookDto.class))).thenReturn(guestBookDto);

    var guestbookResponse = testRestTemplate.exchange(
        buildFullPath("/guestBook/1"), HttpMethod.PUT, new HttpEntity<>(guestBookDto), GuestBookDto.class
    );

    assertAll(
        () -> assertThat(guestbookResponse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.ACCEPTED),
        () -> assertThat(guestbookResponse).extracting(ResponseEntity::getBody).as("guest book").isNotNull(),
        () -> assertThat(guestbookResponse.getBody()).extracting(GuestBookDto::getId).as("guest book id").isEqualTo(1L),
        () -> verify(guestBookServiceMock).saveOrUpdate(any(GuestBookDto.class))
    );
  }

  @Test
  @DisplayName("should create a guest book")
  void shouldCreateGuestBook() {
    GuestBookDto guestBookDto = new GuestBookDto();
    GuestBookDto createdDto = new GuestBookDto();
    createdDto.setId(1L);

    when(guestBookServiceMock.saveOrUpdate(any(GuestBookDto.class))).thenReturn(createdDto);

    var guestbookResponse = testRestTemplate.postForEntity(buildFullPath("/guestBook"), guestBookDto, GuestBookDto.class);

    assertAll(
        () -> assertThat(guestbookResponse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.CREATED),
        () -> assertThat(guestbookResponse).extracting(ResponseEntity::getBody).as("guest book").isNotNull(),
        () -> assertThat(guestbookResponse.getBody()).extracting(GuestBookDto::getId).as("guest book id").isEqualTo(1L),
        () -> verify(guestBookServiceMock).saveOrUpdate(any(GuestBookDto.class))
    );
  }

  @Test
  @DisplayName("should modify existing guest book entry")
  void shouldModifyExistingGuestBookEntry() {
    GuestBookEntryDto guestBookEntryDto = new GuestBookEntryDto();
    guestBookEntryDto.setId(1L);

    when(guestBookServiceMock.saveOrUpdate(any(GuestBookEntryDto.class))).thenReturn(guestBookEntryDto);

    var guestbookEntryResponse = testRestTemplate.exchange(
        buildFullPath("/guestBook/entry/1"), HttpMethod.PUT, new HttpEntity<>(guestBookEntryDto), GuestBookEntryDto.class
    );

    assertAll(
        () -> assertThat(guestbookEntryResponse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.ACCEPTED),
        () -> assertThat(guestbookEntryResponse).extracting(ResponseEntity::getBody).as("guest book entry").isNotNull(),
        () -> assertThat(guestbookEntryResponse.getBody()).extracting(GuestBookEntryDto::getId).as("guest book entry id").isEqualTo(1L),
        () -> verify(guestBookServiceMock).saveOrUpdate(any(GuestBookEntryDto.class))
    );
  }

  @Test
  @DisplayName("should create a guest book entry")
  void shouldCreateGuestBookEntry() {
    GuestBookEntryDto guestBookEntryDto = new GuestBookEntryDto();
    GuestBookEntryDto createdDto = new GuestBookEntryDto();
    createdDto.setId(1L);

    when(guestBookServiceMock.saveOrUpdate(any(GuestBookEntryDto.class))).thenReturn(createdDto);

    var guestbookEntryResponse = testRestTemplate.postForEntity(buildFullPath("/guestBook/entry"), guestBookEntryDto, GuestBookEntryDto.class);

    assertAll(
        () -> assertThat(guestbookEntryResponse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.CREATED),
        () -> assertThat(guestbookEntryResponse).extracting(ResponseEntity::getBody).as("guest book entry").isNotNull(),
        () -> assertThat(guestbookEntryResponse.getBody()).extracting(GuestBookEntryDto::getId).as("guest book entry id").isEqualTo(1L),
        () -> verify(guestBookServiceMock).saveOrUpdate(any(GuestBookEntryDto.class))
    );
  }

  private String buildFullPath(String url) {
    return "http://localhost:" + port + contextPath + url;
  }
}
