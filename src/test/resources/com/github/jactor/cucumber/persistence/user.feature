# language: no
Egenskap: jactor-presistence behandler en bruker

  Bakgrunn: Baseurl for brukerbehandling
    Gitt url til resttjeneste: 'http://localhost:1099/jactor-persistence/user'

  Scenario: Hent aktive brukere fra jactor-persistence
    Gitt endpoint url '/usernames'
    Når en get gjøres på resttjenesten
    Så skal statuskoden fra resttjenesten være '200'
    Og responsen skal inneholde '"tip"'
