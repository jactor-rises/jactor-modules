# jactor-persistence/run-docker

![](https://github.com/jactor-rises/jactor-persistence/workflows/build%20run%20docker/badge.svg)

This action will run the created docker image for jactor-persistence using the IMAGE variable provided.  Login to ghcr.io (GitHub container registry)
is provided by the GITHUB_TOKEN and GITHUB_REPOSITORY where the action is used.

This action is build with ncc (npm) and will be executed with a shell script and is rebuild in a separate workflow on feature branches. The only files
intended to be changed by other than this workflow is `action.yaml`, `index.js`, `package.json`, and/or `run.sh`.

A name of the docker image as an environment variable must be present when the action runs in the workflow. The expected variable name is IMAGE.
