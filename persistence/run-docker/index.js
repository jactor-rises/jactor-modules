const core = require("@actions/core");
const exec = require("@actions/exec");

async function run() {
  try {
    // Execute verify bash script
    await exec.exec(`${__dirname}/../run.sh`);
  } catch (error) {
    core.setFailed(error.message);
  }
}

// noinspection JSIgnoredPromiseFromCall
run();
