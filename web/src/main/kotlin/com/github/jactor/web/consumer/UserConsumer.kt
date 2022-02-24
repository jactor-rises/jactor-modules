package com.github.jactor.web.consumer

import com.github.jactor.shared.dto.UserDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.Optional

@Service
class DefaultUserConsumer(private val restTemplate: RestTemplate) : UserConsumer {

    override fun find(username: String): Optional<UserDto> {
        return Optional.ofNullable(bodyOf(restTemplate.getForEntity("/user/name/$username", UserDto::class.java)))
    }

    override fun findAllUsernames(): List<String> {
        return restTemplate.getForEntity("/user/usernames", Array<String>::class.java).body?.toList() ?: emptyList()
    }

    private fun bodyOf(responseEntity: ResponseEntity<UserDto>?): UserDto? {
        checkNotNull(responseEntity) { "No response from REST service" }

        if (isNot2xxSuccessful(responseEntity.statusCode)) {
            val badConfiguredResponseMesssage = String.format(
                "Bad configuration of consumer! ResponseCode: %s(%d)",
                responseEntity.statusCode.name,
                responseEntity.statusCodeValue
            )
            throw IllegalStateException(badConfiguredResponseMesssage)
        }

        return responseEntity.body
    }

    private fun isNot2xxSuccessful(statusCode: HttpStatus): Boolean {
        return !statusCode.is2xxSuccessful
    }
}

interface UserConsumer {
    fun find(username: String): Optional<UserDto>
    fun findAllUsernames(): List<String>
}
