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
}