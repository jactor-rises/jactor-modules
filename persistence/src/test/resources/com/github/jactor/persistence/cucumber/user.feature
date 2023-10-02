# language: no
Egenskap: jactor-presistence behandler en bruker

  Scenario: Hent aktive brukere fra jactor-persistence
    Gitt base url 'http://localhost:1099/jactor-persistence/user'
    Og endpoint '/usernames'
    Når en get gjøres på resttjenesten
    Så skal statuskoden fra resttjenesten være 200
    Og responsen skal inneholde 'tip'

  Scenario: Hent administratorerer fra jactor-persistence
    Gitt base url 'http://localhost:1099/jactor-persistence/user'
    Og endpoint '/usernames'
    Når en get gjøres på resttjenesten med parameter 'userType' = 'ADMIN'
    Så skal statuskoden fra resttjenesten være 200
    Og responsen skal inneholde 'jactor'

  Scenario: Hent en bruker etter brukernavn
    Gitt base url 'http://localhost:1099/jactor-persistence/user/name'
    Og path variable '/jactor'
    Når en get gjøres på resttjenesten
    Så skal statuskoden fra resttjenesten være 200
    Og responsen skal inneholde '"username":"jactor"'

  Scenario: Opprett en ny bruker, hent opprettet bruker, feil oppretting av ny bruker grunnet identisk brukernavn
    Gitt base url 'http://localhost:1099/jactor-persistence/user'
    Når en post gjøres for body:
    """
      {
        "username":"fish",
        "emailAddress":"fishy@smelly.org",
        "language":"no",
        "firstName":"Mr.",
        "surname":"Fish",
        "addressLine1":"Somewhere",
        "addressLine2":"out",
        "addressLine3":"there",
        "zipCode":"666",
        "city":"below",
        "contry":"no"
      }
    """
    Så skal statuskoden fra resttjenesten være 201
    Og gitt url 'http://localhost:1099/jactor-persistence/user/name/fish'
    Når en get gjøres på resttjenesten
    Så skal statuskoden fra resttjenesten være 200
    Og responsen skal inneholde '"username":"fish"'
    Og responsen skal inneholde '"surname":"Fish"'
    Gitt base url 'http://localhost:1099/jactor-persistence/user'
    Når en post gjøres for body:
    """
      {
        "username":"fish",
        "emailAddress":"fishy@smelly.org",
        "language":"no",
        "firstName":"Mr.",
        "surname":"Fish",
        "addressLine1":"Somewhere",
        "addressLine2":"out",
        "addressLine3":"there",
        "zipCode":"666",
        "city":"below",
        "contry":"no"
      }
    """
    Så skal statuskoden fra resttjenesten være 400
