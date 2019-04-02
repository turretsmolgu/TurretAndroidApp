package com.dev.turret.knp.turretbtapp

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.hardware.Camera
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.dev.turret.knp.turretbtapp.bt.BTMaster
import java.io.ByteArrayOutputStream
import java.net.URI
import java.util.*
import kotlin.math.roundToInt


class CameraModeActivity : AppCompatActivity(), BTMaster.BTMasterCallBack, TurretSocket.TurretServerCallBack,
    Camera.PreviewCallback {
    override fun onPreviewFrame(p0: ByteArray?, p1: Camera?) {
        bdata = p0
    }
    var bdata: ByteArray? = null
    val handler = Handler()
    val timer = Timer()
    var isStreaming = false

    override fun onConnect() {
        val cdelay = App.delay
//        makeToast("CONNECTED")
//        fabServerConnection.setBackgroundResource(R.color.green)
        fabServerConnection.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
//        fabStartStream.setBackgroundResource(R.color.green)
        fabStartStream.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
        isStreaming = true
        timer.schedule(object : TimerTask(){
            override fun run() {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                turretSocket.send(bdata)
//                val bmp = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888)
//                val bmData = renderScriptNV21ToRGBA888(
//                    this@CameraModeActivity,
//                    600,
//                    600,
//                    bdata!!
//                )
//                bmData.copyTo(bmp)
//
//                val stream = ByteArrayOutputStream()
//                bmp.compress(Bitmap.CompressFormat.PNG, 80, stream)
//                val byteArray = stream.toByteArray()
//                bmp.recycle()
//                turretSocket.send(byteArray)

                val yuvimage = YuvImage(bdata, ImageFormat.NV21, preview.w, preview.h, null)
                val baos = ByteArrayOutputStream()
                yuvimage.compressToJpeg(Rect(0, 0, preview.w, preview.h), 100, baos)

//                runOnUiThread {
//                    camera?.takePicture(null, null,
//                        Camera.PictureCallback { p0, p1 -> turretSocket.send(p0) })
//                }


                turretSocket.send(baos.toByteArray())
            }
        },0, cdelay)
    }

    override fun onText(text: String?) {
//        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        Log.i("ANGLE",text)
        var a = text?.toDouble()
        btMaster.turnDevice(a!!.roundToInt(), 1)
    }

    override fun onBin(data: ByteArray?) {

    }

    override fun onDisconnect() {
        makeToast("DISCONNECTED")
    }

    override fun onBTError(e: Exception) {
        makeToast(e.toString())
    }

    var camera: Camera? = null
//    var address = "192.168.43.96"
    var address = "192.168.2.51"
    //val turretSocket = TurretSocket(URI.create("192.168.43.96"), this)
    val turretSocket = TurretSocket(URI("ws://${App.ipAddress}/camera/"), this)


    //Views
    lateinit var fabBluetooth: FloatingActionButton
    lateinit var fabServerConnection: FloatingActionButton
    lateinit var fabStartStream: FloatingActionButton
    lateinit var preview: Preview


    //Other
    lateinit var btMaster: BTMaster
    lateinit var dialogMaster: DialogMaster

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()

        supportFragmentManager.beginTransaction().replace(R.id.prefFrame, SettingsFragment()).commit()

        dialogMaster = DialogMaster(this)

        btMaster = BTMaster(this, this)

//        supportActionBar?.title = btMaster.getMyBTParams()
    }

    private fun initViews(){
        fabBluetooth = findViewById(R.id.fabBluetooth)
        fabServerConnection = findViewById(R.id.fabServerConnection)
        fabServerConnection.setOnClickListener {
            if (!isStreaming) turretSocket.connect()
            else {
                turretSocket.close()
//                fabStartStream.setBackgroundResource(R.color.red)
                fabStartStream.backgroundTintList = ColorStateList.valueOf(Color.RED)
            }
        }
        fabStartStream = findViewById(R.id.fabStartStream)
        fabStartStream.setOnClickListener {
            btMaster.connectToDevice()
            checkBtConnected()
        }
//        llButtons = findViewById(R.id.ll_rl_buttons)
//        btnLeft = findViewById(R.id.btnLeft)
//        btnLeft.setOnClickListener {
//            if(btMaster.isDeviceReady())
//                btMaster.sendBTData(123)
//            else makeToast("Not connected")
//        }
//        btnRight = findViewById(R.id.btnRight)
//        btnRight.setOnClickListener {
//            if(btMaster.isDeviceReady())
//                btMaster.sendBTData(125)
//            else makeToast("Not connected")
//        }
//        seekBar = findViewById(R.id.seekBar)

        preview = Preview(this, findViewById(R.id.cameraView))
    }

    private fun makeToast(s: String){
        Toast.makeText(this, s,Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        camera = Camera.open()
        camera?.setPreviewCallback(this)
        //camera?.startPreview()
        preview.setCamera(camera)
    }

    var cbc: Runnable? = null

    fun checkBtConnected(){
        cbc = Runnable {
            if (btMaster.isDeviceReady()) {
                //TODO check internet
                turretSocket.connect()
//                fabBluetooth.setBackgroundResource(R.color.green)
                fabBluetooth.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
            } else{
                handler.postDelayed(cbc, 200)
            }
        }
        runOnUiThread(cbc)
    }


}
