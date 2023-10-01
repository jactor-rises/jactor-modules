package com.github.jactor.persistence.cucumber

import org.springframework.http.HttpStatus
import com.github.jactor.persistence.cucumber.ScenarioValues.Companion.hentResponse
import com.github.jactor.persistence.cucumber.ScenarioValues.Companion.hentStatusKode
import com.github.jactor.persistence.cucumber.ScenarioValues.Companion.restService
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import io.cucumber.java8.No

@Suppress("unused", "LeakingThis") // bestemmes av cucumber
internal class RestServiceSteps : No, PersistenceCucumberContextConfiguration() {

    init {
        Gitt("base url {string}") { baseUrl: String ->
            restService = RestService(baseUrl)
        }

        Og("gitt url til resttjeneste: {string}") { baseUrl: String ->
            restService = RestService(baseUrl)
        }

        Gitt("endpoint {string}") { url: String ->
            restService.url = url
        }

        Og("path variable {string}") { url: String ->
            restService.url = url
        }

        Når("en get gjøres på resttjenesten") {
            restService.exchangeGet()
        }

        Når("en get gjøres på resttjenesten med parameter {string} = {string}") { navn: String, verdi: String ->
            restService.exchangeGet(navn, verdi)
        }

        Når("en post gjøres med body:") { json: String ->
            restService.exchangePost(json)
        }

        Så("skal statuskoden fra resttjenesten være {int}") { statusKode: Int ->
            val httpStatus = HttpStatus.valueOf(statusKode)
            assertThat(hentStatusKode()).isEqualTo(httpStatus)
        }

        Og("responsen skal inneholde {string}") { tekst: String ->
            assertThat(hentResponse()).isNotNull().contains(tekst)
        }

        Så("skal statuskoden være {int}") { httpCode: Int ->
            assertThat(hentStatusKode()).isEqualTo(HttpStatus.valueOf(httpCode))
        }
    }
}
