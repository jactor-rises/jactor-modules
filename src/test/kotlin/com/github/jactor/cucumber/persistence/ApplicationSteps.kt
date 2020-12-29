package com.github.jactor.cucumber.persistence

import com.github.jactor.cucumber.StepValues.Companion.application
import com.github.jactor.cucumber.StepValues.Companion.status
import io.cucumber.java8.No
import org.assertj.core.api.Assertions.assertThat

class ApplicationSteps : No {

    init {
        Gitt("http url {string}") { url: String ->
            application = Application(url)
        }

        Gitt("kompletterende url {string}") { url: String ->
            application.completeUrl = url
        }

        Når("jeg ber om en response kode på en get request") {
            status = application.hentResponseStatusForGet()
        }

        Så("skal statuskoden være {int}") { httpCode: Int ->
            assertThat(status).isEqualTo(httpCode)
        }
    }
}
