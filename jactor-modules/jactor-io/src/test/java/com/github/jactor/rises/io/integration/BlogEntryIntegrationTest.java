package com.github.jactor.rises.io.integration;

import com.github.jactor.rises.client.dto.BlogDto;
import com.github.jactor.rises.client.dto.BlogEntryDto;
import com.github.jactor.rises.client.dto.UserDto;
import com.github.jactor.rises.io.rest.BlogRestService;
import com.github.jactor.rises.io.rest.UserRestService;
import com.github.jactor.rises.test.extension.time.NowAsPureDateExtension;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.github.jactor.rises.client.dto.AddressDto.anAddress;
import static com.github.jactor.rises.client.dto.BlogDto.aBlog;
import static com.github.jactor.rises.client.dto.BlogEntryDto.aBlogEntry;
import static com.github.jactor.rises.client.dto.PersonDto.aPerson;
import static com.github.jactor.rises.client.dto.UserDto.aUser;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(NowAsPureDateExtension.class)
@SpringBootTest(classes = {Object.class})
@Transactional
@Disabled("#193: spring-context")
class BlogEntryIntegrationTest {

    @Autowired private BlogRestService blogRestService;

    @Autowired private UserRestService userRestService;

    @Test void willSaveBlogEntryEntityToThePersistentLayer() {
        BlogDto blog = blogRestService.saveOrUpdate(
                createBlogForPersistedUser("my blog", "some entry", "me")
        );

        BlogEntryDto blogEntry = blog.getEntries().iterator().next();

        assertThat(blogEntry.getBlog().getTitle()).as("blog.title").isEqualTo("my blog");
        assertThat(blogEntry.getCreatorName()).as("entry.creator").isEqualTo("me");
        assertThat(blogEntry.getCreationTime()).as("entry.createdTime").isEqualTo(LocalDate.now().atStartOfDay());
        assertThat(blogEntry.getEntry()).as("entry.entry").isEqualTo("some entry");
    }

    @SuppressWarnings("SameParameterValue") private BlogDto createBlogForPersistedUser(String blogTitle, String entry, String creatorName) {
        UserDto userDto = aPersistedUser(creatorName);
        BlogDto blogDto = aBlog()
                .with(userDto)
                .withTitle(blogTitle)
                .with(aBlogEntry()
                        .withEntry(entry)
                        .withCreatorName(creatorName)
                ).build();
        blogDto.setUser(userDto);

        return blogDto;
    }

    private UserDto aPersistedUser(String creatorName) {
        UserDto userDto = aUser()
                .withUserName(creatorName)
                .withEmailAddress("jactor@rises")
                .with(aPerson()
                        .withDescription("description")
                        .withSurname("smith")
                        .with(anAddress()
                                .withAddressLine1("the streets")
                                .withAddressLine2("with")
                                .withAddressLine3("no name")
                                .withCity("Ocean")
                                .withCountry("NO")
                                .withZipCode(1234)
                        )
                ).build();

        return userRestService.saveOrUpdate(userDto);
    }
}
