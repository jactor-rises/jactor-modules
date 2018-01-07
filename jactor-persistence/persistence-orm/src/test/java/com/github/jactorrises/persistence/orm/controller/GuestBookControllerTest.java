package com.github.jactorrises.persistence.orm.controller;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jactorrises.client.dto.GuestBookDto;
import com.github.jactorrises.client.dto.GuestBookEntryDto;
import com.github.jactorrises.persistence.orm.service.GuestBookService;
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
public class GuestBookControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private GuestBookService guestBookService;

    @Before public void configureController() {
        mockMvc = MockMvcBuilders.standaloneSetup(new GuestBookController(guestBookService)).build();
    }

    @Test public void shouldFetchBlog() throws Exception {
        mockMvc.perform(get("/guestBook/get/1")).andExpect(status().isOk());

        verify(guestBookService).fetch(1L);
    }

    @Test public void shouldPersistBuestBook() throws Exception {
        GuestBookDto guestBookDto = new GuestBookDto();
        GuestBookDto savedGuestBookDto = new GuestBookDto();
        savedGuestBookDto.setId(110L);

        when(guestBookService.saveOrUpdate(any(GuestBookDto.class))).thenReturn(savedGuestBookDto);

        mockMvc.perform(post("/guestBook/persist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsBytes(guestBookDto))
        )
                .andExpect(header().stringValues("/guestBook/get/110"))
                .andExpect(status().isCreated());

        verify(guestBookService).saveOrUpdate(any(GuestBookDto.class));
    }

    @Test public void shouldUpdateGuestBook() throws Exception {
        GuestBookDto guestBookDto = new GuestBookDto();
        guestBookDto.setId(110L);

        when(guestBookService.saveOrUpdate(any(GuestBookDto.class))).thenReturn(guestBookDto);

        mockMvc.perform(post("/guestBook/persist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsBytes(guestBookDto))
        )
                .andExpect(status().isOk());

        verify(guestBookService).saveOrUpdate(any(GuestBookDto.class));
    }

    @Test public void shouldPersistBuestBookEntry() throws Exception {
        GuestBookEntryDto guestBookEntryDto = new GuestBookEntryDto();
        GuestBookEntryDto savedGuestBookEntryDto = new GuestBookEntryDto();
        savedGuestBookEntryDto.setId(110L);

        when(guestBookService.saveOrUpdate(any(GuestBookEntryDto.class))).thenReturn(savedGuestBookEntryDto);

        mockMvc.perform(post("/guestBook/entry/persist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsBytes(guestBookEntryDto))
        )
                .andExpect(header().stringValues("/guestBook/entry&get/110"))
                .andExpect(status().isCreated());

        verify(guestBookService).saveOrUpdate(any(GuestBookEntryDto.class));
    }

    @Test public void shouldUpdateGuestBookEntry() throws Exception {
        GuestBookEntryDto guestBookEntryDto = new GuestBookEntryDto();
        guestBookEntryDto.setId(110L);

        when(guestBookService.saveOrUpdate(any(GuestBookEntryDto.class))).thenReturn(guestBookEntryDto);

        mockMvc.perform(post("/guestBook/entry/persist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsBytes(guestBookEntryDto))
        )
                .andExpect(status().isOk());

        verify(guestBookService).saveOrUpdate(any(GuestBookEntryDto.class));
    }
}