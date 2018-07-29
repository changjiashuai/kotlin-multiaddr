package io.ipfs.multiformats.multiaddr

import org.junit.Test

/**
 * changjiashuai@gmail.com.
 *
 * Created by CJS on 2018/7/23.
 */
class PortTranscoderTest {

    private val str = "65537"
    private val bytes = byteArrayOf(49, 50, 51, 52, 53)
    private val outRangeBytes = byteArrayOf(54, 53, 53, 51, 55) //65537

    @Test
    fun stringToBytes() {
        val bytes = PortTranscoder().stringToBytes(str)
        println("bytes=${bytes.contentToString()}")
    }

    @Test
    fun bytesToString() {
        val str = PortTranscoder().bytesToString(bytes)
        println("str=$str")
    }

    @Test
    fun validateBytes() {
        println("===" + PortTranscoder().isValidBytes(outRangeBytes))
    }
}