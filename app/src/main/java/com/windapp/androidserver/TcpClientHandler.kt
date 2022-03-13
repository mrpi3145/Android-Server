package com.windapp.androidserver

import android.util.Log
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

class TcpClientHandler(private val dataInputStream: DataInputStream,private val dataOutputStream: DataOutputStream):Thread() {

    fun write(){
    var runnable=    Runnable {
            try {
                dataOutputStream!!.writeUTF("hello")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        Thread(runnable).start()
    }


    override fun run() {
        while(true){
            try {


                    var buffer = ByteArray(1024)
                    var bytes: Int
                    bytes = dataInputStream!!.read(buffer)
                    if (bytes > 0) {
                        var finalBytes: Int = bytes

                        var tempMsg = String(buffer, 0, finalBytes)
                        Log.d(TAG, "recived" + tempMsg)
                        dataOutputStream.writeUTF("hello Client")



                }
            }

            catch (e:IOException){
                e.printStackTrace()
                try{
                    dataInputStream.close()
                    dataOutputStream.close()
                }
                catch (ex:IOException){
                    ex.printStackTrace()
                }

            }
            catch (e:InterruptedException){
                e.printStackTrace()
                try{
                    dataInputStream.close()
                    dataOutputStream.close()
                }
                catch (ex:IOException){
                    ex.printStackTrace()
                }
            }
        }
    }

    companion object{
        private val TAG=TcpClientHandler::class.java.simpleName
    }
}