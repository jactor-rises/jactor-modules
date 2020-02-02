# language: no
#noinspection NonAsciiCharacters
Egenskap: Er det fredag?
  Alle vil vite når helga starter

  Scenario: Søndag er ikke fredag
    Gitt dagen er søndag
    Når jeg spør om det er fredag
    Så er svaret "neida"

  Scenario: Fredag er fredag
    Gitt dagen er fredag
    Når jeg spør om det er fredag
    Så er svaret "jada, helg"

  Abstrakt Scenario: Kan <dag> være fredag.
    Gitt dagen er "<dag>"
    Når jeg spør om det er fredag
    Så er svaret "<svar>"

    Eksempler:
      | dag       | svar                |
      | fredag    | jada, helg          |
      | søndag    | neida               |
      | lørdag    | nei, midt i helga   |
      | onsdag    | snart, lille lørdag |
      | noe annet | neida               |
