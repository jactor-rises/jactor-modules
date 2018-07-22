package com.gitlab.jactor.rises.persistence.service;

import com.gitlab.jactor.rises.commons.dto.BlogDto;
import com.gitlab.jactor.rises.commons.dto.BlogEntryDto;
import com.gitlab.jactor.rises.commons.dto.UserDto;
import com.gitlab.jactor.rises.persistence.entity.blog.BlogEntity;
import com.gitlab.jactor.rises.persistence.entity.blog.BlogEntryEntity;
import com.gitlab.jactor.rises.persistence.extension.RequiredFieldsExtension;
import com.gitlab.jactor.rises.persistence.repository.BlogEntryRepository;
import com.gitlab.jactor.rises.persistence.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static com.gitlab.jactor.rises.persistence.entity.blog.BlogEntity.aBlog;
import static com.gitlab.jactor.rises.persistence.entity.blog.BlogEntryEntity.aBlogEntry;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("A BlogService")
@ExtendWith(RequiredFieldsExtension.class)
class BlogServiceTest {

    private @InjectMocks BlogService blogServiceToTest;
    private @Mock BlogRepository blogRepositoryMock;
    private @Mock BlogEntryRepository blogEntryRepositoryMock;
    private @Mock @SuppressWarnings("unused") /* used by mockito */ UserService userServiceMock;

    @BeforeEach void initMocking() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("should map blog to dto")
    @Test void shouldMapBlogToDto() {
        Optional<BlogEntity> blogEntity = Optional.of(aBlog().withTitle("full speed ahead").build());
        when(blogRepositoryMock.findById(1001L)).thenReturn(blogEntity);

        BlogDto blog = blogServiceToTest.find(1001L).orElseThrow(mockError());

        assertThat(blog.getTitle()).as("title").isEqualTo("full speed ahead");
    }

    @DisplayName("should map blog entry to dto")
    @Test void shouldMapFoundBlogToDto() {
        Optional<BlogEntryEntity> anEntry = Optional.of(aBlogEntry().withCreatorName("me").withEntry("too").build());
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

    @DisplayName("should find blogs for title")
    @Test void shouldFindBlogsForTitle() {
        List<BlogEntity> blogsToFind = Collections.singletonList(aBlog().withTitle("Star Wars").build());
        when(blogRepositoryMock.findBlogsByTitle("Star Wars")).thenReturn(blogsToFind);

        List<BlogDto> blogForTitle = blogServiceToTest.findBlogsBy("Star Wars");

        assertThat(blogForTitle).hasSize(1);
    }

    @DisplayName("should map blog entries to a list of dto")
    @Test void shouldMapBlogEntriesToListOfDto() {
        List<BlogEntryEntity> blogEntryEntities = Collections.singletonList(aBlogEntry().withCreatorName("you").withEntry("too").build());
        when(blogEntryRepositoryMock.findByBlog_Id(1001L)).thenReturn(blogEntryEntities);

        List<BlogEntryDto> blogEntries = blogServiceToTest.findEntriesForBlog(1001L);

        assertAll(
                () -> assertThat(blogEntries).as("entries").hasSize(1),
                () -> assertThat(blogEntries.get(0).getCreatorName()).as("creator name").isEqualTo("you"),
                () -> assertThat(blogEntries.get(0).getEntry()).as("entry").isEqualTo("too")
        );
    }

    @DisplayName("should save BlogDto as BlogEntity")
    @Test void shouldSaveBlogDtoAsBlogEntity() {
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

    @DisplayName("should save BlogEntryDto as BlogEntryEntity")
    @Test void shouldSaveBlogEntryDtoAsBlogEntryEntity() {
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
