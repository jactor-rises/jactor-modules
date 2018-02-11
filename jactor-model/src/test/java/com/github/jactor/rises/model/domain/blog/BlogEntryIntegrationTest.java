package com.github.jactor.rises.model.domain.blog;

import com.github.jactor.rises.client.converter.FieldConverter;
import com.github.jactor.rises.client.dto.BlogDto;
import com.github.jactor.rises.client.dto.BlogEntryDto;
import com.github.jactor.rises.client.dto.UserDto;
import com.github.jactor.rises.persistence.beans.PersistenceBeans;
import com.github.jactor.rises.persistence.beans.service.BlogRestService;
import com.github.jactor.rises.persistence.beans.service.UserRestService;
import com.github.jactor.rises.persistence.orm.PersistenceOrmApplication;
import org.assertj.core.api.Condition;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.github.jactor.rises.model.domain.address.AddressDomain.anAddress;
import static com.github.jactor.rises.model.domain.blog.BlogDomain.aBlog;
import static com.github.jactor.rises.model.domain.blog.BlogEntryDomain.aBlogEntry;
import static com.github.jactor.rises.model.domain.person.PersonDomain.aPerson;
import static com.github.jactor.rises.model.domain.user.UserDomain.aUser;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PersistenceOrmApplication.class, PersistenceBeans.class})
@Transactional
@Ignore("#181: rewrite as spring-boot test in PersistenceOrmApplication")
public class BlogEntryIntegrationTest {

    @Autowired private BlogRestService blogRestService;

    @Autowired private UserRestService userRestService;

    @Test public void willSaveBlogEntryEntityToThePersistentLayer() {
        BlogDto blog = blogRestService.saveOrUpdate(
                createBlogForPersistedUser("my blog", "some entry", "me")
        );

        BlogEntryDto blogEntry = blog.getEntries().iterator().next();

        assertThat(blogEntry.getBlog().getTitle()).as("blog.title").isEqualTo("my blog");
        assertThat(blogEntry.getCreatorName()).as("entry.creator").isEqualTo("me");
        assertThat(blogEntry.getCreatedTime()).as("entry.createdTime").is(withinThisMinute());
        assertThat(blogEntry.getEntry()).as("entry.entry").isEqualTo("some entry");
    }

    private Condition<? super String> withinThisMinute() {
        return new Condition<>(
                localDateTime -> LocalDateTime.now().withNano(0).withSecond(0).isBefore(FieldConverter.convertDateTime(localDateTime)),
                "within this minute"
        );
    }

    @SuppressWarnings("SameParameterValue") private BlogDto createBlogForPersistedUser(String blogTitle, String entry, String creatorName) {
        UserDto userDto = aPersistedUser(creatorName);

        return aBlog().with(userDto).withTitleAs(blogTitle).with(
                aBlogEntry().withEntry(entry).withCreatorName(creatorName)
        ).build().getDto();
    }

    private UserDto aPersistedUser(String creatorName) {
        UserDto userDto = aUser().withUserName(creatorName)
                .withEmailAddress("jactor@rises")
                .with(aPerson()
                        .withDescription("description")
                        .withSurname("jacobsen")
                        .with(anAddress().withAddressLine1("the streets")
                                .withCity("Dirdal")
                                .withCountry("NO")
                                .withZipCode(1234)
                        )
                )
                .build().getDto();

        return userRestService.saveOrUpdate(userDto);
    }
}
