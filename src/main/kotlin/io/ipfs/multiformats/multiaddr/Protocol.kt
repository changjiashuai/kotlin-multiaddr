package io.ipfs.multiformats.multiaddr

/**
 * changjiashuai@gmail.com.
 *
 * Created by CJS on 2018/7/15.
 */

const val LENGTH_PREFIXED = -1 //a size of -1 indicates a length-prefixed variable size

enum class Protocol(val code: Int, val size: Int, val named: String) {
    IP4(4, 32, "ip4"),
    TCP(6, 16, "tcp"),
    UDP(17, 16, "udp"),
    DCCP(33, 16, "dccp"),
    IP6(41, 128, "ip6"),
    DNS(53, LENGTH_PREFIXED, "dns"),
    DNS4(54, LENGTH_PREFIXED, "dns4"),
    DNS6(55, LENGTH_PREFIXED, "	dns6"),
    DNSADDR(56, LENGTH_PREFIXED, "dnsaddr"),
    SCTP(132, 16, "sctp"),
    UDT(301, 0, "udt"),
    UTP(302, 0, "utp"),
    UNIX(400, LENGTH_PREFIXED, "	unix"),
    P2P(421, LENGTH_PREFIXED, "p2p"),
    IPFS(421, LENGTH_PREFIXED, "ipfs"),
    ONION(444, 96, "onion"),
    QUIC(460, 0, "quic"),
    HTTP(480, 0, "http"),
    HTTPS(443, 0, "https"),
    WS(477, 0, "ws"),
    WSS(478, 0, "wss"),
    P2P_WEBSOCKET_STAR(479, 0, "p2p-websocket-star"),
    P2P_WEBRTC_STAR(275, 0, "p2p-webrtc-star"),
    P2P_WEBRTC_DIRECT(276, 0, "p2p-webrtc-direct"),
    P2P_CIRCUIT(290, 0, "p2p-circuit");

    companion object {

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