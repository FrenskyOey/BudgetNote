package core.util.log

import platform.Foundation.NSLog

actual class PlatformLogger actual constructor() : LogInterface {
    override actual fun debug(tag: String, message: String) {
        NSLog("D/$tag: $message")
    }

    override actual fun info(tag: String, message: String) {
        NSLog("I/$tag: $message")
    }

    override actual fun warning(tag: String, message: String) {
        NSLog("W/$tag: $message")
    }

    override actual fun error(tag: String, message: String, throwable: Throwable?) {
        NSLog("E/$tag: $message")
        throwable?.printStackTrace()
    }
}
