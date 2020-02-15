# language: no
Egenskap: jactor-presistence behandler en bruker

  Scenario: Hent aktive brukere fra jactor-persistence
    Gitt url til resttjeneste: 'http://localhost:1099/jactor-persistence/user'
    Og endpoint url '/usernames'
    Når en get gjøres på resttjenesten
    Så skal statuskoden fra resttjenesten være '200'
    Og responsen skal inneholde '"tip"'

  Scenario: Hent en bruker etter brukernavn
    Gitt url til resttjeneste: 'http://localhost:1099/jactor-persistence/user/name'
    Og path variable '/jactor'
    Når en get gjøres på resttjenesten
    Så skal statuskoden fra resttjenesten være '200'
    Og responsen skal inneholde '"username":"jactor"'
