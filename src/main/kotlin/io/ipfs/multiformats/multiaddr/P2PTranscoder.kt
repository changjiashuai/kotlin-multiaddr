package io.ipfs.multiformats.multiaddr

import io.ipfs.multiformats.multihash.Multihash

/**
 * changjiashuai@gmail.com.
 *
 * Created by CJS on 2018/7/23.
 */
class P2PTranscoder : Transcoder {

    override fun stringToBytes(str: String): ByteArray {
        // the address is a varint prefixed multihash string representation
        val bytes = Multihash.fromBase58String(str).raw
        println("P2PTranscoder bytes=${bytes.contentToString()}")
        if (isValidBytes(bytes)) {
            return bytes
        } else {
            throw IllegalArgumentException("multihash length inconsistent")
        }
    }

    override fun bytesToString(bytes: ByteArray): String {
//        if (isValidBytes(bytes)) {
        println("p2p bytes=${bytes.contentToString()}")
            return Multihash.cast(bytes).toBase58String()
//        } else {
//            throw IllegalArgumentException("multihash length inconsistent")
//        }
    }

    override fun isValidBytes(bytes: ByteArray): Boolean {
        try {
            Multihash.cast(bytes)
        } catch (e: Exception) {
            return false
        }
        return true
    }
}