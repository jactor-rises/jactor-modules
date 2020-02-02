# language: no
Egenskap: applikasjonen jactor-persistence

  Bakgrunn:
    Gitt http url 'http://localhost:1099/jactor-persistence'

  Scenario: applikasjon skal være dokumentert av swagger
    Gitt kompletterende url '/swagger-ui.html#/'
    Når jeg kaller en get operasjon
    Så skal http kode være '200'
