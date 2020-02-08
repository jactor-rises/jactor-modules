# language: no
Egenskap: Applikasjonen jactor-persistence

  Bakgrunn:
    Gitt http url 'http://localhost:1099/jactor-persistence'

  Scenario: Applikasjon skal være dokumentert av swagger
    Gitt kompletterende url '/swagger-ui.html#/'
    Når jeg ber om en response kode på en get request
    Så skal statuskoden være '200'

  Scenario: BlogController skal være dokumentert av swagger
    Gitt kompletterende url '/swagger-ui.html#/blog-controller'
    Når jeg ber om en response kode på en get request
    Så skal statuskoden være '200'

  Scenario: GuestBook controller skal være dokumentert av swagger
    Gitt kompletterende url '/swagger-ui.html#/guest-book-controller'
    Når jeg ber om en response kode på en get request
    Så skal statuskoden være '200'

  Scenario: UserController skal være dokumentert av swagger
    Gitt kompletterende url '/swagger-ui.html#/user-controller'
    Når jeg ber om en response kode på en get request
    Så skal statuskoden være '200'
