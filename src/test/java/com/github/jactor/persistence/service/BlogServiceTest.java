package com.github.jactor.persistence.service;

import static com.github.jactor.persistence.entity.blog.BlogEntity.aBlog;
import static com.github.jactor.persistence.entity.blog.BlogEntryEntity.aBlogEntry;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.jactor.persistence.dto.BlogDto;
import com.github.jactor.persistence.dto.BlogEntryDto;
import com.github.jactor.persistence.dto.UserDto;
import com.github.jactor.persistence.entity.blog.BlogEntity;
import com.github.jactor.persistence.entity.blog.BlogEntryEntity;
import com.github.jactor.persistence.repository.BlogEntryRepository;
import com.github.jactor.persistence.repository.BlogRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@DisplayName("A BlogService")
class BlogServiceTest {

  @InjectMocks
  private BlogService blogServiceToTest;
  @Mock
  private BlogRepository blogRepositoryMock;
  @Mock
  private BlogEntryRepository blogEntryRepositoryMock;
  @Mock
  private UserService userServiceMock;

  @BeforeEach
  void initMocking() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  @DisplayName("should map blog to dto")
  void shouldMapBlogToDto() {
    Optional<BlogEntity> blogEntity = Optional.of(aBlog(new BlogDto(null, null, "full speed ahead", null)));
    when(blogRepositoryMock.findById(1001L)).thenReturn(blogEntity);

    BlogDto blog = blogServiceToTest.find(1001L).orElseThrow(mockError());

    assertThat(blog.getTitle()).as("title").isEqualTo("full speed ahead");
  }

  @Test
  @DisplayName("should map blog entry to dto")
  void shouldMapFoundBlogToDto() {
    BlogEntryDto blogEntryDto = new BlogEntryDto(null, new BlogDto(), "me", "too");
    Optional<BlogEntryEntity> anEntry = Optional.of(aBlogEntry(blogEntryDto));
    when(blogEntryRepositoryMock.findById(1001L)).thenReturn(anEntry);

    BlogEntryDto blogEntry = blogServiceToTest.findEntryBy(1001L).orElseThrow(mockError());

    assertAll(
        () -> assertThat(blogEntry.getCreatorName()).as("creator name").isEqualTo("me"),
        () -> assertThat(blogEntry.getEntry()).as("entry").isEqualTo("too")
    );
  }

  private Supplier<AssertionError> mockError() {
    return () -> new AssertionError("missed mocking?");
  }

  @Test
  @DisplayName("should find blogs for title")
  void shouldFindBlogsForTitle() {
    List<BlogEntity> blogsToFind = Collections.singletonList(aBlog(new BlogDto(null, null, "Star Wars", null)));
    when(blogRepositoryMock.findBlogsByTitle("Star Wars")).thenReturn(blogsToFind);

    List<BlogDto> blogForTitle = blogServiceToTest.findBlogsBy("Star Wars");

    assertThat(blogForTitle).hasSize(1);
  }

  @Test
  @DisplayName("should map blog entries to a list of dto")
  void shouldMapBlogEntriesToListOfDto() {
    List<BlogEntryEntity> blogEntryEntities = Collections.singletonList(
        aBlogEntry(new BlogEntryDto(null, new BlogDto(), "you", "too"))
    );

    when(blogEntryRepositoryMock.findByBlog_Id(1001L)).thenReturn(blogEntryEntities);

    List<BlogEntryDto> blogEntries = blogServiceToTest.findEntriesForBlog(1001L);

    assertAll(
        () -> assertThat(blogEntries).as("entries").hasSize(1),
        () -> assertThat(blogEntries.get(0).getCreatorName()).as("creator name").isEqualTo("you"),
        () -> assertThat(blogEntries.get(0).getEntry()).as("entry").isEqualTo("too")
    );
  }

  @Test
  @DisplayName("should save BlogDto as BlogEntity")
  void shouldSaveBlogDtoAsBlogEntity() {
    BlogDto blogDto = new BlogDto();
    blogDto.setCreated(now());
    blogDto.setTitle("some blog");
    blogDto.setUser(new UserDto());

    blogServiceToTest.saveOrUpdate(blogDto);

    ArgumentCaptor<BlogEntity> argCaptor = ArgumentCaptor.forClass(BlogEntity.class);
    verify(blogRepositoryMock).save(argCaptor.capture());
    BlogEntity blogEntity = argCaptor.getValue();

    assertAll(
        () -> assertThat(blogEntity.getCreated()).as("created").isEqualTo(now()),
        () -> assertThat(blogEntity.getTitle()).as("title").isEqualTo("some blog"),
        () -> assertThat(blogEntity.getUser()).as("user").isNotNull()
    );
  }

  @Test
  @DisplayName("should save BlogEntryDto as BlogEntryEntity")
  void shouldSaveBlogEntryDtoAsBlogEntryEntity() {
    BlogEntryDto blogEntryDto = new BlogEntryDto();
    blogEntryDto.setBlog(new BlogDto());
    blogEntryDto.setCreatorName("me");
    blogEntryDto.setEntry("if i where a rich man...");

    blogServiceToTest.saveOrUpdate(blogEntryDto);

    ArgumentCaptor<BlogEntryEntity> argCaptor = ArgumentCaptor.forClass(BlogEntryEntity.class);
    verify(blogEntryRepositoryMock).save(argCaptor.capture());
    BlogEntryEntity blogEntryEntity = argCaptor.getValue();

    assertAll(
        () -> assertThat(blogEntryEntity.getBlog()).as("blog").isNotNull(),
        () -> assertThat(blogEntryEntity.getCreatorName()).as("creator name").isEqualTo("me"),
        () -> assertThat(blogEntryEntity.getEntry()).as("entry").isEqualTo("if i where a rich man...")
    );
  }
}
