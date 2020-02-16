package com.github.jactor.cucumber

import io.cucumber.java.no.Gitt
import io.cucumber.java.no.Når
import io.cucumber.java.no.Og
import io.cucumber.java.no.Så
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus

class RestServiceSteps {
    companion object {
        private lateinit var restService: RestService
    }

    @Gitt("url til resttjeneste: {string}")
    fun `url til resttjeneste`(baseUrl: String) {
        restService = RestService(baseUrl)
    }

    @Gitt("endpoint url {string}")
    @Og("path variable {string}")
    fun url(url: String) {
        restService.url = url
    }

    @Når("en get gjøres på resttjenesten")
    fun `en get gjores pa resttjenesten`() {
        restService.exchangeGet()
    }

    @Når("en get gjøres på resttjenesten med parameter {string} = {string}")
    fun `en get gjores pa resttjenesten med parameter`(navn: String, verdi: String) {
        restService.exchangeGet(navn, verdi)
    }

    @Så("skal statuskoden fra resttjenesten være {string}")
    fun `skal statuskoden fra resttjenesten vare`(statusKode: String) {
        val httpStatus = HttpStatus.valueOf(statusKode.toInt())
        assertThat(restService.hentStatusKode()).isEqualTo(httpStatus)
    }

    @Og("responsen skal inneholde {string}")
    fun `responsen skal inneholde`(tekst: String) {
        assertThat(restService.hentResponse()).containsSubsequence(tekst)
    }
}
