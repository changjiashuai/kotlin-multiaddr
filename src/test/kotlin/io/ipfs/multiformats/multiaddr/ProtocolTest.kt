package io.ipfs.multiformats.multiaddr

import org.junit.Test

/**
 * changjiashuai@gmail.com.
 *
 * Created by CJS on 2018/7/15.
 */
class ProtocolTest {

    @Test
    fun protocolsWithString() {
        val address = "/ip4/1.2.3.4/tcp/80"
        val ps = Protocol.protocolsWithString(address)
        println(ps)
    }

    @Test
    fun test1() {
        val code = 479
        val varint = ByteArray((32 - Integer.numberOfLeadingZeros(code) + 6) / 7)
        println("varint=$varint")
        varint.forEachIndexed { index, byte ->
            println("$index=$byte")
        }
    }

    @Test
    fun stringToByteArray() {
        val address = "/ip4/127.0.0.1/udp/1234"
        println("s=${stringToBytes(address).contentToString()}")
        val b = ByteArray(2)
    }
}