package com.github.jactorrises.persistence.orm.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

@DisplayName("A PersistentEntity")
class PersistentEntityTest {

    @DisplayName("should have an implementation of the toString method")
    @Test void willHaveClassNameAndIdOnToStringMethod() {
        assertThat(new TestPersistentEntity(101).toString()).isEqualTo("id=101");
    }

    private class TestPersistentEntity extends PersistentEntity {

        TestPersistentEntity(long id) {
            super.id = id;
        }
    }
}
