package io.ipfs.multiformats.multiaddr

/**
 * changjiashuai@gmail.com.
 *
 * Created by CJS on 2018/7/23.
 */
class PortTranscoder : Transcoder {

    override fun stringToBytes(str: String): ByteArray {
        if (isValidRange(str)) {
            val x = Integer.parseInt(str)
            return byteArrayOf((x shr 8).toByte(), x.toByte())
        }
        throw IllegalArgumentException("port not in [0,65535] range")
    }

    override fun bytesToString(bytes: ByteArray): String {
        return uint16(bytes[0], bytes[1]).toString()
    }

    override fun isValidBytes(bytes: ByteArray): Boolean {
        return isValidRange(bytesToString(bytes))
    }

    private fun isValidRange(str: String): Boolean {
        return try {
            str.toInt() in 0..65535 //[0,65535]
        } catch (e: Exception) {
            false
        }
    }
}