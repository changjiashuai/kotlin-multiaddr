package io.ipfs.multiformats.multiaddr

/**
 * changjiashuai@gmail.com.
 *
 * Created by CJS on 2018/7/15.
 */
class Multiaddr {

    private var bytes: ByteArray

    constructor(address: String){
        this.bytes = address.toByteArray()
    }

    constructor(bytes: ByteArray) {
        this.bytes = bytes
    }

    /**
     * Protocols returns the list of Protocols this Multiaddr includes
     */
    fun getProtocols(): Array<Protocol> {
        TODO()
    }

    /**
     * /ip4/1.2.3.4 encapsulate /tcp/80 = /ip4/1.2.3.4/tcp/80
     */
    fun encapsulate(multiaddr: Multiaddr): Multiaddr {
        TODO()
    }

    /**
     * /ip4/1.2.3.4/tcp/80 decapsulate /ip4/1.2.3.4 = /tcp/80
     */
    fun decapsulate(multiaddr: Multiaddr): Multiaddr {
        TODO()
    }

    /**
     * Bytes returns the ByteArray representation of this Multiaddr
     */
    fun toBytes(): ByteArray {
        TODO()
    }

    /**
     * valueForProtocol returns the value (if any) following the specified protocol
     */
    fun valueForProtocol(code: Int): String {
        TODO()
    }

    /**
     * String returns the string representation of this Multiaddr
     */
    override fun toString(): String {
        return super.toString()
    }

    /**
     * Equal returns whether two Multiaddrs are exactly equal
     */
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    fun unsignedInt(value: Int): Long {
        return value.toLong() and 0xFFFFFFFF
    }

    fun unsignedShort(value: Short): Int {
        return value.toInt() and 0xFFFF
    }

    fun unsignedByte(value: Byte): Int {
        return value.toInt() and 0xFF
    }
}