package core.util

import okio.ByteString.Companion.encodeUtf8

object HashingUtil {
    fun md5(input: String): String {
        return input.encodeUtf8().md5().hex()
    }
}
