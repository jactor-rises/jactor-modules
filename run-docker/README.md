# jactor-persistence/run-docker

This action will run the image tag given using the github actor and github token in the workflow where it
is used.

This action is build with ncc (npm) and will be executed with a shell script and is rebuild in a
separate workflow on feature branches. The only files intended to be changed by other than this workflow
is `action.yaml`, `index.js`, `package.json`, and/or `run.sh`. 
