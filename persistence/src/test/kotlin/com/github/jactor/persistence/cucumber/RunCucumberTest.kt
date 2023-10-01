package com.github.jactor.persistence.cucumber

import org.junit.platform.suite.api.ConfigurationParameter
import org.junit.platform.suite.api.ConfigurationParameters
import org.junit.platform.suite.api.ExcludeTags
import org.junit.platform.suite.api.IncludeEngines
import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.Suite
import org.springframework.test.context.ActiveProfiles
import io.cucumber.java.Before
import io.cucumber.java.Scenario
import io.cucumber.junit.platform.engine.Constants

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("com/github/jactor/persistence/cucumber")
@ConfigurationParameters(
    ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty, html:build/cucumber-report.html"),
    ConfigurationParameter(key = Constants.PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true")
)
@ExcludeTags("Disabled")
@ActiveProfiles("cucumber")
internal class RunCucumberTest: AbstractSpringBootCucumberConfiguration() {
    @Before
    fun beforeScenario(scenario: Scenario) {
        val users = userRepository.findAll()

        for (user in users) {
            if (user.username !in standardUsers) {
                userRepository.delete(user)
            }
        }
    }
}
