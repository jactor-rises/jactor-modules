package com.github.jactorrises.persistence.boot.entity;

import com.github.jactorrises.client.datatype.Description;
import com.github.jactorrises.client.datatype.EmailAddress;
import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.persistence.boot.entity.user.UserEntityImpl;
import com.github.jactorrises.persistence.client.AddressEntity;
import com.github.jactorrises.persistence.client.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@DisplayName("A PersistentEntity")
class PersistentEntityTest {
    private TestPersistentEntity testPersistentEntity;

    @BeforeEach void initForTesting() {
        testPersistentEntity = new TestPersistentEntity();
    }

    @DisplayName("should have an implementation of the toString method")
    @Test void willHaveClassNameAndIdOnToStringMethod() {
        String string = testPersistentEntity.setId(101L).toString();

        assertThat(string).contains("TestPersistentEntity").contains("101");
    }

    @DisplayName("should fail to convert data when from a type which is unknown")
    @Test void willThrowIllegalArgumentExceptionWhenDataTypeConvertingFromIsUnknown() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> testPersistentEntity.convertTo(new TestPersistentEntity(), PersistentEntity.class));
        assertThat(illegalArgumentException.getMessage())
                .contains(PersistentEntity.class.getSimpleName())
                .contains("is not a type known for any converter");
    }

    @DisplayName("should fail to convert data when from a type which is unknown")
    @Test void willThrowIllegalArgumentExceptionWhenDataTypeIsUnknown() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> testPersistentEntity.convertFrom(new TestPersistentEntity(), PersistentEntity.class));
        assertThat(illegalArgumentException.getMessage())
                .contains(PersistentEntity.class.getSimpleName())
                .contains("is not a type known for any converter");
    }

    @DisplayName("should convert to a name")
    @Test void willConvertFromName() {
        assertThat(testPersistentEntity.convertTo("jacobsen", Name.class)).isEqualTo(new Name("jacobsen"));
    }

    @DisplayName("should convert an email address")
    @Test void willConvertEmailAddress() {
        assertThat(testPersistentEntity.convertTo("some@where.com", EmailAddress.class)).isEqualTo(new EmailAddress("some", "where.com"));
    }

    @DisplayName("should convert to a user name")
    @Test void willConvertUserName() {
        assertThat(testPersistentEntity.convertTo("jactor", UserName.class)).isEqualTo(new UserName("jactor"));
    }

    @DisplayName("should convert a description")
    @Test void willConvertDescription() {
        assertThat(testPersistentEntity.convertTo("description", Description.class)).isEqualTo(new Description("description"));
    }

    @DisplayName("should fail to cast when type is not suitable for the entity")
    @Test void willNotCastOrInitializeKnownImplementation() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> testPersistentEntity.castOrInitializeCopyWith(new UserEntityImpl(), AddressEntity.class));
        assertThat(illegalArgumentException.getMessage())
                .contains(UserEntityImpl.class.getSimpleName())
                .contains("Unable to cast or initialize");
    }

    @DisplayName("should cast when type is suitable for the entity")
    @Test void willCastKnownImplementation() {
        UserEntityImpl userEntityImpl = new UserEntityImpl();
        assertThat(userEntityImpl).isSameAs(testPersistentEntity.castOrInitializeCopyWith(userEntityImpl, UserEntity.class));
    }

    @DisplayName("should initialize known implementation")
    @Test void willInitializeKnownImplementation() {
        assertThat(testPersistentEntity.castOrInitializeCopyWith(mock(UserEntityImpl.class), UserEntityImpl.class))
                .isNotNull()
                .isNotInstanceOf(Mockito.class)
                .isInstanceOf(UserEntity.class);
    }

    private class TestPersistentEntity extends PersistentEntity {

        TestPersistentEntity setId(@SuppressWarnings("SameParameterValue") Long id) {
            super.id = id;
            return this;
        }
    }
}
