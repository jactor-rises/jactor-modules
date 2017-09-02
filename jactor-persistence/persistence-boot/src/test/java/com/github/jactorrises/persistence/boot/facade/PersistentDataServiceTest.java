package com.github.jactorrises.persistence.boot.facade;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.User;
import com.github.jactorrises.persistence.client.UserEntity;
import com.github.jactorrises.persistence.boot.entity.user.UserEntityImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.mock;

@DisplayName("The PersistentDataService")
class PersistentDataServiceTest {

    @DisplayName("should fail when not configured for the requested class")
    @Test void willThrowExceptionIfInterfaceIsNotConfiguredForNewInstances() {
        assertThatIllegalArgumentException().isThrownBy(() -> PersistentDataService.getInstance().provideInstanceFor(Name.class))
                .withMessageContaining("unable to create instance");
    }

    @DisplayName("should find an implementation of the class (interface)")
    @Test void willFindImplementationOfInterface() {
        assertThat(PersistentDataService.getInstance().provideInstanceFor(UserEntity.class)).isInstanceOf(UserEntityImpl.class);
    }

    @DisplayName("should find an implementation of the class (interface) unsing constructor arguments")
    @Test void willFindImplementationOfInterfaceUsingConstructorArguments() {
        assertThat(PersistentDataService.getInstance().provideInstanceFor(UserEntity.class, mock(User.class))).isInstanceOf(UserEntityImpl.class);
    }
}
