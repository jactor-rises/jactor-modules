package com.gitlab.jactor.rises.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("A JactorPersistence")
class JactorPersistenceTest {

    @Autowired private CommandLineRunner commandLineRunner;

    @DisplayName("should contain bean named CommandlineRunner")
    @Test void shouldContainCommandLineRunner() {
        assertThat(commandLineRunner).isNotNull();
    }
}