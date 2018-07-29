package io.ipfs.multiformats.multiaddr

import io.ipfs.multiformats.multihash.VarInt

/**
 * changjiashuai@gmail.com.
 *
 * Created by CJS on 2018/7/15.
 */

const val LENGTH_PREFIXED = -1 //a size of -1 indicates a length-prefixed variable size
val IPV4_TRANSCODER = IPv4Transcoder()
val PORT_TRANSCODER = PortTranscoder()
val IPV6_TRANSCODER = IPv6Transcoder()
val UNIX_TRANSCODER = UnixTranscoder()
val P2P_TRANSCODER = P2PTranscoder()
val ONION_TRANSCODER = OnionTranscoder()

/**
 * @param size a size of -1 indicates a length-prefixed variable size
 * @param path indicates a path protocol (eg: unix, http)
 */
enum class Protocol(val code: Int, val size: Int, val named: String, val path: Boolean, val transcoder: Transcoder? = null) {
    IP4(4, 32, "ip4", false, IPV4_TRANSCODER),
    TCP(6, 16, "tcp", false, PORT_TRANSCODER),
    UDP(17, 16, "udp", false, PORT_TRANSCODER),
    DCCP(33, 16, "dccp", false, PORT_TRANSCODER),
    IP6(41, 128, "ip6", false, IPV6_TRANSCODER),
    DNS(53, LENGTH_PREFIXED, "dns", false),
    DNS4(54, LENGTH_PREFIXED, "dns4", false),
    DNS6(55, LENGTH_PREFIXED, "dns6", false),
    DNSADDR(56, LENGTH_PREFIXED, "dnsaddr", false),
    SCTP(132, 16, "sctp", false, PORT_TRANSCODER),
    UDT(301, 0, "udt", false),
    UTP(302, 0, "utp", false),
    UNIX(400, LENGTH_PREFIXED, "unix", true, UNIX_TRANSCODER),
    P2P(421, LENGTH_PREFIXED, "p2p", false, P2P_TRANSCODER),
    IPFS(421, LENGTH_PREFIXED, "ipfs", false, P2P_TRANSCODER),
    ONION(444, 96, "onion", false, ONION_TRANSCODER),
    QUIC(460, 0, "quic", false),
    HTTP(480, 0, "http", false),
    HTTPS(443, 0, "https", false),
    WS(477, 0, "ws", false),
    WSS(478, 0, "wss", false),
    P2P_WEBSOCKET_STAR(479, 0, "p2p-websocket-star", false),
    P2P_WEBRTC_STAR(275, 0, "p2p-webrtc-star", false),
    P2P_WEBRTC_DIRECT(276, 0, "p2p-webrtc-direct", false),
    P2P_CIRCUIT(290, 0, "p2p-circuit", false);

    companion object {

        /**
         * @param buf varint-encoded ByteArray
         * @return converts a varint-encoded ByteArray to an integer protocol code
         *         and the number of bytes read.
         */
        fun varintToCode(buf: ByteArray): Pair<Long, Int> {
            return VarInt.decodeVarInt(buf)
        }

        /**
         * @param code func code
         * @return converts an integer to a varint-encoded ByteArray
         */
        fun codeToVarint(code: Int): ByteArray {
            return VarInt.encodeVarint(code)
        }

        fun protocolWithCode(code: Int): Protocol? {
            for (p in Protocol.values()) {
                if (p.code == code) {
                    return p
                }
            }
            return null
        }

        fun protocolWithName(named: String): Protocol? {
            for (p in Protocol.values()) {
                if (p.named == named) {
                    return p
                }
            }
            return null
        }

        fun protocolsWithString(address: String): ArrayList<Protocol> {
            val s = address.trim('/')
            val sp = s.split("/")
            if (sp.isEmpty()) {
                return arrayListOf()
            }
            val protocols = ArrayList<Protocol>(sp.size)
            sp.forEach { named ->
                val p = protocolWithName(named)
                p?.let { protocols.add(p) }
            }
            return protocols
        }
    }
}