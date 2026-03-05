package core.util.log

/**
 * Marker `expect` class that each platform replaces with a real [Log] implementation.
 * This keeps [LogHelper] free of `expect`/`actual` boilerplate.
 */
expect class PlatformLogger() : LogInterface{
    override fun debug(tag: String, message: String)
    override fun error(tag: String, message: String, throwable: Throwable?)
    override fun info(tag: String, message: String)
    override fun warning(tag: String, message: String)
}
