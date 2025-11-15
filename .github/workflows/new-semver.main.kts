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
object Constants {
    const val ENVIRONMENT_IS_DEBUG = "IS_DEBUG"
    const val MAJOR_MINOR = "majorMinor"
    const val CURRENT_SEMANTIC_VERSION = "currentSemVer"
}

private fun Array<java.io.File>.findSciptFile(): java.io.File? {
    val kts = filter { it.extension == "kts" }

    if (kts.isEmpty()) return null

    return when {
        kts.size == 1 -> kts[0]
        else -> kts.firstOrNull { it.nameWithoutExtension.contains("semver") }
    }
}

private val isDebug = System.getenv(Constants.ENVIRONMENT_IS_DEBUG)?.toBoolean() ?: false

fun debugMessage(message: String) {
    if (isDebug) println(message)
}

class ScriptFile {
    val name: String
        get() = java.io.File(".").also { debugMessage("> current directory: ${it.absolutePath}") }
            .listFiles().also { files -> files?.forEach { debugMessage("> ${it.name}") } }
            ?.findSciptFile()
            ?.name ?: "<script-file>.kts"
}

private val scriptFileName = ScriptFile().name
private val errorMessage = """
    {}
        Usage: $scriptFileName <mapped args, ie: ${Constants.MAJOR_MINOR}=? ${Constants.CURRENT_SEMANTIC_VERSION}=?>
        - ex: ./$scriptFileName ${Constants.MAJOR_MINOR}=2.0 ${Constants.CURRENT_SEMANTIC_VERSION}=2.0.15
""".trimIndent()

fun errorWithMessage(message: String): Nothing = errorMessage.replace("{}", message).let {
    error(it)
}

private val allArgs = args.joinToString(" ")
private var majorMinorVersion: String
private var semanticVersion: String

debugMessage("all args: $allArgs")

if (args.size < 2) {
    errorWithMessage("Two arguments are required!")
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
majorMinorVersion = commands[Constants.MAJOR_MINOR] ?: errorWithMessage(
    "${Constants.MAJOR_MINOR} argument is not supplied!"
)

semanticVersion = commands[Constants.CURRENT_SEMANTIC_VERSION] ?: errorWithMessage(
    "${Constants.CURRENT_SEMANTIC_VERSION} argument is not supplied!"
)

val newSemanticVersion = createNewSemanticVersion()

println(newSemanticVersion)

fun createNewSemanticVersion(): String {
    debugMessage("creating new semantic version from $majorMinorVersion and current semantic version ($semanticVersion)")

    if (!semanticVersion.startsWith(majorMinorVersion)) {
        return createNewSemanticVersion(
            majorVersion = majorMinorVersion.split(".")[0].toInt(),
            minorVersion = majorMinorVersion.split(".")[1].toInt(),
            currentMajorVersion = semanticVersion.split(".")[0].toInt(),
            currentMinorVersion = semanticVersion.split(".")[1].toInt()
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
