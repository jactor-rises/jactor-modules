#!/usr/bin/env bash
set -e

if [[ -z ${GITHUB_ACTOR} ]]; then
  echo "no actor to use as user for docker login"
  exit 1;
fi

if [[ -z ${GITHUB_TOKEN} ]]; then
  echo "no token to use as password for docker login"
  exit 1;
fi

if [[ -z ${IMAGE} ]]; then
  echo "no docker image to run has been configured"
  exit 1;
fi

echo ${GITHUB_TOKEN} | docker login docker.pkg.github.com -u ${GITHUB_ACTOR} --password-stdin
docker run --detach --publish 1099:1099 ${IMAGE}

echo "Starting jactor-persistence "

RUNNING=""
PROGRESS=""

while [[ -z "$RUNNING" ]]
do
  HEALTH=$(curl --silent http://localhost:1099/jactor-persistence/actuator/health || true)
  RUNNING=$(echo "$HEALTH" | grep "\"status\":\"UP\"" || true)
  PROGRESS="$PROGRESS."
  echo -n "$PROGRESS"
  sleep 1
done

echo "jactor-presistene is started..."
