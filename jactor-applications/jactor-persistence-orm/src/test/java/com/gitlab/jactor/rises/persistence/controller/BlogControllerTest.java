package com.gitlab.jactor.rises.persistence.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.jactor.rises.commons.dto.BlogDto;
import com.gitlab.jactor.rises.commons.dto.BlogEntryDto;
import com.gitlab.jactor.rises.persistence.JactorPersistence;
import com.gitlab.jactor.rises.persistence.service.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@DisplayName("A BlogController")
class BlogControllerTest {

    private MockMvc mockMvc;
    private @MockBean BlogService blogServiceMock;
    private @Autowired ObjectMapper objectMapper;

    @BeforeEach void mockMvc() {
        mockMvc = standaloneSetup(new BlogController(blogServiceMock)).build();
    }

    @DisplayName("should find a blog")
    @Test void shouldFindBlog() throws Exception {
        when(blogServiceMock.find(1L)).thenReturn(Optional.of(new BlogDto()));

        mockMvc.perform(get("/blog/get/1")).andExpect(status().isOk());
    }

    @DisplayName("should not find a blog")
    @Test void shouldNotFindBlog() throws Exception {
        when(blogServiceMock.find(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/blog/get/1")).andExpect(status().isNoContent());
    }

    @DisplayName("should find a blog entry")
    @Test void shouldFindBlogEntry() throws Exception {
        when(blogServiceMock.findEntryBy(1L)).thenReturn(Optional.of(new BlogEntryDto()));

        mockMvc.perform(get("/blog/entry/get/1")).andExpect(status().isOk());
    }

    @DisplayName("should not find a blog entry")
    @Test void shouldNotFindBlogEntry() throws Exception {
        when(blogServiceMock.findEntryBy(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/blog/entry/get/1")).andExpect(status().isNoContent());
    }

    @DisplayName("should not find blogs by title")
    @Test void shouldNotFindBlogs() throws Exception {
        when(blogServiceMock.findBlogsBy("Anything")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/blog/find/Anything")).andExpect(status().isNoContent());
    }

    @DisplayName("should find blogs by title")
    @Test void shouldFindBlogs() throws Exception {
        when(blogServiceMock.findBlogsBy("Anything")).thenReturn(Collections.singletonList(new BlogDto()));

        mockMvc.perform(get("/blog/find/Anything")).andExpect(status().isOk());
    }

    @DisplayName("should not find blog entries by blog id")
    @Test void shouldNotFindBlogEntries() throws Exception {
        when(blogServiceMock.findEntriesForBlog(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/blog/1/entries/find")).andExpect(status().isNoContent());
    }

    @DisplayName("should find blog entries by blog id")
    @Test void shouldFindBlogEntries() throws Exception {
        when(blogServiceMock.findEntriesForBlog(1L)).thenReturn(Collections.singletonList(new BlogEntryDto()));

        mockMvc.perform(get("/blog/1/entries/find")).andExpect(status().isOk());
    }

    @DisplayName("should persist changes to existing blog")
    @Test void shouldPersistChangesToExistingBlog() throws Exception {
        BlogDto blogDto = new BlogDto();
        blogDto.setId(1L);

        mockMvc.perform(post("/blog/persist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(blogDto))
        ).andExpect(status().isOk());

        verify(blogServiceMock).saveOrUpdate(any(BlogDto.class));
    }

    @DisplayName("should create a blog")
    @Test void shouldCreateBlog() throws Exception {
        BlogDto blogDto = new BlogDto();
        BlogDto createdDto = new BlogDto();
        createdDto.setId(1L);

        when(blogServiceMock.saveOrUpdate(any(BlogDto.class))).thenReturn(createdDto);

        mockMvc.perform(post("/blog/persist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(blogDto))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(equalTo(1))));
    }

    @DisplayName("should persist changes to existing blog entry")
    @Test void shouldPersistChangesToExistingBlogEntry() throws Exception {
        BlogEntryDto blogEntryDto = new BlogEntryDto();
        blogEntryDto.setId(1L);

        mockMvc.perform(post("/blog/entry/persist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(blogEntryDto))
        ).andExpect(status().isOk());

        verify(blogServiceMock).saveOrUpdate(any(BlogEntryDto.class));
    }

    @DisplayName("should create blog entry")
    @Test void shouldCreateBlogEntry() throws Exception {
        BlogEntryDto blogEntryDto = new BlogEntryDto();
        BlogEntryDto createdDto = new BlogEntryDto();
        createdDto.setId(1L);

        when(blogServiceMock.saveOrUpdate(any(BlogEntryDto.class))).thenReturn(createdDto);

        mockMvc.perform(post("/blog/entry/persist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(blogEntryDto))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(equalTo(1))));

        verify(blogServiceMock).saveOrUpdate(any(BlogEntryDto.class));
    }
}
