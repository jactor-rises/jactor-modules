package com.github.jactorrises.model.persistence.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    private class TestPersistentEntity extends PersistentEntity {

        TestPersistentEntity setId(@SuppressWarnings("SameParameterValue") Long id) {
            super.id = id;
            return this;
        }
    }
}
