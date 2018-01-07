package com.github.jactorrises.persistence.orm.controller;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jactorrises.client.dto.BlogDto;
import com.github.jactorrises.client.dto.BlogEntryDto;
import com.github.jactorrises.persistence.orm.service.BlogService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class BlogControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private BlogService blogServiceMock;

    @Before public void configureController() {
        mockMvc = MockMvcBuilders.standaloneSetup(new BlogController(blogServiceMock)).build();
    }

    @Test public void shouldFetchBlog() throws Exception {
        mockMvc.perform(get("/blog/get/1")).andExpect(status().isOk());

        verify(blogServiceMock).fetch(1L);
    }

    @Test public void shouldPersistBlog() throws Exception {
        BlogDto blogDto = new BlogDto();
        BlogDto savedBlogDto = new BlogDto();
        savedBlogDto.setId(110L);

        when(blogServiceMock.saveOrUpdate(any(BlogDto.class))).thenReturn(savedBlogDto);

        mockMvc.perform(post("/blog/persist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsBytes(blogDto))
        )
                .andExpect(header().stringValues("/blog/get/110"))
                .andExpect(status().isCreated());

        verify(blogServiceMock).saveOrUpdate(any(BlogDto.class));
    }

    @Test public void shouldUpdateBlog() throws Exception {
        BlogDto blogDto = new BlogDto();
        blogDto.setId(110L);

        when(blogServiceMock.saveOrUpdate(any(BlogDto.class))).thenReturn(blogDto);

        mockMvc.perform(post("/blog/persist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsBytes(blogDto))
        )
                .andExpect(status().isOk());

        verify(blogServiceMock).saveOrUpdate(any(BlogDto.class));
    }

    @Test public void shouldPersistBlogEntry() throws Exception {
        BlogEntryDto blogEntryDto = new BlogEntryDto();
        BlogEntryDto savedBlogDto = new BlogEntryDto();
        savedBlogDto.setId(110L);

        when(blogServiceMock.saveOrUpdate(any(BlogEntryDto.class))).thenReturn(savedBlogDto);

        mockMvc.perform(post("/blog/entry/persist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsBytes(blogEntryDto))
        )
                .andExpect(header().stringValues("/blog/entry/get/110"))
                .andExpect(status().isCreated());

        verify(blogServiceMock).saveOrUpdate(any(BlogEntryDto.class));
    }

    @Test public void shouldUpdateBlogEntry() throws Exception {
        BlogEntryDto blogEntryDto = new BlogEntryDto();
        blogEntryDto.setId(110L);

        when(blogServiceMock.saveOrUpdate(any(BlogEntryDto.class))).thenReturn(blogEntryDto);

        mockMvc.perform(post("/blog/entry/persist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsBytes(blogEntryDto))
        )
                .andExpect(status().isOk());

        verify(blogServiceMock).saveOrUpdate(any(BlogEntryDto.class));
    }
}