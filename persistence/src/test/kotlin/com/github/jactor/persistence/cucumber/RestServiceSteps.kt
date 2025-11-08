package com.github.jactor.persistence.cucumber

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import io.cucumber.java8.No
import org.springframework.http.HttpStatus

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
            scenarioValues.entityExchangeResult = scenarioValues.restService
                .exchangeGet(parameternavn = null, parameter = null) { webTestClient }
        }

        Når("en get gjøres på resttjenesten med parameter {string} = {string}") { parameternavn: String, verdi: String ->
            scenarioValues.entityExchangeResult = scenarioValues.restService
                .exchangeGet(parameternavn, verdi) { webTestClient }
        }

        Når("en post gjøres med body:") { json: String ->
            scenarioValues.restService.exchangePost(json) { webTestClient }
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
