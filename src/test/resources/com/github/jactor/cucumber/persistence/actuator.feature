# language: no
Egenskap: Actuator på app jactor-persistence

  Bakgrunn:
    Gitt url til resttjeneste: 'http://localhost:1099/jactor-persistence/actuator'

  Scenario: Kall mot actuator/health
    Gitt endpoint url '/health'
    Når en get gjøres på resttjenesten
    Så skal statuskoden fra resttjenesten være '200'
    Og responsen skal inneholde '"status":"UP"'