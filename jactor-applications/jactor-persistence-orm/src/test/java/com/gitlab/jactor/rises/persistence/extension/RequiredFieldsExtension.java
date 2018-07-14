package com.gitlab.jactor.rises.persistence.extension;

import com.gitlab.jactor.rises.persistence.entity.address.AddressEntity;
import com.gitlab.jactor.rises.persistence.entity.blog.BlogEntity;
import com.gitlab.jactor.rises.persistence.entity.blog.BlogEntryEntity;
import com.gitlab.jactor.rises.persistence.entity.guestbook.GuestBookEntity;
import com.gitlab.jactor.rises.persistence.entity.guestbook.GuestBookEntryEntity;
import com.gitlab.jactor.rises.persistence.entity.person.PersonEntity;
import com.gitlab.jactor.rises.persistence.entity.user.UserEntity;
import com.gitlab.jactor.rises.test.extension.validate.fields.AbstractRequiredFieldsExtension;
import com.gitlab.jactor.rises.test.extension.validate.fields.ClassFieldValue;

import java.time.LocalDateTime;

import static com.gitlab.jactor.rises.persistence.entity.address.AddressEntity.anAddress;
import static com.gitlab.jactor.rises.persistence.entity.blog.BlogEntity.aBlog;
import static com.gitlab.jactor.rises.persistence.entity.guestbook.GuestBookEntity.aGuestBook;
import static com.gitlab.jactor.rises.persistence.entity.person.PersonEntity.aPerson;
import static com.gitlab.jactor.rises.persistence.entity.user.UserEntity.aUser;
import static java.util.Arrays.asList;

public class RequiredFieldsExtension extends AbstractRequiredFieldsExtension {
    private static PersonEntity aPersonWithRequiredValues() {
        return aPerson()
                .withSurname("McTest")
                .with(anAddressWithRequiredFields())
                .build();
    }

    private static AddressEntity anAddressWithRequiredFields() {
        return anAddress()
                .withAddressLine1("Test Boulevard 1")
                .withZipCode(1001)
                .withCity("Test Ville")
                .build();
    }

    private static UserEntity aUserWithRequiredFields() {
        return aUser()
                .with(aPersonWithRequiredValues())
                .withUsername(uniqueName())
                .build();
    }

    private static BlogEntity aBlogWithRequiredFields() {
        return aBlog()
                .with(aUserWithRequiredFields())
                .withTitle("here we go again")
                .build();
    }

    private static GuestBookEntity aGuestBookWithRequiredFields() {
        return aGuestBook()
                .with(aUserWithRequiredFields())
                .withTitle("home is best")
                .build();
    }

    private static String uniqueName() {
        return "testName_" + LocalDateTime.now();
    }

    static {
        AbstractRequiredFieldsExtension.withRequiredFields(UserEntity.class, asList(
                new ClassFieldValue("username", RequiredFieldsExtension::uniqueName),
                new ClassFieldValue("personEntity", RequiredFieldsExtension::aPersonWithRequiredValues)
        ));

        AbstractRequiredFieldsExtension.withRequiredFields(PersonEntity.class, asList(
                new ClassFieldValue("surname", () -> "McTest"),
                new ClassFieldValue("addressEntity", RequiredFieldsExtension::anAddressWithRequiredFields)
        ));

        AbstractRequiredFieldsExtension.withRequiredFields(AddressEntity.class, asList(
                new ClassFieldValue("addressLine1", () -> "Test Boulevard 1"),
                new ClassFieldValue("zipCode", () -> 1001),
                new ClassFieldValue("city", () -> "Testing")
        ));

        AbstractRequiredFieldsExtension.withRequiredFields(BlogEntity.class, asList(
                new ClassFieldValue("title", () -> "test title"),
                new ClassFieldValue("userEntity", RequiredFieldsExtension::aUserWithRequiredFields)
        ));

        AbstractRequiredFieldsExtension.withRequiredFields(BlogEntryEntity.class, asList(
                new ClassFieldValue("entry", () -> "jibberish"),
                new ClassFieldValue("name", () -> "McTest"),
                new ClassFieldValue("blog", RequiredFieldsExtension::aBlogWithRequiredFields)
        ));

        AbstractRequiredFieldsExtension.withRequiredFields(GuestBookEntity.class, asList(
                new ClassFieldValue("title", () -> "test title"),
                new ClassFieldValue("user", RequiredFieldsExtension::aUserWithRequiredFields)
        ));

        AbstractRequiredFieldsExtension.withRequiredFields(GuestBookEntryEntity.class, asList(
                new ClassFieldValue("entry", () -> "jibberish"),
                new ClassFieldValue("name", () -> "McTest"),
                new ClassFieldValue("guestBook", RequiredFieldsExtension::aGuestBookWithRequiredFields)
        ));
    }
}
