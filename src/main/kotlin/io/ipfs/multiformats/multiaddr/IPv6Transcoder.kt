package io.ipfs.multiformats.multiaddr

import java.net.Inet6Address

/**
 * changjiashuai@gmail.com.
 *
 * Created by CJS on 2018/7/23.
 */
class IPv6Transcoder : Transcoder {

    override fun stringToBytes(str: String): ByteArray {
        if (Inet6Address.getByName(str) is Inet6Address) {
            return Inet6Address.getByName(str).address
        }
        throw IllegalArgumentException("invalid ipv6 str")
    }

    override fun bytesToString(bytes: ByteArray): String {
        if (isValidBytes(bytes)) {
            return Inet6Address.getByAddress(bytes).hostName
        }
        throw IllegalArgumentException("invalid ipv6 bytes")
    }

    override fun isValidBytes(bytes: ByteArray): Boolean {
        return Inet6Address.getByAddress(bytes) is Inet6Address
    }
}