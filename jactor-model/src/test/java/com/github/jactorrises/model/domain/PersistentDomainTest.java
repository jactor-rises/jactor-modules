package com.github.jactorrises.model.domain;

import com.github.jactorrises.persistence.client.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("A PersistentDomain")
class PersistentDomainTest {

    private PersistentDomain<UserEntity, Long> persistentDomain = new TestPersistedDomain();
    private UserEntity userEntity = null;

    @DisplayName("should fail if a domain is created for an entity which is null")
    @Test
    void shouldFailIfDomainIsCreatedForAnEntityWhichIsNull() {
        assertThatNullPointerException().isThrownBy(persistentDomain::getId)
                .withMessage(PersistentDomain.THE_ENTITY_ON_THE_DOMAIN_CANNOT_BE_NULL);
    }

    @Test
    @DisplayName("should get the common properties of athe persistent entity")
    void shouldGetTheCommonPropertiesThePersistentEntity() {
        userEntity = mock(UserEntity.class);
        persistentDomain.getId();
        persistentDomain.getCreatedBy();
        persistentDomain.getCreationTime();
        persistentDomain.getUpdatedBy();
        persistentDomain.getUpdatedTime();

        verify(userEntity).getId();
        verify(userEntity).getCreatedBy();
        verify(userEntity).getCreationTime();
        verify(userEntity).getUpdatedBy();
        verify(userEntity).getUpdatedTime();
    }

    private class TestPersistedDomain extends PersistentDomain<UserEntity, Long> {
        @Override public UserEntity getEntity() {
            return userEntity;
        }
    }
}
