package com.github.jactor.rises.persistence.orm.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jactor.rises.client.datatype.UserName;
import com.github.jactor.rises.client.dto.UserDto;
import com.github.jactor.rises.persistence.orm.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static com.github.jactor.rises.persistence.orm.entity.address.AddressEntity.anAddress;
import static com.github.jactor.rises.persistence.orm.entity.person.PersonEntity.aPerson;
import static com.github.jactor.rises.persistence.orm.entity.user.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @MockBean private UserService userServiceMock;

    @Before public void configureController() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userServiceMock)).build();
    }

    @Test public void shouldNotFindUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/user/find/someone"))
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString())
                .isNullOrEmpty();
    }

    @Test public void shouldFindUser() throws Exception {
        when(userServiceMock.findUsing(any(UserName.class))).thenAnswer(
                invocationOnMock -> Optional.of(
                        aUser().withUserName("jactor").with(
                                aPerson().with(
                                        anAddress()
                                )
                        ).build().asDto()
                )
        );

        MvcResult mvcResult = mockMvc.perform(get("/user/find/jactor"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString())
                .contains("jactor")
                .contains("person")
                .contains("address");

        verify(userServiceMock).findUsing(new UserName("jactor"));
    }

    @Test public void shouldFetchUser() throws Exception {
        mockMvc.perform(get("/user/get/1")).andExpect(status().isOk());

        verify(userServiceMock).fetch(1L);
    }

    @Test public void shouldPersistUser() throws Exception {
        UserDto userDto = new UserDto();
        UserDto savedUserDto = new UserDto();
        savedUserDto.setId(110L);

        when(userServiceMock.saveOrUpdate(any(UserDto.class))).thenReturn(savedUserDto);

        mockMvc.perform(post("/user/persist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsBytes(userDto))
        )
                .andExpect(header().stringValues("/user/get/110"))
                .andExpect(status().isCreated());

        verify(userServiceMock).saveOrUpdate(any(UserDto.class));
    }

    @Test public void shouldUpdateUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(110L);

        when(userServiceMock.saveOrUpdate(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/user/persist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsBytes(userDto))
        )
                .andExpect(status().isOk());

        verify(userServiceMock).saveOrUpdate(any(UserDto.class));
    }
}
