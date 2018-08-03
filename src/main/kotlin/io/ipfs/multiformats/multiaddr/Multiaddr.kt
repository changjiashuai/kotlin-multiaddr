package io.ipfs.multiformats.multiaddr

import java.util.*

/**
 * changjiashuai@gmail.com.
 *
 * Created by CJS on 2018/7/15.
 */
class Multiaddr {

    private var bytes: ByteArray

    /**
     * parses and validates an input string, returning a Multiaddr
     */
    constructor(address: String) {
        this.bytes = stringToBytes(address)
    }

    /**
     * initializes a Multiaddr from a byte representation.
     * It validates it as an input string.
     */
    constructor(bytes: ByteArray) {
        if (isValidBytes(bytes)) {
            this.bytes = bytes
        } else {
            throw IllegalArgumentException("invalid bytes for multiaddr")
        }
        this.bytes = bytes
    }

    /**
     * Protocols returns the list of Protocols this Multiaddr includes
     */
    fun getProtocols(): ArrayList<Protocol> {
        return bytesSplitForProtocol(bytes)
    }

    /**
     * /ip4/1.2.3.4 encapsulate /tcp/80 = /ip4/1.2.3.4/tcp/80
     */
    fun encapsulate(multiaddr: Multiaddr): Multiaddr {
        return Multiaddr(bytes.plus(multiaddr.bytes))
    }

    /**
     * /ip4/1.2.3.4/tcp/80 decapsulate /ip4/1.2.3.4 = /tcp/80
     */
    fun decapsulate(multiaddr: Multiaddr): Multiaddr {
        val s1 = toString()
        val s2 = multiaddr.toString()
        val i = s1.lastIndexOf(s2)
        if (i < 0) {
            // if multiaddr not contained, return a copy.
            return Multiaddr(multiaddr.bytes)
        }
        return Multiaddr(s1.substring(0, i) + s1.substring(i + s2.length))
    }

    /**
     * Bytes returns the ByteArray representation of this Multiaddr
     */
    fun toBytes(): ByteArray {
        // consider returning copy to prevent changing underneath us?
        return bytes.copyOf()
    }

    /**
     * valueForProtocol returns the value (if any) following the specified protocol
     */
    fun valueForProtocol(code: Int): String {
        for (md in split()) {
            //
            val c = Protocol.varintToCode(md.bytes)
            val p = Protocol.protocolWithCode(c.first.toInt())
            if (p != null) {
                if (p.code == code) {
                    if (p.size == 0) {
                        return ""
                    }
                    if (p.path) {
                        return md.toString().trim('/').substringAfter('/')
                    }
                    return md.toString().trim('/').split('/')[1]
                }
            }
        }
        throw IllegalStateException("protocol not found in multiaddr")
    }

    fun split(): ArrayList<Multiaddr> {
        val bsList = bytesSplit(bytes)
        val mds = arrayListOf<Multiaddr>()
        bsList.forEach {
            mds.add(Multiaddr(it))
        }
        return mds
    }

    /**
     * String returns the string representation of this Multiaddr
     */
    override fun toString(): String {
        return bytesToString(bytes)
    }

    /**
     * Equal returns whether two Multiaddrs are exactly equal
     */
    override fun equals(other: Any?): Boolean {
        return Arrays.equals(bytes, (other as Multiaddr).bytes)
    }

    companion object {

        fun join(vararg mds: Multiaddr): Multiaddr {
            var s = ""
            for (md in mds) {
                s += md.toString()
            }
            return Multiaddr(s)
        }

    }
}