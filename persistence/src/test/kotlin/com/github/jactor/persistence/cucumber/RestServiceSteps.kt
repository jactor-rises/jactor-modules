package com.github.jactor.persistence.cucumber

import org.springframework.http.HttpStatus
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import io.cucumber.java8.No

@Suppress("unused", "LeakingThis") // bestemmes av cucumber
internal class RestServiceSteps : No, PersistenceCucumberContextConfiguration() {

    init {
        Gitt("base url {string}") { baseUrl: String ->
            scenarioValues.restService = RestService(baseUrl)
        }

        Og("gitt url til resttjeneste: {string}") { baseUrl: String ->
            scenarioValues.restService = RestService(baseUrl)
        }

        Gitt("endpoint {string}") { endpoint: String ->
            scenarioValues.restService.endpoint = endpoint
        }

        Og("path variable {string}") { endpoint: String ->
            scenarioValues.restService.endpoint = endpoint
        }

        Når("en get gjøres på resttjenesten") {
            scenarioValues.responseEntity = scenarioValues.restService
                .exchangeGet(parameternavn = null, parameter = null) { testRestTemplate }
        }

        Når("en get gjøres på resttjenesten med parameter {string} = {string}") { parameternavn: String, verdi: String ->
            scenarioValues.responseEntity = scenarioValues.restService
                .exchangeGet(parameternavn, verdi) { testRestTemplate }
        }

        Når("en post gjøres med body:") { json: String ->
            scenarioValues.restService.exchangePost(json) { testRestTemplate }
        }

        Så("skal statuskoden fra resttjenesten være {int}") { statusKode: Int ->
            val httpStatus = HttpStatus.valueOf(statusKode)
            assertThat(scenarioValues.hentStatusKode()).isEqualTo(httpStatus)
        }

        Og("responsen skal inneholde {string}") { tekst: String ->
            assertThat(scenarioValues.hentResponse()).isNotNull().contains(tekst)
        }

        Så("skal statuskoden være {int}") { httpCode: Int ->
            assertThat(scenarioValues.hentStatusKode()).isEqualTo(HttpStatus.valueOf(httpCode))
        }
    }
}
