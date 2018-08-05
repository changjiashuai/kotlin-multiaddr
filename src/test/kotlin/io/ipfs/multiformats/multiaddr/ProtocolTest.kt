package io.ipfs.multiformats.multiaddr

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * changjiashuai@gmail.com.
 *
 * Created by CJS on 2018/7/15.
 */
class ProtocolTest {

    @Test
    fun protocolsWithString() {
        val a1 = "////////ip4/tcp"
        val a2 = "/ip4"
        val a3 = "/ip4/tcp"
        val a4 = "ip4/tcp/udp/ip6"
        val a5 = "ip4/udp/////////"
        val a6 = "////////ip4/tcp////////"
        val p1 = Protocol.protocolsWithString(a1)
        val p2 = Protocol.protocolsWithString(a2)
        val p3 = Protocol.protocolsWithString(a3)
        val p4 = Protocol.protocolsWithString(a4)
        val p5 = Protocol.protocolsWithString(a5)
        val p6 = Protocol.protocolsWithString(a6)

        //a1
        assertEquals("ip4", p1[0].named)
        assertEquals("tcp", p1[1].named)

        //a2
        assertEquals("ip4", p2[0].named)

        //a3
        assertEquals("ip4", p3[0].named)
        assertEquals("tcp", p3[1].named)

        //a4
        assertEquals("ip4", p4[0].named)
        assertEquals("tcp", p4[1].named)
        assertEquals("udp", p4[2].named)
        assertEquals("ip6", p4[3].named)

        //a5
        assertEquals("ip4", p5[0].named)
        assertEquals("udp", p5[1].named)

        //a6
        assertEquals("ip4", p6[0].named)
        assertEquals("tcp", p6[1].named)

        val badStr = "dsijafd"
        val p = Protocol.protocolsWithString(badStr)
        assertEquals(0, p.size)
    }
}