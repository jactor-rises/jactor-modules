package com.github.jactorrises.model.domain;

import com.github.jactorrises.client.persistence.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("A PersistentDomain")
class PersistentDomainTest {

    private PersistentDomain<Long> persistentDomain = new TestPersistedDomain();
    private UserDto userDto = null;

    @DisplayName("should fail if a domain is created for an entity which is null")
    @Test
    void shouldFailIfDomainIsCreatedForAnEntityWhichIsNull() {
        assertThatNullPointerException().isThrownBy(persistentDomain::getId)
                .withMessage(PersistentDomain.THE_PERSISTENT_DATA_ON_THE_DOMAIN_CANNOT_BE_NULL);
    }

    @Test @SuppressWarnings("ResultOfMethodCallIgnored")
    @DisplayName("should get the common properties of the dto")
    void shouldGetTheCommonPropertiesThePersistentEntity() {
        userDto = mock(UserDto.class);
        persistentDomain.getId();
        persistentDomain.getCreatedBy();
        persistentDomain.getCreationTime();
        persistentDomain.getUpdatedBy();
        persistentDomain.getUpdatedTime();

        verify(userDto).getId();
        verify(userDto).getCreatedBy();
        verify(userDto).getCreationTime();
        verify(userDto).getUpdatedBy();
        verify(userDto).getUpdatedTime();
    }

    private class TestPersistedDomain extends PersistentDomain<Long> {
        @Override public UserDto getDto() {
            return userDto;
        }
    }
}
