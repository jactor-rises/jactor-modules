package nu.hjemme.persistence.facade;

import nu.hjemme.client.datatype.Name;
import nu.hjemme.client.domain.User;
import nu.hjemme.persistence.client.UserEntity;
import nu.hjemme.persistence.orm.domain.DefaultUserEntity;
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
        assertThat(PersistentDataService.getInstance().provideInstanceFor(UserEntity.class)).isInstanceOf(DefaultUserEntity.class);
    }

    @DisplayName("should find an implementation of the class (interface) unsing constructor arguments")
    @Test void willFindImplementationOfInterfaceUsingConstructorArguments() {
        assertThat(PersistentDataService.getInstance().provideInstanceFor(UserEntity.class, mock(User.class))).isInstanceOf(DefaultUserEntity.class);
    }
}
