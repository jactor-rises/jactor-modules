package com.github.jactor.web.client

import com.github.jactor.shared.api.UserDto
import com.github.jactor.web.JactorWebBeans
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class DefaultUserClient(
    private val restTemplate: RestTemplate,
    jactorWebUriTemplateHandler: JactorWebBeans.JactorWebUriTemplateHandler
) : UserClient {

    init {
        restTemplate.uriTemplateHandler = jactorWebUriTemplateHandler.uriTemplateHandler
    }

    override fun find(username: String): UserDto? {
        return bodyOf(restTemplate.getForEntity("/user/name/$username", UserDto::class.java))
    }

    override fun findAllUsernames(): List<String> {
        return restTemplate.getForEntity("/user/usernames", Array<String>::class.java).body?.toList() ?: emptyList()
    }

    private fun bodyOf(responseEntity: ResponseEntity<UserDto>?): UserDto? {
        checkNotNull(responseEntity) { "No response from REST service" }

        if (isNot2xxSuccessful(responseEntity.statusCode)) {
            val badConfiguredResponseMesssage = String.format(
                "Bad configuration of consumer! ResponseCode: %s(%d)",
                responseEntity.statusCode.toString(),
                responseEntity.statusCode
            )

            throw IllegalStateException(badConfiguredResponseMesssage)
        }

        return responseEntity.body
    }

    private fun isNot2xxSuccessful(statusCode: HttpStatusCode): Boolean {
        return !statusCode.is2xxSuccessful
    }
}

interface UserClient {
    fun find(username: String): UserDto?
    fun findAllUsernames(): List<String>
}
