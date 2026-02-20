package core.util

import okio.ByteString.Companion.encodeUtf8

/**
 * Utility object for cryptographic hashing operations.
 */
object HashUtil {
    /**
     * Generates MD5 hash of the input string using Okio.
     * 
     * @param input String to hash
     * @return MD5 hash as hexadecimal string
     */
    fun md5(input: String): String {
        return input.encodeUtf8().md5().hex()
    }
}
