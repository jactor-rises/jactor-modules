package com.github.jactor.rises.persistence.orm;

import com.github.jactor.rises.persistence.orm.entity.address.AddressEntity;
import com.github.jactor.rises.persistence.orm.entity.person.PersonEntity;
import com.github.jactor.rises.persistence.orm.entity.user.UserEntity;
import com.github.jactor.rises.test.extension.fields.AbstractRequiredFieldsExtension;
import com.github.jactor.rises.test.extension.fields.ClassFieldValue;

import java.time.LocalDateTime;

import static com.github.jactor.rises.persistence.orm.entity.address.AddressEntity.anAddress;
import static com.github.jactor.rises.persistence.orm.entity.person.PersonEntity.aPerson;
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
                .withCity("Testfield")
                .build();
    }

    static {
        AbstractRequiredFieldsExtension.withRequiredFields(UserEntity.class, asList(
                new ClassFieldValue("userName", () -> "testNameTimed_" + LocalDateTime.now()),
                new ClassFieldValue("person", RequiredFieldsExtension::aPersonWithRequiredValues)
        ));

        AbstractRequiredFieldsExtension.withRequiredFields(PersonEntity.class, asList(
                new ClassFieldValue("surname", () -> "McTest"),
                new ClassFieldValue("address", RequiredFieldsExtension::anAddressWithRequiredFields, "setAddressEntity")
        ));

        AbstractRequiredFieldsExtension.withRequiredFields(AddressEntity.class, asList(
                new ClassFieldValue("addressLine1", () -> "Test Boulevard 1"),
                new ClassFieldValue("zipCode", () -> 1001),
                new ClassFieldValue("city", () -> "Testing")
        ));
     }
}
