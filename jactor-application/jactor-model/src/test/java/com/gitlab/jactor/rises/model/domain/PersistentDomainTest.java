package com.gitlab.jactor.rises.model.domain;

import com.gitlab.jactor.rises.commons.datatype.Name;
import com.gitlab.jactor.rises.commons.dto.PersistentDto;
import com.gitlab.jactor.rises.commons.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

@DisplayName("A PersistentDomain")
class PersistentDomainTest {

    @DisplayName("should fail if a domain is created with a dto which is null") @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test void shouldFailIfDomainIsCreatedForDtoWhichIsNull() {
        assertThatNullPointerException().isThrownBy(new TestPersistedDomain(null)::fetchDto)
                .withMessage(PersistentDomain.THE_PERSISTENT_DATA_ON_THE_DOMAIN_CANNOT_BE_NULL);
    }

    @DisplayName("should get the common properties of the dto")
    @Test void shouldGetTheCommonPropertiesThePersistentEntity() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setCreatedBy("me");
        userDto.setCreationTime(LocalDateTime.now().minusDays(1));
        userDto.setUpdatedBy("you");
        userDto.setUpdatedTime(LocalDateTime.now());

        TestPersistedDomain persistentDomain = new TestPersistedDomain(userDto);
        assertThat(persistentDomain.getId()).as("id").isEqualTo(1L);
        assertThat(persistentDomain.getCreatedBy()).as("creator").isEqualTo(new Name("me"));
        assertThat(persistentDomain.getCreationTime()).as("created time").isEqualTo(userDto.getCreationTime());
        assertThat(persistentDomain.getUpdatedBy()).as("updated by").isEqualTo(new Name("you"));
        assertThat(persistentDomain.getUpdatedTime()).as("updatet time").isEqualTo(userDto.getUpdatedTime());
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
