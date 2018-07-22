package com.gitlab.jactor.rises.persistence.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.jactor.rises.commons.dto.UserDto;
import com.gitlab.jactor.rises.persistence.JactorPersistence;
import com.gitlab.jactor.rises.persistence.service.UserService;
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

import static java.util.Arrays.asList;
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
@DisplayName("A UserController")
class UserControllerTest {

    private MockMvc mockMvc;
    private @MockBean UserService userServiceMock;
    private @Autowired ObjectMapper objectMapper;

    @BeforeEach void mockMvc() {
        mockMvc = standaloneSetup(new UserController(userServiceMock)).build();
    }

    @DisplayName("should not find a user by username")
    @Test void shouldNotFindByUsername() throws Exception {
        when(userServiceMock.find("me")).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/find/me")).andExpect(status().isNoContent());
    }

    @DisplayName("should find a user by username")
    @Test void shouldFindByUsername() throws Exception {
        when(userServiceMock.find("me")).thenReturn(Optional.of(new UserDto()));

        mockMvc.perform(get("/user/find/me")).andExpect(status().isOk());
    }

    @DisplayName("should not find a user by id")
    @Test void shouldNotFindById() throws Exception {
        when(userServiceMock.find(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/get/1")).andExpect(status().isNoContent());
    }

    @DisplayName("should find a user by id")
    @Test void shouldFindById() throws Exception {
        when(userServiceMock.find(1L)).thenReturn(Optional.of(new UserDto()));

        mockMvc.perform(get("/user/get/1")).andExpect(status().isOk());
    }

    @DisplayName("should persist changes to existing user")
    @Test void shouldPersistChangesToExistingUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);

        mockMvc.perform(post("/user/persist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(userDto))
        ).andExpect(status().isOk());

        verify(userServiceMock).saveOrUpdate(any(UserDto.class));
    }

    @DisplayName("should create a user")
    @Test void shouldCreateGuestUser() throws Exception {
        UserDto userDto = new UserDto();
        UserDto createdDto = new UserDto();
        createdDto.setId(1L);

        when(userServiceMock.saveOrUpdate(any(UserDto.class))).thenReturn(createdDto);

        mockMvc.perform(post("/user/persist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(userDto))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(equalTo(1))));
    }

    @DisplayName("should find all usernames on active users")
    @Test void shouldFindAllUsernames() throws Exception {
        when(userServiceMock.findUsernamesOnActiveUsers())
                .thenReturn(asList("bart", "lisa"));

        mockMvc.perform(get("/user/all/usernames")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("[0]", is(equalTo("bart"))))
                .andExpect(jsonPath("[1]", is(equalTo("lisa"))));
    }
}