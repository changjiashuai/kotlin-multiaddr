package io.ipfs.multiformats.multiaddr

/**
 * changjiashuai@gmail.com.
 *
 * Created by CJS on 2018/7/23.
 */
interface Transcoder {
    fun stringToBytes(str: String): ByteArray
    fun bytesToString(bytes: ByteArray): String
    fun isValidBytes(bytes: ByteArray): Boolean
}