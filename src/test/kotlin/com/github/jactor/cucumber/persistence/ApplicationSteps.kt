package com.github.jactor.cucumber.persistence

import io.cucumber.java8.No
import org.assertj.core.api.Assertions.assertThat

class ApplicationSteps : No {
    companion object {
        private lateinit var application: Application
        private var status: Int? = null
    }

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

        Så("skal statuskoden være {string}") { httpCode: String ->
            assertThat(status).isEqualTo(httpCode.toInt())
        }
    }
}
