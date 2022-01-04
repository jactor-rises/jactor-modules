package com.github.jactor.persistence.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.jactor.persistence.JactorPersistence;
import com.github.jactor.persistence.dto.BlogDto;
import com.github.jactor.persistence.dto.BlogEntryDto;
import com.github.jactor.persistence.service.BlogService;
import java.util.Collections;
import java.util.List;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JactorPersistence.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("A BlogController")
class BlogControllerTest {

  @LocalServerPort
  private int port;
  @Value("${server.servlet.context-path}")
  private String contextPath;

  @MockBean
  private BlogService blogServiceMock;
  @Autowired
  private TestRestTemplate testRestTemplate;

  @Test
  @DisplayName("should build full path")
  void shouldBuildFullPath() {
    assertThat(buildFullPath("/somewhere")).isEqualTo("http://localhost:" + port + "/jactor-persistence/somewhere");
  }

  @Test
  @DisplayName("should find a blog")
  void shouldFindBlog() {
    when(blogServiceMock.find(1L)).thenReturn(Optional.of(new BlogDto()));

    var blogResponse = testRestTemplate.getForEntity(buildFullPath("/blog/1"), BlogDto.class);

    assertAll(
        () -> assertThat(blogResponse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.OK),
        () -> assertThat(blogResponse).extracting(ResponseEntity::getBody).as("blog").isNotNull()
    );
  }

  @Test
  @DisplayName("should not find a blog")
  void shouldNotFindBlog() {
    when(blogServiceMock.find(1L)).thenReturn(Optional.empty());

    var blogResponse = testRestTemplate.getForEntity(buildFullPath("/blog/1"), BlogDto.class);

    assertAll(
        () -> assertThat(blogResponse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.NO_CONTENT),
        () -> assertThat(blogResponse).extracting(ResponseEntity::getBody).as("blog").isNull()
    );
  }

  @Test
  @DisplayName("should find a blog entry")
  void shouldFindBlogEntry() {
    when(blogServiceMock.findEntryBy(1L)).thenReturn(Optional.of(new BlogEntryDto()));

    var blogEntryResponse = testRestTemplate.getForEntity(buildFullPath("/blog/entry/1"), BlogEntryDto.class);

    assertAll(
        () -> assertThat(blogEntryResponse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.OK),
        () -> assertThat(blogEntryResponse).extracting(ResponseEntity::getBody).as("blog entry").isNotNull()
    );
  }

  @Test
  @DisplayName("should not find a blog entry")
  void shouldNotFindBlogEntry() {
    when(blogServiceMock.findEntryBy(1L)).thenReturn(Optional.empty());

    var blogEntryResponse = testRestTemplate.getForEntity(buildFullPath("/blog/entry/1"), BlogEntryDto.class);

    assertAll(
        () -> assertThat(blogEntryResponse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.NO_CONTENT),
        () -> assertThat(blogEntryResponse).extracting(ResponseEntity::getBody).as("blog entry").isNull()
    );
  }

  @Test
  @DisplayName("should not find blogs by title")
  void shouldNotFindBlogs() {
    when(blogServiceMock.findBlogsBy("Anything")).thenReturn(Collections.emptyList());

    var blogResponse = testRestTemplate.exchange(buildFullPath("/blog/title/Anything"), HttpMethod.GET, null, typeIsListOfBlogs());

    assertAll(
        () -> assertThat(blogResponse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.NO_CONTENT),
        () -> assertThat(blogResponse).extracting(ResponseEntity::getBody).as("blogs").isNull()
    );
  }

  @Test
  @DisplayName("should find blogs by title")
  void shouldFindBlogs() {
    when(blogServiceMock.findBlogsBy("Anything")).thenReturn(Collections.singletonList(new BlogDto()));

    var blogResponse = testRestTemplate.exchange(buildFullPath("/blog/title/Anything"), HttpMethod.GET, null, typeIsListOfBlogs());

    assertAll(
        () -> assertThat(blogResponse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.OK),
        () -> assertThat(blogResponse.getBody()).as("blogs").isNotEmpty()
    );
  }

  private ParameterizedTypeReference<List<BlogDto>> typeIsListOfBlogs() {
    return new ParameterizedTypeReference<>() {
    };
  }

  @Test
  @DisplayName("should not find blog entries by blog id")
  void shouldNotFindBlogEntries() {
    when(blogServiceMock.findEntriesForBlog(1L)).thenReturn(Collections.emptyList());

    var blogEntriesResponse = testRestTemplate.exchange(buildFullPath("/blog/1/entries"), HttpMethod.GET, null, typeIsListOfBlogEntries());

    assertAll(
        () -> assertThat(blogEntriesResponse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.NO_CONTENT),
        () -> assertThat(blogEntriesResponse).extracting(ResponseEntity::getBody).as("blogs").isNull()
    );
  }

  @Test
  @DisplayName("should find blog entries by blog id")
  void shouldFindBlogEntries() {
    when(blogServiceMock.findEntriesForBlog(1L)).thenReturn(Collections.singletonList(new BlogEntryDto()));

    var blogEntriesResponse = testRestTemplate.exchange(buildFullPath("/blog/1/entries"), HttpMethod.GET, null, typeIsListOfBlogEntries());

    assertAll(
        () -> assertThat(blogEntriesResponse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.OK),
        () -> assertThat(blogEntriesResponse.getBody()).as("blogs").isNotEmpty()
    );
  }

  private ParameterizedTypeReference<List<BlogEntryDto>> typeIsListOfBlogEntries() {
    return new ParameterizedTypeReference<>() {
    };
  }

  @Test
  @DisplayName("should persist changes to existing blog")
  void shouldPersistChangesToExistingBlog() {
    BlogDto blogDto = new BlogDto();
    blogDto.setId(1L);

    when(blogServiceMock.saveOrUpdate(any(BlogDto.class))).thenReturn(blogDto);

    var blogResponse = testRestTemplate.exchange(buildFullPath("/blog/1"), HttpMethod.PUT, new HttpEntity<>(blogDto), BlogDto.class);

    assertAll(
        () -> assertThat(blogResponse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.ACCEPTED),
        () -> assertThat(blogResponse).extracting(ResponseEntity::getBody).as("updated blog").isNotNull(),
        () -> verify(blogServiceMock).saveOrUpdate(any(BlogDto.class))
    );
  }

  @Test
  @DisplayName("should create a blog")
  void shouldCreateBlog() {
    BlogDto blogDto = new BlogDto();
    BlogDto createdDto = new BlogDto();
    createdDto.setId(1L);

    when(blogServiceMock.saveOrUpdate(any(BlogDto.class))).thenReturn(createdDto);

    var blogResponse = testRestTemplate.exchange(buildFullPath("/blog"), HttpMethod.POST, new HttpEntity<>(blogDto), BlogDto.class);

    assertAll(
        () -> assertThat(blogResponse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.CREATED),
        () -> assertThat(blogResponse).extracting(ResponseEntity::getBody).as("created blog").isNotNull(),
        () -> assertThat(blogResponse.getBody()).extracting(BlogDto::getId).as("blog id").isEqualTo(1L),
        () -> verify(blogServiceMock).saveOrUpdate(any(BlogDto.class))
    );
  }

  @Test
  @DisplayName("should persist changes to existing blog entry")
  void shouldPersistChangesToExistingBlogEntry() {
    BlogEntryDto blogEntryDto = new BlogEntryDto();
    blogEntryDto.setId(1L);

    when(blogServiceMock.saveOrUpdate(any(BlogEntryDto.class))).thenReturn(blogEntryDto);

    var blogEntryResponse = testRestTemplate.exchange(
        buildFullPath("/blog/entry/1"), HttpMethod.PUT, new HttpEntity<>(blogEntryDto), BlogEntryDto.class
    );

    assertAll(
        () -> assertThat(blogEntryResponse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.ACCEPTED),
        () -> assertThat(blogEntryResponse).extracting(ResponseEntity::getBody).as("updated entry").isNotNull(),
        () -> verify(blogServiceMock).saveOrUpdate(any(BlogEntryDto.class))
    );
  }

  @Test
  @DisplayName("should create blog entry")
  void shouldCreateBlogEntry() {
    BlogEntryDto blogEntryDto = new BlogEntryDto();
    BlogEntryDto createdDto = new BlogEntryDto();
    createdDto.setId(1L);

    when(blogServiceMock.saveOrUpdate(any(BlogEntryDto.class))).thenReturn(createdDto);

    var blogEntryResponse = testRestTemplate.exchange(
        buildFullPath("/blog/entry"), HttpMethod.POST, new HttpEntity<>(blogEntryDto), BlogEntryDto.class
    );

    assertAll(
        () -> assertThat(blogEntryResponse).extracting(ResponseEntity::getStatusCode).as("status").isEqualTo(HttpStatus.CREATED),
        () -> assertThat(blogEntryResponse).extracting(ResponseEntity::getBody).as("created entry").isNotNull(),
        () -> assertThat(blogEntryResponse.getBody()).extracting(BlogEntryDto::getId).as("blog entry id").isEqualTo(1L),
        () -> verify(blogServiceMock).saveOrUpdate(any(BlogEntryDto.class))
    );
  }

  private String buildFullPath(String url) {
    return "http://localhost:" + port + contextPath + url;
  }
}
