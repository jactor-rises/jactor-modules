package com.github.jactor.rises.persistence.extension;

import com.github.jactor.rises.persistence.entity.address.AddressEntity;
import com.github.jactor.rises.persistence.entity.blog.BlogEntity;
import com.github.jactor.rises.persistence.entity.person.PersonEntity;
import com.github.jactor.rises.persistence.entity.user.UserEntity;
import com.github.jactor.rises.test.extension.validate.fields.AbstractRequiredFieldsExtension;
import com.github.jactor.rises.test.extension.validate.fields.ClassFieldValue;

import java.time.LocalDateTime;

import static com.github.jactor.rises.persistence.entity.address.AddressEntity.anAddress;
import static com.github.jactor.rises.persistence.entity.person.PersonEntity.aPerson;
import static com.github.jactor.rises.persistence.entity.user.UserEntity.aUser;
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
                .withUserName(uniqueName())
                .build();
    }

    private static String uniqueName() {
        return "testName@" + LocalDateTime.now();
    }

    static {
        AbstractRequiredFieldsExtension.withRequiredFields(UserEntity.class, asList(
                new ClassFieldValue("userName", RequiredFieldsExtension::uniqueName),
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
     }
}
