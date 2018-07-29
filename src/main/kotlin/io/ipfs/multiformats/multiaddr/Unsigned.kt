package io.ipfs.multiformats.multiaddr

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