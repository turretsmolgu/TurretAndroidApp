package com.dev.turret.knp.turretbtapp

import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.widget.EditText
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.Toast
import com.dev.turret.knp.turretbtapp.bt.BTMaster

class HandModeActivity : AppCompatActivity(), BTMaster.BTMasterCallBack, SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        btMaster.turnDevice(p1-80, 1)
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {

    }

    override fun onStopTrackingTouch(p0: SeekBar?) {

    }

    override fun onBTError(e: Exception) {
        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
    }

    lateinit var btnLeft: ImageButton
    lateinit var btnRight: ImageButton
    lateinit var etAngle: EditText
    lateinit var fabBluetooth: FloatingActionButton
    lateinit var sbTurn: SeekBar

    lateinit var btMaster: BTMaster

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hand_mode)
        initViews()
        btMaster = BTMaster(this,this)
    }

    private fun initViews(){
        fabBluetooth = findViewById(R.id.fabBluetooth)
        fabBluetooth.setOnClickListener {
            btMaster.connectToDevice()
            checkBtConnected()
        }
        btnLeft = findViewById(R.id.btnLeft)
        btnLeft.setOnClickListener {
            btMaster.turnDevice(etAngle.text.toString().toInt(), -1)
        }
        btnRight = findViewById(R.id.btnRight)
        btnRight.setOnClickListener {
            btMaster.turnDevice(etAngle.text.toString().toInt(), 1)
        }
        etAngle = findViewById(R.id.etAngle)


        sbTurn = findViewById(R.id.sbTurn)
        sbTurn.progress = 80
        sbTurn.setOnSeekBarChangeListener(this)
    }

    val handler = Handler()
    lateinit var cbc: Runnable

    private fun checkBtConnected(){
        cbc = Runnable {
            if (btMaster.isDeviceReady()) {
                fabBluetooth.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
            } else{
                handler.postDelayed(cbc, 100)
            }
        }
        runOnUiThread(cbc)
    }

    override fun onStart() {
        super.onStart()
        if (btMaster.isDeviceReady())
            fabBluetooth.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
    }
}
