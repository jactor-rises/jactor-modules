# jactor-cucumber
Integrsjonstesting av jactor-persistence

![](https://github.com/jactor-rises/jactor-cucumber/workflows/bump%20version/badge.svg)

### Integrasjonstesting med maven
Dette er et [maven](https://maven.apache.org) prosjekt som kjører [cucumber](https://cucumber.io) med [Kotlin](https://kotlinlang.org)

Utføring av testing gjøres med kommandoen `mvn test` i rotkatalogen til prosjektet

### Test rapportering

Etter at testing er gjennomført så kan man lage en rapport som blir tilgjengelig i
`target/generated-report/index.html`. Dette gjøres av en maven-plugin:
```
mvn cluecumber-report:reporting
```
Man kan også gjøre både testing og rapportgenerering i et steg med `mvn install`
