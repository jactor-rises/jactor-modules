package com.github.jactor.rises.persistence.service;

import com.github.jactor.rises.persistence.JactorPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@DisplayName("A com.github.jactor.rises.persistence.service.JactorRestService")
class JactorPersistenceRestServiceTest {

    private MockMvc mockMvc;
    private @Autowired WebApplicationContext webApplicationContext;

    @BeforeEach void mockMvc() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @DisplayName("should greet a beautiful world")
    @Test void shouldGreet() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("{ \"greeting\" : \"hello beautiful world!\" }"));
    }
}
