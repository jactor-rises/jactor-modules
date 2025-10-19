package com.github.jactor.persistence.test

import com.github.jactor.persistence.Address
import com.github.jactor.persistence.Blog
import com.github.jactor.persistence.BlogEntry
import com.github.jactor.persistence.GuestBook
import com.github.jactor.persistence.JactorPersistenceRepositiesConfig
import com.github.jactor.persistence.Person
import com.github.jactor.persistence.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

/**
 * For å unngå å starte en spring-boot applikasjon og eventuelt laste spring-context på ny og når endringer som gjøres i
 * enhetstestene ikke skal ha innvirkning på spring-context<br>
 * Hvis test krever data i database, må det settes opp manuelt
 */
@SpringBootTest
@Transactional
abstract class AbstractSpringBootNoDirtyContextTest {
    @Autowired
    private lateinit var jactorPersistenceRepositiesConfig: JactorPersistenceRepositiesConfig

    protected fun save(address: Address): Address = jactorPersistenceRepositiesConfig.addressRepository
        .save(addressDao = address.toAddressDao()).toAddress()

    protected fun save(blog: Blog): Blog = jactorPersistenceRepositiesConfig.blogRepository
        .save(blogDao = blog.toBlogDao()).toBlog()

    protected fun save(blogEntry: BlogEntry): BlogEntry = jactorPersistenceRepositiesConfig.blogRepository
        .save(blogEntryDao = blogEntry.toBlogEntryDao()).toBlogEntry()

    protected fun save(guestBook: GuestBook): GuestBook = jactorPersistenceRepositiesConfig.guestBookRepository
        .save(guestBookDao = guestBook.toGuestBookDao()).toGuestBook()

    protected fun save(person: Person): Person = jactorPersistenceRepositiesConfig.personRepository
        .save(personDao = person.toPersonDao()).toPerson()

    protected fun save(user: User): User = jactorPersistenceRepositiesConfig.userRepository
        .save(userDao = user.toUserDao()).toUser()
}
