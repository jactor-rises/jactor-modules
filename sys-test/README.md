# jactor-cucumber
Integrsjonstesting av jactor-persistence

[![run tests](https://github.com/jactor-rises/jactor-cucumber/actions/workflows/tests.yaml/badge.svg)](https://github.com/jactor-rises/jactor-cucumber/actions/workflows/tests.yaml)
[![scheduled integration tests](https://github.com/jactor-rises/jactor-cucumber/actions/workflows/schedule.yaml/badge.svg)](https://github.com/jactor-rises/jactor-cucumber/actions/workflows/schedule.yaml)

## Integrasjonstesting med maven
Dette er et [maven](https://maven.apache.org) prosjekt som kjører [cucumber](https://cucumber.io) med [Kotlin](https://kotlinlang.org)

Utføring av testing gjøres med kommandoen `mvn test -P cucumber` i rotkatalogen til prosjektet

## Test rapportering

Når testene kjøres av prosjektets workflows vil en [cucumber report](https://reports.cucumber.io) bli generert. Link til denne finner du under tabben
"Action", workflows: "run tests" eller "scheduled integration tests" og på bunnen av utskriften til steget `Run mvn test -P cucumber`. Du kan også
publisere testresulater fra lokal kjøring ved å sette miljøvariabelen `CUCUMBER_PUBLISH_ENABLED=true`
