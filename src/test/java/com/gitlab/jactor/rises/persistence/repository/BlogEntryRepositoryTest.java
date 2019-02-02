package com.gitlab.jactor.rises.persistence.repository;

import com.gitlab.jactor.rises.persistence.JactorPersistence;
import com.gitlab.jactor.rises.persistence.entity.address.AddressEntity;
import com.gitlab.jactor.rises.persistence.entity.blog.BlogEntity;
import com.gitlab.jactor.rises.persistence.entity.blog.BlogEntryEntity;
import com.gitlab.jactor.rises.persistence.entity.person.PersonEntity;
import com.gitlab.jactor.rises.persistence.entity.user.UserEntity;
import com.gitlab.jactor.rises.test.extension.time.NowAsPureDateExtension;
import com.gitlab.jactor.rises.test.extension.validate.fields.FieldValue;
import com.gitlab.jactor.rises.test.extension.validate.fields.RequiredFieldsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.gitlab.jactor.rises.persistence.entity.address.AddressEntity.anAddress;
import static com.gitlab.jactor.rises.persistence.entity.blog.BlogEntity.aBlog;
import static com.gitlab.jactor.rises.persistence.entity.blog.BlogEntryEntity.aBlogEntry;
import static com.gitlab.jactor.rises.persistence.entity.person.PersonEntity.aPerson;
import static com.gitlab.jactor.rises.persistence.entity.user.UserEntity.aUser;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@ExtendWith(NowAsPureDateExtension.class)
@SpringBootTest(classes = {JactorPersistence.class})
@Transactional
@DisplayName("A BlogEntryRepository")
class BlogEntryRepositoryTest {

    @RegisterExtension RequiredFieldsExtension requiredFieldsExtension = new RequiredFieldsExtension(Map.of(
            UserEntity.class, asList(
                    new FieldValue("username", () -> "unique@" + LocalDateTime.now()),
                    new FieldValue("personEntity", () -> aPerson().build())
            ), PersonEntity.class, asList(
                    new FieldValue("addressEntity", () -> anAddress().build()),
                    new FieldValue("surname", "sure, man")
            ), AddressEntity.class, asList(
                    new FieldValue("addressLine1", "Test Boulevard 1"),
                    new FieldValue("zipCode", 1001),
                    new FieldValue("city", "Testing")
            ), BlogEntity.class, asList(
                    new FieldValue("title", "by awesome blog"),
                    new FieldValue("userEntity", () -> aUser().build())
            )
    ));

    private @Autowired BlogEntryRepository blogEntryRepository;
    private @Autowired BlogRepository blogRepository;
    private @Autowired EntityManager entityManager;

    @DisplayName("should save then read blog entry")
    @Test void shouldSaveThenReadBlogEntry() {
        BlogEntryEntity blogEntryToSave = aBlogEntry()
                .with(aBlog())
                .withCreatorName("smith")
                .withEntry("once upon a time")
                .build();

        blogEntryRepository.save(blogEntryToSave);
        entityManager.flush();
        entityManager.clear();

        BlogEntryEntity blogEntryById = blogEntryRepository.findById(blogEntryToSave.getId()).orElseThrow(this::entryNotFound);

        assertAll(
                () -> assertThat(blogEntryById.getCreatorName()).as("entry.creatorName").isEqualTo("smith"),
                () -> assertThat(blogEntryById.getCreationTime()).as("entry.creationTime").isEqualTo(LocalDate.now().atStartOfDay()),
                () -> assertThat(blogEntryById.getEntry()).as("entry.entry").isEqualTo("once upon a time")
        );
    }

    @DisplayName("should write then update and read a blog entry")
    @Test void shouldWriteThenUpdateAndReadBlogEntry() {
        BlogEntryEntity blogEntryToSave = aBlogEntry()
                .with(aBlog())
                .withCreatorName("smith")
                .withEntry("once upon a time")
                .build();

        blogEntryRepository.save(blogEntryToSave);
        entityManager.flush();
        entityManager.clear();

        BlogEntryEntity blogEntryById = blogEntryRepository.findById(blogEntryToSave.getId()).orElseThrow(this::entryNotFound);

        blogEntryById.setCreatorName("luke");
        blogEntryById.update("happily ever after");

        blogEntryRepository.save(blogEntryById);

        Optional<BlogEntryEntity> updatedEntry = blogEntryRepository.findById(blogEntryToSave.getId());
        entityManager.flush();
        entityManager.clear();

        assertAll(
                () -> assertThat(updatedEntry).as("updatedEntry").isPresent(),
                () -> {
                    BlogEntryEntity blogEntryEntity = updatedEntry.orElseThrow(this::entryNotFound);

                    assertAll(
                            () -> assertThat(blogEntryEntity.getCreatorName()).as("entry.creatorName").isEqualTo("luke"),
                            () -> assertThat(blogEntryEntity.getUpdatedTime()).as("entry.updatedTime").isEqualTo(LocalDate.now().atStartOfDay()),
                            () -> assertThat(blogEntryEntity.getEntry()).as("entry.entry").isEqualTo("happily ever after")
                    );
                }
        );
    }

    @DisplayName("should write two entries and on two blogs then find entry for the right blog")
    @Test void shouldWriteTwoEntriesOnTwoBlogsThenFindEntryOnBlog() {
        blogEntryRepository.save(
                aBlogEntry()
                        .with(aBlog())
                        .withCreatorName("someone")
                        .withEntry("jadda")
                        .build()
        );

        BlogEntity knownBlog = aBlog().build();
        blogRepository.save(knownBlog);

        BlogEntryEntity blogEntryToSave = aBlogEntry()
                .with(knownBlog)
                .withCreatorName("shrek")
                .withEntry("far far away")
                .build();

        blogEntryRepository.save(blogEntryToSave);
        entityManager.flush();
        entityManager.clear();

        List<BlogEntryEntity> entriesByBlog = blogEntryRepository.findByBlog_Id(knownBlog.getId());

        assertAll(
                () -> assertThat(blogEntryRepository.findAll()).as("all entries").hasSize(2),
                () -> assertThat(entriesByBlog).as("entriesByBlog").hasSize(1),
                () -> assertThat(entriesByBlog.get(0).getCreatorName()).as("entry.creatorName").isEqualTo("shrek"),
                () -> assertThat(entriesByBlog.get(0).getEntry()).as("entry.entry").isEqualTo("far far away")
        );
    }

    private AssertionError entryNotFound() {
        return new AssertionError("Blog Entry not found");
    }
}
