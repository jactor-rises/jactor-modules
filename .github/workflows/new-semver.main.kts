#!/usr/bin/env kotlin

/***********************************************
 * DESCRIPTION:
 * -----------
 * Will find next patched version when given current snapshot-version and current major/minor-version (using semantic
 * versioning)
 * -
 * Major and minor version will be determined by previous tag and the patch-version is bumped.
 * -
 * The new patched version (full semantic version, <major>.<minor>.<patch>) will be returned from the script.
 * -
 * Note! If the major/minor version is less than current semantic version or larger than bumped semantic version, the
 * script will fail...
 */
object Argument {
    const val MAJOR_MINOR = "majorMinor"
    const val CURRENT_SEMANTIC_VERSION = "currentSemVer"
}

object Constants {
    const val ENVIRONMENT_IS_DEBUG = "IS_DEBUG"

    private const val SCRIPT_FILE_NAME = "new-semver.main.kts"

    val ERROR_MESSAGE = """
            >>> ERROR <<<
            {}
              Usage: $SCRIPT_FILE_NAME <mapped args, ie: ${Argument.MAJOR_MINOR}=? ${Argument.CURRENT_SEMANTIC_VERSION}=?>
              - ex: ./$SCRIPT_FILE_NAME ${Argument.MAJOR_MINOR}=2.0 ${Argument.CURRENT_SEMANTIC_VERSION}=2.0.15
        """.trimIndent()
}

private val isDebug = System.getenv(Constants.ENVIRONMENT_IS_DEBUG)?.toBoolean() ?: false
private val allArgs = args.joinToString(" ")
private var majorMinorVersion: String
private var semanticVersion: String

debugMessage("all args: $allArgs")

if (args.size < 2) {
    errorMessage("Two arguments are required!")
}

val commands = buildMap {
    allArgs.split(Regex(" "))
        .filter { it.contains('=') }
        .filter { !it.endsWith('=') }
        .filter { !it.startsWith('=') }
        .forEach {
            val keyValue = it.split('=')
            put(key = keyValue[0], value = keyValue[1])
        }
}

debugMessage("map args: $commands")

// read arguments
majorMinorVersion = commands[Argument.MAJOR_MINOR] ?: errorMessage(
    "${Argument.MAJOR_MINOR} argument is not supplied!"
)

semanticVersion = commands[Argument.CURRENT_SEMANTIC_VERSION] ?: errorMessage(
    "${Argument.CURRENT_SEMANTIC_VERSION} argument is not supplied!"
)

val newSemanticVersion = createNewSemanticVersion()

println(newSemanticVersion)

fun errorMessage(message: String): Nothing = Constants.ERROR_MESSAGE.replace("{}", message).let {
    error(it)
}

fun createNewSemanticVersion(): String {
    debugMessage("creating new semantic version from $majorMinorVersion and current semantic version ($semanticVersion)")

    if (!semanticVersion.startsWith(majorMinorVersion)) {
        return createNewSemanticVersion(
            majorMinorVersion.split(".")[0].toInt(),
            majorMinorVersion.split(".")[1].toInt(),
            semanticVersion.split(".")[0].toInt(),
            semanticVersion.split(".")[1].toInt()
        )
    }

    val currentPatch = semanticVersion.substring(semanticVersion.lastIndexOf('.') + 1).toInt()

    return "$majorMinorVersion.${currentPatch + 1}"
}

fun createNewSemanticVersion(
    majorVersion: Int,
    minorVersion: Int,
    currentMajorVersion: Int,
    currentMinorVersion: Int
): String {
    debugMessage(
        """
            supplied major version: $majorVersion
            supplied minor version: $minorVersion
            snapshot major version: $currentMajorVersion
            snapshot minor version: $currentMinorVersion
        """.trimIndent()
    )

    if (majorVersion < currentMajorVersion) {
        error("Supplied major/minor version ($majorMinorVersion) is less than current snapshot major version!")
    }

    if (majorVersion == currentMajorVersion && minorVersion < currentMinorVersion) {
        error("Supplied major/minor version ($majorMinorVersion) is less than current snapshot major/minor version")
    }

    if (majorVersion > (currentMajorVersion + 1)) {
        error("New major version should only be bumped! (new: $majorVersion, current: $currentMajorVersion)")
    }

    if (majorVersion == currentMajorVersion && minorVersion > (currentMinorVersion + 1)) {
        error("New minor version should only be bumped! (new: $minorVersion, current: $currentMinorVersion)")
    }

    return "$majorMinorVersion.0"
}

fun debugMessage(message: String) {
    if (isDebug) println(message)
}
