package com.github.jactor.cucumber

import com.github.jactor.cucumber.StepValues.Companion.restService
import io.cucumber.java8.No
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus

class RestServiceSteps : No {

    init {
        Gitt("url til resttjeneste: {string}") { baseUrl: String ->
            restService = RestService(baseUrl)
        }

        Og("gitt url til resttjeneste: {string}") { baseUrl: String ->
            restService = RestService(baseUrl)
        }

        Gitt("endpoint url {string}") { url: String ->
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

        Så("skal statuskoden fra resttjenesten være {string}") { statusKode: String ->
            val httpStatus = HttpStatus.valueOf(statusKode.toInt())
            assertThat(restService.hentStatusKode()).isEqualTo(httpStatus)
        }

        Og("responsen skal inneholde {string}") { tekst: String ->
            assertThat(restService.hentResponse()).containsSubsequence(tekst)
        }
    }
}
