# jactor-cucumber
Integrsjonstesting av jactor-persistence

![](https://github.com/jactor-rises/jactor-cucumber/workflows/bump%20version/badge.svg)

## Integrasjonstesting med maven
Dette er et [maven](https://maven.apache.org) prosjekt som kjører [cucumber](https://cucumber.io) med [Kotlin](https://kotlinlang.org)

Utføring av testing gjøres med kommandoen `mvn test` i rotkatalogen til prosjektet

## Test rapportering

Etter at testing er gjennomført så kan man lage en rapport som blir tilgjengelig i
`target/generated-report/index.html`. Dette gjøres av en maven-plugin:
```
mvn cluecumber-report:reporting
```
Man kan også gjøre både testing og rapportgenerering i et steg med `mvn install`

### Sist utført integrasjonstest

Siste cucumber [rapport](https://jactor-rises.github.io/jactor-cucumber/latest)

### Eldre tester

[2020-05-17/](https://jactor-rises.github.io/jactor-cucumber/generated/2020-05-17/)
[2020-05-16/](https://jactor-rises.github.io/jactor-cucumber/generated/2020-05-16/)
[2020-05-16.17:46:36/](https://jactor-rises.github.io/jactor-cucumber/generated/2020-05-16.17:46:36/)
[2020-05-16.17:37:38/](https://jactor-rises.github.io/jactor-cucumber/generated/2020-05-16.17:37:38/)
[2020-05-16.17:28:38/](https://jactor-rises.github.io/jactor-cucumber/generated/2020-05-16.17:28:38/)
[2020-05-16.17:19:31/](https://jactor-rises.github.io/jactor-cucumber/generated/2020-05-16.17:19:31/)
[2020-05-06/](https://jactor-rises.github.io/jactor-cucumber/generated/2020-05-06/)
[2020-05-05/](https://jactor-rises.github.io/jactor-cucumber/generated/2020-05-05/)
[2020-05-01/](https://jactor-rises.github.io/jactor-cucumber/generated/2020-05-01/)
[2020-04-19/](https://jactor-rises.github.io/jactor-cucumber/generated/2020-04-19/)
