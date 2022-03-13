package com.windapp.androidserver

import android.content.Intent
import android.media.metrics.TrackChangeEvent
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.InetAddress
import java.net.NetworkInterface
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView=findViewById<TextView>(R.id.tv)
        textView.setText(getIPAddress(true))

        var btnSend=findViewById<Button>(R.id.btnsend)
        btnSend.setOnClickListener {
        TcpServerService.tcpClientHandler.write()

        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(applicationContext, TcpServerService::class.java))
        } else {
            startService(Intent(applicationContext, TcpServerService::class.java))
        }
    }


    private fun getLocalIpAddress():String{
        val wifiManager=applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        wifiManager?.let {
            val wifiInfo=wifiManager.connectionInfo
            val ipInt=wifiInfo.ipAddress
            return  InetAddress.getByAddress(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array()).hostAddress
        }
    }

    fun getIPAddress(useIPv4: Boolean): String? {
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs: List<InetAddress> = Collections.list(intf.getInetAddresses())
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        val isIPv4 = sAddr.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%')
                                return if (delim < 0) sAddr.toUpperCase() else sAddr.substring(
                                    0,
                                    delim
                                ).toUpperCase()
                            }
                        }
                    }
                }
            }
        } catch (ignored: Exception) {
        }
        return ""
    }

}