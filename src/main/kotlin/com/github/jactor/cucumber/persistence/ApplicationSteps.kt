package com.github.jactor.cucumber.persistence

import com.github.jactor.cucumber.ScenarioValues.Companion.application
import com.github.jactor.cucumber.ScenarioValues.Companion.hentStatusKode
import io.cucumber.java8.No
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus

class ApplicationSteps : No {

    init {
        Gitt("http url {string}") { url: String ->
            application = Application(url)
        }

        Gitt("kompletterende url {string}") { url: String ->
            application.completeUrl = url
        }

        Når("jeg utfører en get request") {
            application.doGet()
        }

        Så("skal statuskoden være {int}") { httpCode: Int ->
            assertThat(HttpStatus.valueOf(httpCode)).isEqualTo(hentStatusKode())
        }
    }
}
