package io.ipfs.multiformats.multiaddr

import java.net.Inet4Address

/**
 * changjiashuai@gmail.com.
 *
 * Created by CJS on 2018/7/23.
 */
class IPv4Transcoder : Transcoder {

    override fun stringToBytes(str: String): ByteArray {
        if (Inet4Address.getByName(str) is Inet4Address) {
            return Inet4Address.getByName(str).address
        }
        throw IllegalArgumentException("invalid ipv4 str")
    }

    override fun bytesToString(bytes: ByteArray): String {
        if (isValidBytes(bytes)) {
            return Inet4Address.getByAddress(bytes).hostAddress
        }
        throw IllegalArgumentException("invalid ipv4 bytes")
    }

    override fun isValidBytes(bytes: ByteArray): Boolean {
        return Inet4Address.getByAddress(bytes) is Inet4Address
    }
}