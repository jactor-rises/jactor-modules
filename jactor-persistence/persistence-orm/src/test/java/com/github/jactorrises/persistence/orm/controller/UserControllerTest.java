package com.github.jactorrises.persistence.orm.controller;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.persistence.orm.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static com.github.jactorrises.persistence.orm.entity.address.AddressEntity.anAddress;
import static com.github.jactorrises.persistence.orm.entity.person.PersonEntity.aPerson;
import static com.github.jactorrises.persistence.orm.entity.user.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration
public class UserControllerTest {

    private MockMvc mockMvc;

    @MockBean private UserService userServiceMock;

    @Before public void configureController() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userServiceMock)).build();
    }

    @Test public void shouldUseHibernateRepositoryToFindUser() throws Exception {
        mockMvc.perform(get("/user/jactor")).andExpect(status().isOk());

        verify(userServiceMock).findUsing(new UserName("jactor"));
    }

    @Test public void shouldNotFindUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/user/someone"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString())
                .isNullOrEmpty();
    }

    @Test public void shouldFindUser() throws Exception {
        when(userServiceMock.findUsing(any(UserName.class))).thenAnswer(
                invocationOnMock -> Optional.of(
                        aUser().withUserName("someone").with(
                                aPerson().with(
                                        anAddress()
                                )
                        ).build().asDto()
                )
        );

        MvcResult mvcResult = mockMvc.perform(get("/user/someone"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString())
                .contains("someone")
                .contains("person")
                .contains("address");
    }

    @Configuration
    class TestConfig {
    }
}
