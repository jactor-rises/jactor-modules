package com.github.jactor.cucumber

import com.github.jactor.cucumber.persistence.Application

class StepValues {
    companion object {
        lateinit var application: Application
        lateinit var restService: RestService
        lateinit var uniqueKey: String

        var status: Int = -1
    }
}