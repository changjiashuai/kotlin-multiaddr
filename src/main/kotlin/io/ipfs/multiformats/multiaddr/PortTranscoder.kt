package io.ipfs.multiformats.multiaddr

import java.nio.ByteBuffer
import java.nio.ByteOrder

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
        val buffer = ByteBuffer.allocate(bytes.size)
        buffer.order(ByteOrder.BIG_ENDIAN)
        buffer.put(bytes)
        return buffer.getShort(0).toString()
    }

    override fun isValidBytes(bytes: ByteArray): Boolean {
        return isValidRange(uint16(bytes[0], bytes[1]).toString())
    }

    private fun isValidRange(str: String): Boolean {
        return str.toInt() in 0..65535 //[0,65535]
    }
}