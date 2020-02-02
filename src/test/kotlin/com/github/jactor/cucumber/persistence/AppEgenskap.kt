package com.github.jactor.cucumber.persistence

import io.cucumber.java.PendingException
import io.cucumber.java.no.Gitt
import io.cucumber.java.no.Når
import io.cucumber.java.no.Så

class AppEgenskap {
    companion object {
        private lateinit var application: Application
    }

    @Gitt("http url {string}")
    fun `http url`(url: String) {
        application = Application(url)
    }

    @Gitt("kompletterende url {string}")
    fun `kompletterende url`(url: String?) {
        application.completeUrl = url
    }

    @Når("jeg kaller en get operasjon")
    fun `jeg kaller en get operasjon`() {
        throw PendingException()
    }

    @Så("skal http kode være {string}")
    fun `skal http kode vaere`(httpCode: String) {
        throw PendingException()
    }
}

