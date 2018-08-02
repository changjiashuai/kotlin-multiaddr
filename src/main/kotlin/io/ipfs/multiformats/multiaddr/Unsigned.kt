package io.ipfs.multiformats.multiaddr

import java.nio.ByteBuffer


/**
 * changjiashuai@gmail.com.
 *
 * Created by CJS on 2018/7/28.
 */
fun Byte.toUByte(): Int {
    return this.toInt().and(0xFF)
}

fun Short.toUShort(): Int {
    return this.toInt().and(0xFFFF)
}

fun Int.toULong(): Long {
    return this.toLong().and(0xFFFFFFFF)
}

fun uint8(b: Byte): Int {
    return b.toInt() and 0xff
}

fun uint16(s: Short): Int {
    return s.toInt() and 0xffff
}

fun uint16(hi: Byte, lo: Byte): Int {
    return hi.toInt() and 0xff shl 8 or (lo.toInt() and 0xff)
}

fun uint32(i: Int): Long {
    return i.toLong() and 0xffffffffL
}

fun bytesToBEInt(bytes: ByteArray): Int {
    return ((uint8(bytes[0]) shl 24)
            + (uint8(bytes[1]) shl 16)
            + (uint8(bytes[2]) shl 8)
            + uint8(bytes[3]))
}

fun bytesToLEInt(bytes: ByteArray): Int {
    return Integer.reverseBytes(bytesToBEInt(bytes))
}

fun getUint8(buffer: ByteBuffer, position: Int): Int {
    return uint8(buffer.get(position))
}

fun getUint16(buffer: ByteBuffer, position: Int): Int {
    return uint16(buffer.getShort(position))
}

fun getUint32(buffer: ByteBuffer, position: Int): Long {
    return uint32(buffer.getInt(position))
}

fun put(buffer: ByteBuffer, position: Int, bytes: ByteArray) {
    val original = buffer.position()
    buffer.position(position)
    buffer.put(bytes)
    buffer.position(original)
}

fun isBitSet(flags: Long, bitIndex: Int): Boolean {
    return flags and bitAt(bitIndex) != 0L
}

fun bitAt(bitIndex: Int): Long {
    return 1L shl bitIndex
}

fun unpackBits(value: Long): IntArray {
    var _value = value
    val size = java.lang.Long.bitCount(_value)
    val result = IntArray(size)
    var index = 0
    var bitPos = 0
    while (_value > 0) {
        if (_value and 1 == 1L) result[index++] = bitPos
        _value = _value shr 1
        bitPos++
    }
    return result
}

fun packBits(bits: IntArray): Long {
    var packed: Long = 0
    for (b in bits) {
        packed = packed or (1 shl b).toLong()
    }
    return packed
}