package com.dev.turret.knp.turretbtapp.bt

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context


class BTMaster (val cotext: Context, var btmcb: BTMasterCallBack) {

    val btAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    var clientSocket: BluetoothSocket? = null
    //var btSocket: BluetoothSocket

    interface BTMasterCallBack{
        fun onBTError(e: Exception)
    }

    fun getMyBTParams(): String{
        return "${btAdapter.name}: ${btAdapter.address} | ${btAdapter.state}"
    }

    fun getPairedDevices(): MutableList<String>{
        val list: MutableList<String> = ArrayList()
        for (bd in btAdapter.bondedDevices){
            list.add("${bd.name} : ${bd.address}")
            println("${bd.name} : ${bd.address}")
        }
        return list
    }

    fun sendBTData(n: Int){
        var os = clientSocket!!.outputStream
        os.write(n)
    }

    fun connectToDevice(){
        var device: BluetoothDevice = btAdapter.getRemoteDevice("20:15:11:23:15:72")
        val m = device.javaClass.getMethod(
            "createRfcommSocket", Int::class.javaPrimitiveType
        )
        clientSocket = m.invoke(device, 1) as BluetoothSocket

        clientSocket!!.connect()
    }

    fun isDeviceReady(): Boolean{
        return clientSocket != null && clientSocket!!.isConnected
    }

    fun turnDevice(angle: Int, dir: Int){
        if(!isDeviceReady()) return
        var a = angle
        a *= dir
        sendBTData(a)
    }

}