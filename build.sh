#!/usr/bin/env bash
cd jactor-commons
mvn clean install
cd ../jactor-standalone/persistence-orm
mvn clean install
cd ../../jactor-module
mvn clean install
