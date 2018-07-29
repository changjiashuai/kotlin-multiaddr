package io.ipfs.multiformats.multiaddr

import java.io.ByteArrayOutputStream

/**
 * changjiashuai@gmail.com.
 *
 * Created by CJS on 2018/7/16.
 */

/**
 * @param protocol
 * @param bytes
 * @return <skip, size>
 */
fun sizeForAddr(protocol: Protocol, bytes: ByteArray): Pair<Int, Int> {
    return when {
        protocol.size > 0 -> Pair(0, protocol.size / 8)
        protocol.size == 0 -> Pair(0, 0)
        else -> {
            val result = Protocol.varintToCode(bytes) //<code,number>
            Pair(result.second, result.first.toInt())
        }
    }
}

fun bytesSplit(bytes: ByteArray): ArrayList<ByteArray> {
    val ret = arrayListOf<ByteArray>()
    var b = bytes
    while (b.isNotEmpty()) {
        val result = Protocol.varintToCode(bytes)
        val code = result.first.toInt()
        val number = result.second
        val protocol = Protocol.protocolWithCode(code)
        protocol?.let {
            if (it.code == 0) {
                throw IllegalStateException("no protocol with code ${bytes[0]}")
            }
            val result2 = sizeForAddr(it, b.sliceArray(IntRange(number, b.size - 1)))
            val skip = result2.first
            val size = result2.second
            val length = number + skip + size
            ret.add(b.sliceArray(IntRange(0, length)))
            b = b.sliceArray(IntRange(length, b.size - 1))
        }
    }
    return ret
}

//name/value
fun bytesToString(bytes: ByteArray): String {
    var s = ""
    var b = bytes
    while (b.isNotEmpty()) {
        val result = Protocol.varintToCode(b)
        val code = result.first.toInt()
        val number = result.second
        b = b.sliceArray(IntRange(1, b.size - 1))
        val protocol = Protocol.protocolWithCode(code)
        if (protocol != null) {
            if (protocol.code == 0) {
                throw IllegalStateException("no protocol with code $code")
            }
            //name
            s = s + "/" + protocol.named
            if (protocol.size == 0) {
                continue
            }
            //addr value
            val result2 = sizeForAddr(protocol, b)
            val skip = result2.first
            val size = result2.second
            b = b.sliceArray(IntRange(skip, b.size - 1))
            if (b.size < size || size < 0) {
                throw IllegalStateException("invalid value for size")
            }
            protocol.transcoder?.let {
                var str = it.bytesToString( b.sliceArray(IntRange(0, size - 1)))
                if (protocol.path && str.isNotEmpty() && str[0] == '/') {
                    str = str.substring(1)
                }
                if (str.isNotEmpty()) {
                    s = "$s/$str"
                }
            }
            b = b.sliceArray(IntRange(size, b.size - 1))
        }
    }
    return s
}

fun stringToBytes(str: String): ByteArray {
    if (!str.startsWith("/")) {
        throw IllegalArgumentException("invalid multiaddr, must begin with /")
    }
    val s = str.trimEnd('/').trimStart('/')
    val sp = s.split("/")
    val baos = ByteArrayOutputStream()
    var i = 0
    while (sp.size != i) {
        val name = sp[i]
        val protocol = Protocol.protocolWithName(name)
        if (protocol == null) {
            throw IllegalStateException("no protocol with name $name")
        } else {
            val code = protocol.code
            baos.write(Protocol.codeToVarint(code))
            val size = protocol.size
            if (size == 0) {
                i += 1
                continue
            }
            if (i + 1 == sp.size) {
                throw IllegalStateException("protocol requires address, none given: $name")
            }
            var value = sp[i + 1]
            if (value.isEmpty()) {
                throw IllegalStateException("protocol requires address, none given: $name")
            }
            if (protocol.path) {
                // it's a path protocol (terminal).
                // consume the rest of the address as the next component.
                value = sp.subList(i, sp.size).joinToString("/").removePrefix("unix")
                i = sp.size
            } else {
                i += 2
            }
            protocol.transcoder?.let {
                val b = it.stringToBytes(value)
                if (protocol.size < 0) { // varint size
                    baos.write(Protocol.codeToVarint(b.size))
                }
                baos.write(b)
            }
        }
    }
    return baos.toByteArray()
}

fun isValidBytes(buf: ByteArray): Boolean {
    var b = buf
    while (b.isNotEmpty()) {
        val result = Protocol.varintToCode(b)
        val code = result.first.toInt()
        val number = result.second
        b = b.sliceArray(IntRange(1, b.size - 1))
        val protocol = Protocol.protocolWithCode(code)
        if (protocol != null) {
            if (protocol.code == 0) {
                throw IllegalStateException("no protocol with code $code")
            }
            if (protocol.size == 0) {
                continue
            }
            val result2 = sizeForAddr(protocol, b)
            val skip = result2.first
            val size = result2.second
            b = b.sliceArray(IntRange(skip, b.size - 1))
            if (b.size < size || size < 0) {
                throw IllegalStateException("invalid value for size")
            }
            protocol.transcoder?.let {
                b = b.sliceArray(IntRange(0, size - 1))
                if (!it.isValidBytes(b)) {
                    throw IllegalStateException("invalid bytes for transcoder")
                }
            }
            b = b.sliceArray(IntRange(size, b.size - 1))
        }
    }
    return true
}