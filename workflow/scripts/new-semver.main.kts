#!/usr/bin/env kotlin

import java.io.File
import java.nio.file.Files

/***********************************************
 * DESCRIPTION:
 * -----------
 * Will find next patched version when given a semantic-version
 */
object Argument {
    const val SEMVER = "semver"
}

object Constants {
    const val ENVIRONMENT_IS_DEBUG = "IS_DEBUG"
    const val FILE_NAME_NEW_SEM_VER = "newSemVer"

    private const val SCRIPT_FILE_NAME = "new-semver.main.kts"

    val ERROR_MESSAGE = """
            >>> ERROR <<<
            {}
              Usage: $SCRIPT_FILE_NAME <mapped args, ie: ${Argument.SEMVER}=?<
              - ex: ./$SCRIPT_FILE_NAME ${Argument.SEMVER}=2.0.15
        """.trimIndent()
}

val isDebug = System.getenv(Constants.ENVIRONMENT_IS_DEBUG)?.toBoolean() ?: false
val allArgs = args.joinToString(" ")

debugMessage("all args: $allArgs")

if (args.size != 1) {
    throw IllegalArgumentException(errMsg("Input argument required!"))
}

val inputs = allArgs.split(Regex(" "))
val commands: MutableMap<String, String> = HashMap()

inputs.filter { it.contains('=') }.filter { !it.endsWith('=') }.forEach {
    val key = it.split("=")[0]
    val value = it.split("=")[1]
    commands[key] = value
}

debugMessage("map args: $commands")

// read arguments
val semanticVersion = commands[Argument.SEMVER] ?: throw IllegalArgumentException(errMsg("${Argument.SEMVER} argument is not supplied!"))

val newSemanticVersion = createNewSemanticVersion()
val semVerFile = File(Constants.FILE_NAME_NEW_SEM_VER)

Files.write(semVerFile.toPath(), newSemanticVersion.toByteArray())

fun errMsg(message: String): String = Constants.ERROR_MESSAGE.replace("{}", message)
fun createNewSemanticVersion(): String {
    debugMessage("creating new semantic version from given semantic version ($semanticVersion)")

    val lastIndexOfDot = semanticVersion.lastIndexOf('.')
    val majorMinorVersion = semanticVersion.substring(0, lastIndexOfDot)
    val currentPatchVersion = semanticVersion.substring(lastIndexOfDot + 1).toInt()

    return "$majorMinorVersion.${currentPatchVersion + 1}"
}

fun debugMessage(message: String) {
    if (isDebug) println(message)
}
