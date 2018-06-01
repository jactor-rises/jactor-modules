#!/usr/bin/env bash
cd jactor-commons
mvn clean install
cd ../jactor-standalones/persistence-orm
mvn clean install
cd ../../jactor-modules
mvn clean install
