#!/usr/bin/env bash

cd jactor-commons
mvn clean install
STATUS=$?

if [ $STATUS -gt 0 ]; then
  exit ${STATUS}
fi

cd ../jactor-standalones/jactor-persistence-orm
mvn clean install
STATUS=$?

if [ $STATUS -gt 0 ]; then
  exit ${STATUS}
fi

cd ../../jactor-modules
mvn clean install
STATUS=$?

if [ $STATUS -gt 0 ]; then
  exit ${STATUS}
fi

cd ../jactor-standalones/jactor-web/
mvn clean install
STATUS=$?

if [ $STATUS -gt 0 ]; then
  exit ${STATUS}
fi
