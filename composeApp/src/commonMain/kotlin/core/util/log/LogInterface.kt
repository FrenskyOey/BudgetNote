package core.util.log

/**
 * Platform-agnostic logging contract.
 * Implement this interface to plug in any logging library (e.g. Timber, OSLog, SLF4J).
 * Being an interface also makes it easy to provide a fake in tests.
 */
interface LogInterface {
    fun debug(tag: String, message: String)
    fun info(tag: String, message: String)
    fun warning(tag: String, message: String)
    fun error(tag: String, message: String, throwable: Throwable? = null)
}
