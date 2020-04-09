package com.github.jactor.cucumber.persistence

import io.cucumber.java.no.Gitt
import io.cucumber.java.no.Når
import io.cucumber.java.no.Så
import org.assertj.core.api.Assertions.assertThat

class ApplicationSteps {
    companion object {
        private lateinit var application: Application
        private var status: Int? = null
    }

    @Gitt("http url {string}")
    fun `http url`(url: String) {
        application = Application(url)
    }

    @Gitt("kompletterende url {string}")
    fun `kompletterende url`(url: String?) {
        application.completeUrl = url
    }

    @Når("jeg ber om en response kode på en get request")
    fun `jeg ber om en response kode paa en get request`() {
        status = application.hentResponseStatusForGet()
    }

    @Så("skal statuskoden være {string}")
    fun `skal statuskoden vaere`(httpCode: String) {
        assertThat(status).isEqualTo(httpCode.toInt())
    }
}
