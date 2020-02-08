# jactor-cucumber
Integration testing of jactor-persistence

### Test rapportering

Etter at testing er gjennomført så kan man lage en rapport som blir tilgjengelig
i `target/generated-report/index.html`. Dette gjøres av en maven-plugin:
```
mvn cluecumber-report:reporting
```
Man kan også gjøre både testing og rapportgenerering i et steg med `mvn install`
