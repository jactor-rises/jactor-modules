package com.github.jactor.rises.model.service.rest;

import com.github.jactor.rises.client.dto.BlogDto;
import com.github.jactor.rises.client.dto.BlogEntryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("A BlogRestService")
class BlogRestServiceTest {

    @Mock
    private RestTemplate restTemplateMock;

    private BlogRestService blogRestService;

    @BeforeEach
    void initAndMock() {
        MockitoAnnotations.initMocks(this);
        blogRestService = new BlogRestService(restTemplateMock, "/blog");
    }

    @DisplayName("should use RestTemplate GET to fetch a blog")
    @Test void shouldUseRestTemplateGetToFetchBlog() {
        when(restTemplateMock.getForEntity(anyString(), eq(BlogDto.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        blogRestService.fetch(1L);

        verify(restTemplateMock).getForEntity(eq("/blog/1"), eq(BlogDto.class));
    }

    @DisplayName("should use RestTemplate GET to fetch a blog entry")
    @Test void shouldUseRestTemplateGetToFetchBlogEntry() {
        when(restTemplateMock.getForEntity(anyString(), eq(BlogEntryDto.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        blogRestService.fetchEntry(1L);

        verify(restTemplateMock).getForEntity(eq("/blog/entry/1"), eq(BlogEntryDto.class));
    }

    @DisplayName("should exchange blog values with HttpMethod.POST when persisting")
    @Test void shouldExchangeBlogValuesWithHttpPostWhenSaveOrUpdate() {
        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(BlogDto.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        BlogDto blogDto = new BlogDto();
        blogRestService.saveOrUpdate(blogDto);

        verify(restTemplateMock).exchange(
                eq("/blog/persist"), eq(HttpMethod.POST), eq(new HttpEntity<>(blogDto)), eq(BlogDto.class)
        );
    }

    @DisplayName("should exchange blog entry values with HttpMethod.POST when persisting")
    @Test void shouldExchangeBlogEntryValuesWithHttpPostWhenSaveOrUpdate() {
        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(BlogEntryDto.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        BlogEntryDto blogEntryDto = new BlogEntryDto();
        blogRestService.saveOrUpdate(blogEntryDto);

        verify(restTemplateMock).exchange(
                eq("/blog/entry/persist"), eq(HttpMethod.POST), eq(new HttpEntity<>(blogEntryDto)), eq(BlogEntryDto.class)
        );
    }
}