# language: no
Egenskap: back-end, jactor-persistence

  Bakgrunn:
    Gitt base url 'http://localhost:1099/jactor-persistence/actuator'
    Og endpoint '/mappings'
    Når en get gjøres på resttjenesten

  Scenario: BlogController skal være mappet
    Så skal statuskoden være 200
    Og responsen skal inneholde 'com.github.jactor.persistence.api.controller.BlogController'
    Og responsen skal inneholde '/blog/{blogId}'
    Og responsen skal inneholde '/blog/{id}/entries'
    Og responsen skal inneholde '/blog/entry'
    Og responsen skal inneholde '/blog/entry/{id}'
    Og responsen skal inneholde '/blog/entry/{blogEntryId}'

  Scenario: GuestBook skal være mappet
    Så skal statuskoden være 200
    Og responsen skal inneholde 'com.github.jactor.persistence.api.controller.GuestBookController'
    Og responsen skal inneholde '/guestBook'
    Og responsen skal inneholde '/guestBook/{id}'
    Og responsen skal inneholde '/guestBook/entry'
    Og responsen skal inneholde '/guestBook/entry/{id}'

  Scenario: UserController skal være mappet
    Så skal statuskoden være 200
    Og responsen skal inneholde 'com.github.jactor.persistence.api.controller.UserController'
    Og responsen skal inneholde '/user/{id}'
    Og responsen skal inneholde '/user/name/{username}'
    Og responsen skal inneholde '/user/usernames'
