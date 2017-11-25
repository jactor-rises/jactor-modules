package com.github.jactorrises.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

@DisplayName("A PersistentOrm")
class PersistentOrmTest {

    @DisplayName("should have an implementation of the toString method")
    @Test void willHaveClassNameAndIdOnToStringMethod() {
        assertThat(new TestPersistentOrm(101).toString()).isEqualTo("id=101");
    }

    private class TestPersistentOrm extends PersistentOrm {

        TestPersistentOrm(long id) {
            super.id = id;
        }
    }
}
