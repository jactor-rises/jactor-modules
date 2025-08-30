# jactor-modules

[![continuous integration on jactor-persistence](https://github.com/jactor-rises/jactor-modules/actions/workflows/persistence-ci.yaml/badge.svg)](https://github.com/jactor-rises/jactor-modules/actions/workflows/persistence-ci.yaml)
[![continuous integration on jactor-web](https://github.com/jactor-rises/jactor-modules/actions/workflows/web-ci.yaml/badge.svg)](https://github.com/jactor-rises/jactor-modules/actions/workflows/web-ci.yaml)
[![verify pull request](https://github.com/jactor-rises/jactor-modules/actions/workflows/pr.yaml/badge.svg)](https://github.com/jactor-rises/jactor-modules/actions/workflows/pr.yaml)

## gradle multi-build

Two standalone microservices merget into one gradle build.
- https://github.com/jactor-rises/jactor-persistence -> [jactor-modules/persistence](https://github.com/jactor-rises/jactor-modules/tree/main/persistence) 
- https://github.com/jactor-rises/jactor-web -> [jactor-modules/web](https://github.com/jactor-rises/jactor-modules/tree/main/web)

These microservices also have common http-api which was previously a separate module:
- https://github.com/jactor-rises/jactor-shared -> [jactor-modules/shared](https://github.com/jactor-rises/jactor-modules/tree/main/shared)

These three projects share common test code: [jactor-modules/shared-test](https://github.com/jactor-rises/jactor-modules/tree/main/shared-test)

For details on each microservice, see [persistence/README.md](https://github.com/jactor-rises/jactor-modules/blob/main/persistence/README.md)
and [web/README.md](https://github.com/jactor-rises/jactor-modules/blob/main/web/README.md)
