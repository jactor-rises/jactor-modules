package com.github.jactor.cucumber

import io.cucumber.java.no.Gitt
import io.cucumber.java.no.Når
import io.cucumber.java.no.Og
import io.cucumber.java.no.Så
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus

class FellesEgenskaper {
    companion object {
        private lateinit var restService: RestServie
    }

    @Gitt("url til resttjeneste: {string}")
    fun `url til resttjeneste`(baseUrl: String) {
        restService = RestServie(baseUrl)
    }

    @Gitt("endpoint url {string}")
    fun `endpoint url`(endpoint: String) {
        restService.endpoint = endpoint
    }

    @Når("en get gjøres på resttjenesten")
    fun `en get gjores pa resttjenesten`() {
        restService.exchangeGet()
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
