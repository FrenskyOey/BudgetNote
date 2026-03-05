package core.util.log

actual class PlatformLogger actual constructor() : LogInterface {
    override actual fun debug(tag: String, message: String) {
        println("D/$tag: $message")
    }

    override actual fun info(tag: String, message: String) {
        println("I/$tag: $message")
    }

    override actual fun warning(tag: String, message: String) {
        println("W/$tag: $message")
    }

    override actual fun error(tag: String, message: String, throwable: Throwable?) {
        println("E/$tag: $message")
        throwable?.printStackTrace()
    }
}
