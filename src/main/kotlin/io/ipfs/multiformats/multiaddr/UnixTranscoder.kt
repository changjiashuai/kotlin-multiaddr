package io.ipfs.multiformats.multiaddr

/**
 * changjiashuai@gmail.com.
 *
 * Created by CJS on 2018/7/23.
 */
class UnixTranscoder : Transcoder {

    override fun stringToBytes(str: String): ByteArray {
        return str.toByteArray()
    }

    override fun bytesToString(bytes: ByteArray): String {
        return String(bytes)
    }

    override fun isValidBytes(bytes: ByteArray): Boolean {
        return true
    }
}