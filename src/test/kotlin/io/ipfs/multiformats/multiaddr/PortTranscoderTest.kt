package io.ipfs.multiformats.multiaddr

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * changjiashuai@gmail.com.
 *
 * Created by CJS on 2018/7/23.
 */
class PortTranscoderTest {

    @Test
    fun stringToBytes() {
        val s1 = "0"
        val s2 = "65535"
        val b1 = PortTranscoder().stringToBytes(s1)
        val b2 = PortTranscoder().stringToBytes(s2)
        assertEquals("[0, 0]", b1.contentToString())
        assertEquals("[-1, -1]", b2.contentToString())
    }

    @Test(expected = IllegalArgumentException::class)
    fun badString() {
        val s1 = "-1"
        val s2 = "65536"
        val b1 = PortTranscoder().stringToBytes(s1)
        val b2 = PortTranscoder().stringToBytes(s2)
        println(b1.contentToString())
        println(b2.contentToString())

        //[-1, -1]
        //[0, 0]
    }

    @Test
    fun bytesToString() {
        val b1 = byteArrayOf(0, 0)
        val b2 = byteArrayOf(-1, -1)
        val s1 = PortTranscoder().bytesToString(b1)
        val s2 = PortTranscoder().bytesToString(b2)
        assertEquals("0", s1)
        assertEquals("65535", s2)
    }

    @Test
    fun badBytes(){

    }

    @Test
    fun validateBytes() {
        val b1 = byteArrayOf(127, 127)
        assertTrue(PortTranscoder().isValidBytes(b1))
//        assertFalse(PortTranscoder().isValidBytes(outRangeBytes))
    }
}