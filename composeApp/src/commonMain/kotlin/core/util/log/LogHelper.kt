package core.util.log

import com.app.budgetnote.core.config.BuildKonfig


/**
 * Central logging utility. Receives a [Log] dependency via constructor injection (Koin).
 * This makes it trivial to swap the backend or replace with a fake in tests.
 *
 * Usage (after injecting via Koin):
 *   logHelper.debug("Fetching accounts…")
 *   logHelper.error("Login failed", error = exception)
 *   logHelper.error("Login failed", error = exception, tag = "Auth")
 *
 * Logs are suppressed entirely in production builds (FLAVOR_NAME == "production").
 */
class LogHelper(private val logger: LogInterface = PlatformLogger()) {

    private val isDebug: Boolean
        get() = BuildKonfig.FLAVOR_NAME != "production"

    private companion object {
        const val DEFAULT_TAG = "BudgetNote"
    }

    fun debug(message: String, tag: String = DEFAULT_TAG) {
        if (isDebug) logger.debug(tag, message)
    }

    fun info(message: String, tag: String = DEFAULT_TAG) {
        if (isDebug) logger.info(tag, message)
    }

    fun warning(message: String, tag: String = DEFAULT_TAG) {
        if (isDebug) logger.warning(tag, message)
    }

    fun error(message: String, error: Exception? = null, tag: String = DEFAULT_TAG) {
        if (isDebug) logger.error(tag, message, error)
    }
}
