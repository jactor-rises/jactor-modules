#!/usr/bin/env bash

export MAVEN_OPTS=-Xmx1024m

mvn clean install
STATUS=$?

if [ $STATUS -gt 0 ]; then
  exit ${STATUS}
fi

cd jactor-applications
mvn clean install
STATUS=$?

if [ $STATUS -gt 0 ]; then
  exit ${STATUS}
fi
