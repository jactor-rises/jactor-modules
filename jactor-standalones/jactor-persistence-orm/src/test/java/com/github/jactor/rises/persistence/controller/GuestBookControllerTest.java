package com.github.jactor.rises.persistence.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jactor.rises.client.dto.NewGuestBookDto;
import com.github.jactor.rises.client.dto.NewGuestBookEntryDto;
import com.github.jactor.rises.persistence.JactorPersistence;
import com.github.jactor.rises.persistence.service.GuestBookService;
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
@DisplayName("A GuestBookController")
class GuestBookControllerTest {

    private MockMvc mockMvc;
    private @MockBean GuestBookService guestBookServiceMock;
    private @Autowired ObjectMapper objectMapper;

    @BeforeEach void mockMvc() {
        mockMvc = standaloneSetup(new GuestBookController(guestBookServiceMock)).build();
    }

    @DisplayName("should not find a guest book")
    @Test void shouldNotFindGuestBook() throws Exception {
        when(guestBookServiceMock.find(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/guestBook/get/1")).andExpect(status().isNoContent());
    }

    @DisplayName("should find a guest book")
    @Test void shouldFindGuestBook() throws Exception {
        when(guestBookServiceMock.find(1L)).thenReturn(Optional.of(new NewGuestBookDto()));

        mockMvc.perform(get("/guestBook/get/1")).andExpect(status().isOk());
    }

    @DisplayName("should not find a guest book entry")
    @Test void shouldNotFindGuestBookEntry() throws Exception {
        when(guestBookServiceMock.findEntry(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/guestBook/get/entry/1")).andExpect(status().isNoContent());
    }

    @DisplayName("should find a guest book entry")
    @Test void shouldFindGuestBookEntry() throws Exception {
        when(guestBookServiceMock.findEntry(1L)).thenReturn(Optional.of(new NewGuestBookEntryDto()));

        mockMvc.perform(get("/guestBook/get/entry/1")).andExpect(status().isOk());
    }

    @DisplayName("should persist changes to existing guest book")
    @Test void shouldPersistChangesToExistingGuestBook() throws Exception {
        NewGuestBookDto guestBookDto = new NewGuestBookDto();
        guestBookDto.setId(1L);

        mockMvc.perform(post("/guestBook/persist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(guestBookDto))
        ).andExpect(status().isOk());

        verify(guestBookServiceMock).saveOrUpdate(any(NewGuestBookDto.class));
    }

    @DisplayName("should create a guest book")
    @Test void shouldCreateGuestBook() throws Exception {
        NewGuestBookDto guestBookDto = new NewGuestBookDto();
        NewGuestBookDto createdDto = new NewGuestBookDto();
        createdDto.setId(1L);

        when(guestBookServiceMock.saveOrUpdate(any(NewGuestBookDto.class))).thenReturn(createdDto);

        mockMvc.perform(post("/guestBook/persist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(guestBookDto))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(equalTo(1))));
    }

    @DisplayName("should persist changes to existing guest book entry")
    @Test void shouldPersistChangesToExistingGuestBookEntry() throws Exception {
        NewGuestBookEntryDto guestBookEntryDto = new NewGuestBookEntryDto();
        guestBookEntryDto.setId(1L);

        mockMvc.perform(post("/guestBook/entry/persist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(guestBookEntryDto))
        ).andExpect(status().isOk());

        verify(guestBookServiceMock).saveOrUpdate(any(NewGuestBookEntryDto.class));
    }

    @DisplayName("should create a guest book entry")
    @Test void shouldCreateGuestBookEntry() throws Exception {
        NewGuestBookEntryDto guestBookEntryDto = new NewGuestBookEntryDto();
        NewGuestBookEntryDto createdDto = new NewGuestBookEntryDto();
        createdDto.setId(1L);

        when(guestBookServiceMock.saveOrUpdate(any(NewGuestBookEntryDto.class))).thenReturn(createdDto);

        mockMvc.perform(post("/guestBook/entry/persist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(guestBookEntryDto))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(equalTo(1))));
    }
}
