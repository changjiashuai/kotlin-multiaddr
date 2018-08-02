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

/**
 * @return ArrayList<ByteArray> ByteArray:code+value?
 */
fun bytesSplit(bytes: ByteArray): ArrayList<ByteArray> {
    val ret = arrayListOf<ByteArray>()
    var b = bytes
    var length = 0
    while (b.isNotEmpty()) {
        println("bytesSplit b.size=${b.size}")
        println("bytesSplit b=${b.contentToString()}")
        val result = Protocol.varintToCode(b)
        val code = result.first.toInt()
        val number = result.second / 7
        val protocol = Protocol.protocolWithCode(code)
        println("bytesSplit code=$code")
        println("bytesSplit protocol=$protocol")
        if (protocol != null) {
            if (protocol.code == 0) {
                throw IllegalStateException("no protocol with code $code")
            }
            if (protocol.size == 0) {
                // just only code no value
                ret.add(b.sliceArray(IntRange(0, number - 1)))
                b = b.sliceArray(IntRange(number, b.size - 1))
                continue
            }
            println("bytesSplit b.size remove code after=${b.size}")
            //addr value
            val result2 = sizeForAddr(protocol, b.sliceArray(IntRange(number, b.size - 1)))
            val skip = result2.first / 7
            val size = result2.second

            length = number + skip + size

            println("bytesSplit skip=$skip, size=$size")
            println("bytesSplit length=$length")
            println("bytesSplit b.size=${b.size}")
            val codeValue = b.sliceArray(IntRange(0, length - 1))
            ret.add(codeValue)
            //rest bytes
        }
        b = b.sliceArray(IntRange(length, b.size - 1))
    }
    println("bytesSplit end b=${b.contentToString()}")
    println("-----------------------------------")
    return ret
}

//name/value
fun bytesToString(bytes: ByteArray): String {
    var s = ""
    var b = bytes
    var length = 0
    println("bytesToString origin b=${b.contentToString()}")
    while (b.isNotEmpty()) {
        val result = Protocol.varintToCode(b)
        val code = result.first.toInt()
        val number = result.second / 7
        b = b.sliceArray(IntRange(number, b.size - 1))
        println("bytesToString remove code b=${b.contentToString()}")
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
            val skip = result2.first / 7
            val size = result2.second
            println("bytesToString value size=$size, skip=$skip")

//            length = number + skip + size

            println("bytesToString remove value b.size=${b.size}, addr=${b.contentToString()}")
            if (b.size < size || size < 0) {
                throw IllegalStateException("invalid value for size")
            }
            protocol.transcoder?.let {
                //addr bytes
                if (protocol.size == LENGTH_PREFIXED) {
                    //remove addr code
                    b = b.sliceArray(IntRange(skip, b.size - 1))
                }
                var str = it.bytesToString(b.sliceArray(IntRange(0, size - 1)))
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
    println("bytesToString--------------------------------")
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