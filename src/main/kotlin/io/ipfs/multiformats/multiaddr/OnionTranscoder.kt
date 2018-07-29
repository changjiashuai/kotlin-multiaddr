package io.ipfs.multiformats.multiaddr

import org.apache.commons.codec.binary.Base32

/**
 * changjiashuai@gmail.com.
 *
 * Created by CJS on 2018/7/23.
 */
class OnionTranscoder : Transcoder {

    override fun stringToBytes(str: String): ByteArray {
        val sp = str.split(":")
        if (sp.size != 2) {
            throw IllegalArgumentException("failed to parse onion addr: $str does not contain a port number.")
        }
        val addr = sp[0]
        // onion address without the ".onion" substring
        if (addr.length != 16) {
            throw IllegalArgumentException("failed to parse onion addr: $str not a Tor onion address.")
        }
        val onionHostBytes: ByteArray
        val base32 = Base32()
        if (base32.isInAlphabet(addr.toUpperCase())) {
            onionHostBytes = base32.decode(addr.toUpperCase())
        } else {
            throw IllegalArgumentException("illegal base32 data onion addr: $str")
        }
        // onion port number
        val port = sp[1].toInt()
        if (port >= 65535) {
            throw IllegalArgumentException("failed to parse onion addr: port greater than 65535")
        }
        if (port < 1) {
            throw  IllegalArgumentException("failed to parse onion addr: port less than 1")
        }
        return onionHostBytes.plus(port.toByte())
    }

    override fun bytesToString(bytes: ByteArray): String {
        val addr = Base32().encodeToString(bytes.copyOfRange(0, 10))
        if (isValidBytes(addr.toByteArray())) {
            val port = String(bytes.copyOfRange(10, 12))
            return "$addr:$port"
        } else {
            throw IllegalArgumentException("illegal base32 data onion addr bytes: $bytes")
        }
    }

    override fun isValidBytes(bytes: ByteArray): Boolean {
        return Base32().isInAlphabet(String(bytes).toUpperCase())
    }
}