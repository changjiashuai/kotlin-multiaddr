package io.ipfs.multiformats.multiaddr

import org.apache.commons.codec.binary.Hex
import org.junit.Assert.*
import org.junit.Test
import java.util.*

/**
 * changjiashuai@gmail.com.
 *
 * Created by CJS on 2018/7/25.
 */
class MultiaddrTest {

    private val failedCase = listOf(
            "/ip6/127.0.0.1/::1",
            "/ip4",
            "/ip4/::1",
            "/ip4/fdpsofodsajfdoisa",
            "/ip6",
            "/udp",
            "/tcp",
            "/sctp",
            "/udp/65536",
            "/tcp/65536",
            "/quic/65536",
            "/onion/9imaq4ygg2iegci7:80",
            "/onion/aaimaq4ygg2iegci7:80",
            "/onion/timaq4ygg2iegci7:0",
            "/onion/timaq4ygg2iegci7:-1",
            "/onion/timaq4ygg2iegci7",
            "/onion/timaq4ygg2iegci@:666",
            "/udp/1234/sctp",
            "/udp/1234/udt/1234",
            "/udp/1234/utp/1234",
            "/ip4/127.0.0.1/udp/jfodsajfidosajfoidsa",
            "/ip4/127.0.0.1/udp",
            "/ip4/127.0.0.1/tcp/jfodsajfidosajfoidsa",
            "/ip4/127.0.0.1/tcp",
            "/ip4/127.0.0.1/quic/1234",
            "/ip4/127.0.0.1/ipfs",
            "/ip4/127.0.0.1/ipfs/tcp",
            "/ip4/127.0.0.1/p2p",
            "/ip4/127.0.0.1/p2p/tcp",
            "/unix",
            "/ip4/1.2.3.4/tcp/80/unix"
    )

    private val succeedCase = listOf(
            "/ip4/1.2.3.4",
            "/ip4/0.0.0.0",
            "/ip6/::1",
            "/ip6/2601:9:4f81:9700:803e:ca65:66e8:c21",
            "/ip6/2601:9:4f81:9700:803e:ca65:66e8:c21/udp/1234/quic",
            "/onion/timaq4ygg2iegci7:1234",
            "/onion/timaq4ygg2iegci7:80/http",
            "/udp/0",
            "/tcp/0",
            "/sctp/0",
            "/udp/1234",
            "/tcp/1234",
            "/sctp/1234",
            "/udp/65535",
            "/tcp/65535",
            "/ipfs/QmcgpsyWgH8Y8ajJz1Cu72KnS5uo2Aa2LpzU7kinSupNKC",
            "/p2p/QmcgpsyWgH8Y8ajJz1Cu72KnS5uo2Aa2LpzU7kinSupNKC",
            "/udp/1234/sctp/1234",
            "/udp/1234/udt",
            "/udp/1234/utp",
            "/tcp/1234/http",
            "/tcp/1234/https",
            "/ipfs/QmcgpsyWgH8Y8ajJz1Cu72KnS5uo2Aa2LpzU7kinSupNKC/tcp/1234",
            "/p2p/QmcgpsyWgH8Y8ajJz1Cu72KnS5uo2Aa2LpzU7kinSupNKC/tcp/1234",
            "/ip4/127.0.0.1/udp/1234",
            "/ip4/127.0.0.1/udp/0",
            "/ip4/127.0.0.1/tcp/1234",
            "/ip4/127.0.0.1/tcp/1234/",
            "/ip4/127.0.0.1/udp/1234/quic",
            "/ip4/127.0.0.1/ipfs/QmcgpsyWgH8Y8ajJz1Cu72KnS5uo2Aa2LpzU7kinSupNKC",
            "/ip4/127.0.0.1/ipfs/QmcgpsyWgH8Y8ajJz1Cu72KnS5uo2Aa2LpzU7kinSupNKC/tcp/1234",
            "/ip4/127.0.0.1/p2p/QmcgpsyWgH8Y8ajJz1Cu72KnS5uo2Aa2LpzU7kinSupNKC",
            "/ip4/127.0.0.1/p2p/QmcgpsyWgH8Y8ajJz1Cu72KnS5uo2Aa2LpzU7kinSupNKC/tcp/1234",
            "/unix/a/b/c/d/e",
            "/unix/stdio",
            "/ip4/1.2.3.4/tcp/80/unix/a/b/c/d/e/f",
            "/ip4/127.0.0.1/ipfs/QmcgpsyWgH8Y8ajJz1Cu72KnS5uo2Aa2LpzU7kinSupNKC/tcp/1234/unix/stdio",
            "/ip4/127.0.0.1/p2p/QmcgpsyWgH8Y8ajJz1Cu72KnS5uo2Aa2LpzU7kinSupNKC/tcp/1234/unix/stdio"
    )

    @Test(expected = IllegalArgumentException::class)
    fun testConstructorFailed() {
        failedCase.forEach {
            val md = Multiaddr(it)
        }
    }

    @Test
    fun testConstructorSucceed() {
        succeedCase.forEach {
            val md = Multiaddr(it)
        }
    }

    @Test
    fun getProtocols() {
        val ma = Multiaddr("/ip4/127.0.0.1/utp/tcp/5555/udp/1234/utp/ipfs/QmbHVEEepCi7rn7VL7Exxpd2Ci9NNB6ifvqwhsrbRMgQFP/unix/a/b/c/d/e")
        val ps = ma.getProtocols()
        assertEquals("ip4", ps[0].named)
        assertEquals("utp", ps[1].named)
        assertEquals("tcp", ps[2].named)
        assertEquals("udp", ps[3].named)
        assertEquals("utp", ps[4].named)
        assertEquals("p2p", ps[5].named)
        assertEquals("unix", ps[6].named)
    }

    @Test
    fun valueForProtocol() {
        val ma = Multiaddr("/ip4/127.0.0.1/utp/tcp/5555/udp/1234/utp/ipfs/QmbHVEEepCi7rn7VL7Exxpd2Ci9NNB6ifvqwhsrbRMgQFP/unix/a/b/c/d/e")
        assertEquals("127.0.0.1", ma.valueForProtocol(Protocol.IP4.code))
        assertEquals("", ma.valueForProtocol(Protocol.UTP.code))
        assertEquals("5555", ma.valueForProtocol(Protocol.TCP.code))
        assertEquals("1234", ma.valueForProtocol(Protocol.UDP.code))
        assertEquals("", ma.valueForProtocol(Protocol.UTP.code))
        assertEquals("a/b/c/d/e", ma.valueForProtocol(Protocol.UNIX.code))
        assertEquals("QmbHVEEepCi7rn7VL7Exxpd2Ci9NNB6ifvqwhsrbRMgQFP", ma.valueForProtocol(Protocol.IPFS.code))
        assertEquals("QmbHVEEepCi7rn7VL7Exxpd2Ci9NNB6ifvqwhsrbRMgQFP", ma.valueForProtocol(Protocol.P2P.code))
    }

    @Test
    fun encapsulate() {
        val md1 = Multiaddr("/ip4/127.0.0.1/udp/1234")
        val md2 = Multiaddr("/udp/5678")
        val str = md1.encapsulate(md2).toString()
        assertEquals("/ip4/127.0.0.1/udp/1234/udp/5678", str)
        assertNotEquals("/ip4/127.0.0.1/udp/1234/udp/1234", str)
    }

    @Test
    fun decapsulate() {
        val md1 = Multiaddr("/ip4/127.0.0.1/udp/5678/ip4/127.0.0.1/udp/1234/udp/1234")
        val md2 = Multiaddr("/ip4/127.0.0.1")
        val str = md1.decapsulate(md2).toString()
        assertEquals("/ip4/127.0.0.1/udp/5678/udp/1234/udp/1234", str)
    }

    private fun testString(s: String, hex: String): Boolean {
        val b1 = Hex.decodeHex(hex)
        val b2 = Multiaddr(s).toBytes()
        val flag = Arrays.equals(b1, b2)
        return flag && isValidBytes(b2)
    }

    @Test
    fun testStringToBytes() {
        assertTrue(testString("/ip4/127.0.0.1/udp/1234", "047f0000011104d2"))
        assertTrue(testString("/ip4/127.0.0.1/tcp/4321", "047f0000010610e1"))
        assertTrue(testString("/ip4/127.0.0.1/udp/1234/ip4/127.0.0.1/tcp/4321", "047f0000011104d2047f0000010610e1"))
    }

    private fun testBytes(s: String, hex: String): Boolean {
        val bytes = Hex.decodeHex(hex)
        val str = bytesToString(bytes)
        return s == str
    }

    @Test
    fun testBytesToString() {
        assertTrue(testBytes("/ip4/127.0.0.1/udp/1234", "047f0000011104d2"))
        assertTrue(testBytes("/ip4/127.0.0.1/tcp/4321", "047f0000010610e1"))
        assertTrue(testBytes("/ip4/127.0.0.1/udp/1234/ip4/127.0.0.1/tcp/4321", "047f0000011104d2047f0000010610e1"))
    }

    @Test
    fun testEqual() {
        val m1 = Multiaddr("/ip4/127.0.0.1/udp/1234")
        val m2 = Multiaddr("/ip4/127.0.0.1/tcp/1234")
        val m3 = Multiaddr("/ip4/127.0.0.1/tcp/1234")
        val m4 = Multiaddr("/ip4/127.0.0.1/tcp/1234")

        assertFalse(m1 == m2)
        assertFalse(m2 == m1)
        assertTrue(m2 == m3)
        assertTrue(m3 == m2)
        assertTrue(m1 == m1)
        assertTrue(m2 == m4)
        assertTrue(m4 == m3)
    }

    @Test
    fun join() {
        val m1 = Multiaddr("/ip4/127.0.0.1/udp/1234")
        val m2 = Multiaddr("/ip4/127.0.0.1/tcp/1234")
        assertEquals("/ip4/127.0.0.1/udp/1234/ip4/127.0.0.1/tcp/1234", Multiaddr.join(m1, m2).toString())
    }
}