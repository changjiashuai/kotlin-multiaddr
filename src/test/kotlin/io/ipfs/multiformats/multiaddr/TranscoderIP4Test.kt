package io.ipfs.multiformats.multiaddr

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * changjiashuai@gmail.com.
 *
 * Created by CJS on 2018/7/23.
 */
class TranscoderIP4Test {

    private val ipv4Str = "www.baidu.com"
    private val ipv6Str = "2601:9:4f81:9700:803e:ca65:66e8:c21"
    private val ipv4Bytes = byteArrayOf(14, -41, -79, 38)
    private val ipv6Bytes = byteArrayOf(38, 1, 0, 9, 79, -127, -105, 0, -128, 62, -54, 101, 102, -24, 12, 33)

    @Test
    fun stringToBytes() {
        val bytes = IPv4Transcoder().stringToBytes(ipv4Str)
        println("bytes=${bytes.contentToString()}")
    }

    @Test
    fun bytesToString() {
        val str = IPv4Transcoder().bytesToString(ipv4Bytes)
        println("str=$str")
    }

    @Test
    fun validateBytes() {
        val iPv4Transcoder = IPv4Transcoder()
        assertTrue(iPv4Transcoder.isValidBytes(ipv4Bytes))
        assertFalse(iPv4Transcoder.isValidBytes(ipv6Bytes))
    }
}