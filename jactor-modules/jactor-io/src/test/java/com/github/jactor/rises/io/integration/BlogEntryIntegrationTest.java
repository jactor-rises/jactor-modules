package com.github.jactor.rises.io.integration;

import com.github.jactor.rises.client.dto.BlogEntryDto;
import com.github.jactor.rises.client.dto.UserDto;
import com.github.jactor.rises.io.ctx.JactorIo;
import com.github.jactor.rises.io.rest.BlogRestService;
import com.github.jactor.rises.io.rest.UserRestService;
import com.github.jactor.rises.test.util.SpringBootActuatorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.URL;
import java.time.LocalDateTime;

import static com.github.jactor.rises.client.dto.AddressDto.anAddress;
import static com.github.jactor.rises.client.dto.BlogDto.aBlog;
import static com.github.jactor.rises.client.dto.BlogEntryDto.aBlogEntry;
import static com.github.jactor.rises.client.dto.PersonDto.aPerson;
import static com.github.jactor.rises.client.dto.UserDto.aUser;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DisplayName("The BlogEntryIntegrationTest")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JactorIo.class)
class BlogEntryIntegrationTest {

    private @Autowired BlogRestService blogRestService;
    private @Autowired UserRestService userRestService;

    @BeforeEach void assumeJactorPersistenceRunning() throws IOException {
        assumeTrue(
                SpringBootActuatorUtil.isServerRunning("http://localhost:1099/jactor-persistence-orm/"),
                "Integration test can only run when server is 'UP'"
        );
    }

    @DisplayName("should save blog entry")
    @Test void shouldSaveBlogEntryEntity() {
        BlogEntryDto blogEntry = blogRestService.saveOrUpdate(
                createBlogEntryForPersistedUser("my blog", "some entry", "me")
        );

        assertAll(
                () -> assertThat(blogEntry.getBlog().getTitle()).as("entry.blog.title").isEqualTo("my blog"),
                () -> assertThat(blogEntry.getCreatorName()).as("entry.creator").contains("me"),
                () -> assertThat(blogEntry.getCreationTime()).as("entry.createdTime").isAfter(LocalDateTime.now().minusMinutes(1)),
                () -> assertThat(blogEntry.getEntry()).as("entry.entry").isEqualTo("some entry")
        );
    }

    @SuppressWarnings("SameParameterValue") private BlogEntryDto createBlogEntryForPersistedUser(String blogTitle, String entry, String creatorName) {
        UserDto userDto = aPersistedUser(creatorName);

        return aBlogEntry()
                .with(aBlog()
                        .withTitle(blogTitle)
                        .with(userDto)
                ).withEntry(entry)
                .withCreatorName(creatorName)
                .build();

    }

    private UserDto aPersistedUser(String username) {
        UserDto userDto = aUser()
                .withUserName(createUnique(username))
                .withEmailAddress("jactor@rises")
                .with(aPerson()
                        .withDescription("description")
                        .withSurname("jacobsen")
                        .with(anAddress()
                                .withAddressLine1("the streets")
                                .withCity("Dirdal")
                                .withCountry("NO")
                                .withZipCode(1234)
                        )
                ).build();

        return userRestService.saveOrUpdate(userDto);
    }

    private String createUnique(String username) {
        return username + "@" + LocalDateTime.now();
    }
}
