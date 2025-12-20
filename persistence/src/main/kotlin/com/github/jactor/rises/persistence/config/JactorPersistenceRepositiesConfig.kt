package com.github.jactor.rises.persistence.config

import com.github.jactor.rises.persistence.address.AddressDao
import com.github.jactor.rises.persistence.address.AddressRepository
import com.github.jactor.rises.persistence.blog.BlogDao
import com.github.jactor.rises.persistence.blog.BlogEntryDao
import com.github.jactor.rises.persistence.blog.BlogRepository
import com.github.jactor.rises.persistence.guestbook.GuestBookDao
import com.github.jactor.rises.persistence.guestbook.GuestBookEntryDao
import com.github.jactor.rises.persistence.guestbook.GuestBookRepository
import com.github.jactor.rises.persistence.person.PersonDao
import com.github.jactor.rises.persistence.person.PersonRepository
import com.github.jactor.rises.persistence.user.UserDao
import com.github.jactor.rises.persistence.user.UserRepository
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.context.annotation.Configuration
import java.util.UUID

@Configuration
@OpenAPIDefinition(info = Info(title = "jactor-persistence.repositories", version = "v2"))
class JactorPersistenceRepositiesConfig(
    internal val addressRepository: AddressRepository,
    internal val blogRepository: BlogRepository,
    internal val guestBookRepository: GuestBookRepository,
    internal val personRepository: PersonRepository,
    internal val userRepository: UserRepository,
) {
    init {
        initFetchRelations()
    }

    internal fun initFetchRelations() {
        fetchAddressRelation = { addressRepository.findById(id = it) }
        fetchBlogRelation = { blogRepository.findBlogById(id = it) }
        fetchBlogRelations = { blogRepository.findBlogsByUserId(id = it) }
        fetchBlogEntryRelations = { blogRepository.findBlogEntriesByBlogId(id = it) }
        fetchGuestBookRelation = { guestBookRepository.findGuestBookById(id = it) }
        fetchGuestBookEntryRelations = { guestBookRepository.findGuestBookEtriesByGuestBookId(id = it) }
        fetchPersonRelation = { personRepository.findById(id = it) }
        fetchUserRelation = { userRepository.findById(id = it) }
        fetchUserRelations = { userRepository.findByPersonId(id = it) }
    }

    internal companion object {
        internal var fetchAddressRelation: (UUID) -> AddressDao? = { null }
        internal var fetchBlogRelation: (UUID) -> BlogDao? = { null }
        internal var fetchBlogRelations: (UUID) -> List<BlogDao> = { emptyList() }
        internal var fetchBlogEntryRelations: (UUID) -> List<BlogEntryDao> = { emptyList() }
        internal var fetchGuestBookRelation: (UUID) -> GuestBookDao? = { null }
        internal var fetchGuestBookEntryRelations: (UUID) -> List<GuestBookEntryDao> = { emptyList() }
        internal var fetchPersonRelation: (UUID) -> PersonDao? = { null }
        internal var fetchUserRelation: (UUID) -> UserDao? = { null }
        internal var fetchUserRelations: (UUID) -> List<UserDao> = { emptyList() }
    }
}
