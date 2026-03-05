package core.util.log

import android.util.Log

actual class PlatformLogger actual constructor() : LogInterface {
    override actual fun debug(tag: String, message: String) {
        Log.d(tag, message)
    }

    override actual fun info(tag: String, message: String) {
        Log.i(tag, message)
    }

    override actual fun warning(tag: String, message: String) {
        Log.w(tag, message)
    }

    override actual fun error(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }
}
