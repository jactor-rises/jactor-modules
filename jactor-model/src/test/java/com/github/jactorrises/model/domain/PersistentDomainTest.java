package com.github.jactorrises.model.domain;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.persistence.dto.PersistentDto;
import com.github.jactorrises.client.persistence.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

@DisplayName("A PersistentDomain")
class PersistentDomainTest {

    @DisplayName("should fail if a domain is created for an entity which is null")
    @Test @SuppressWarnings("ResultOfMethodCallIgnored")
    void shouldFailIfDomainIsCreatedForAnEntityWhichIsNull() {
        assertThatNullPointerException().isThrownBy(new TestPersistedDomain(null)::getId)
                .withMessage(PersistentDomain.THE_PERSISTENT_DATA_ON_THE_DOMAIN_CANNOT_BE_NULL);
    }

    @DisplayName("should get the common properties of the dto")
    @Test
    void shouldGetTheCommonPropertiesThePersistentEntity() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setCreatedBy("me");
        userDto.setCreationTime(LocalDateTime.now().withNano(0).minusDays(1));
        userDto.setUpdatedBy("you");
        userDto.setUpdatedTime(LocalDateTime.now().withNano(0));

        TestPersistedDomain persistentDomain = new TestPersistedDomain(userDto);
        assertThat(persistentDomain.getId()).as("id").isEqualTo(1L);
        assertThat(persistentDomain.getCreatedBy()).as("creator").isEqualTo(new Name("me"));
        assertThat(persistentDomain.getCreationTime()).as("created time").isEqualTo(LocalDateTime.now().withNano(0).minusDays(1));
        assertThat(persistentDomain.getUpdatedBy()).as("updated by").isEqualTo(new Name("you"));
        assertThat(persistentDomain.getUpdatedTime()).as("updatet time").isEqualTo(LocalDateTime.now().withNano(0));
    }

    private class TestPersistedDomain extends PersistentDomain {
        private PersistentDto persistentDto;

        TestPersistedDomain(PersistentDto persistentDto) {
            this.persistentDto = persistentDto;
        }

        @Override public PersistentDto getDto() {
            return persistentDto;
        }
    }
}
