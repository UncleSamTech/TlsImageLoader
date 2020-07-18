package com.gitapp.tlsimageloader

import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

class Tls12SocketFactory (
    private val internalSSLSocketFactory: SSLSocketFactory) :
    SSLSocketFactory() {
    override fun getDefaultCipherSuites(): Array<String> {
        return internalSSLSocketFactory.defaultCipherSuites
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return internalSSLSocketFactory.supportedCipherSuites
    }

    @Throws(IOException::class)
    override fun createSocket(
        s: Socket,
        host: String,
        port: Int,
        autoClose: Boolean
    ): Socket {
        return enableTLSOnSocket(
            internalSSLSocketFactory.createSocket(
                s,
                host,
                port,
                autoClose
            )
        )
    }

    @Throws(IOException::class)
    override fun createSocket(host: String, port: Int): Socket {
        return enableTLSOnSocket(
            internalSSLSocketFactory.createSocket(
                host,
                port
            )
        )
    }

    @Throws(IOException::class)
    override fun createSocket(
        host: String,
        port: Int,
        localHost: InetAddress,
        localPort: Int
    ): Socket {
        return enableTLSOnSocket(
            internalSSLSocketFactory.createSocket(
                host,
                port,
                localHost,
                localPort
            )
        )
    }

    @Throws(IOException::class)
    override fun createSocket(host: InetAddress, port: Int): Socket {
        return enableTLSOnSocket(
            internalSSLSocketFactory.createSocket(
                host,
                port
            )
        )
    }

    @Throws(IOException::class)
    override fun createSocket(
        address: InetAddress,
        port: Int,
        localAddress: InetAddress,
        localPort: Int
    ): Socket {
        return enableTLSOnSocket(
            internalSSLSocketFactory.createSocket(
                address,
                port,
                localAddress,
                localPort
            )
        )
    }

    companion object {
        /*
     * Utility methods
     */
        private fun enableTLSOnSocket(socket: Socket): Socket {
            if (socket != null && socket is SSLSocket
                && isTLSServerEnabled(socket)
            ) { // skip the fix if server doesn't provide there TLS version
                socket.enabledProtocols = arrayOf(
                    "TLSv1.2",
                    "TLSv1.1"
                )
            }
            return socket
        }

        private fun isTLSServerEnabled(sslSocket: SSLSocket): Boolean {
            println("__prova__ :: " + sslSocket.supportedProtocols.toString())
            for (protocol in sslSocket.supportedProtocols) {
                if (protocol == "TLSv1.2" || protocol == "TLSv1.1") {
                    return true
                }
            }
            return false
        }
    }




    }