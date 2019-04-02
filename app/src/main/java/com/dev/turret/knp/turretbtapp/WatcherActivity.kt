package com.dev.turret.knp.turretbtapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import java.net.URI


class WatcherActivity : AppCompatActivity(), TurretSocket.TurretServerCallBack {
    override fun onConnect() {
        Toast.makeText(this, "CONNECTED", Toast.LENGTH_SHORT).show()
    }

    override fun onText(text: String?) {

    }

    override fun onBin(data: ByteArray?) {
        updateImage(data)
    }

    override fun onDisconnect() {
        Toast.makeText(this, "DISC", Toast.LENGTH_SHORT).show()
    }

    lateinit var fabPlay: FloatingActionButton
    lateinit var ivWatcher: ImageView

    var isStreamin = false

    val turretSocket = TurretSocket(URI("ws://${App.ipAddress}/ws/"), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watcher)

        initViews()
    }

    private fun initViews(){
        fabPlay = findViewById(R.id.fabPlay)

        fabPlay.setOnClickListener {
            if(isStreamin)
                turretSocket.close()
            else turretSocket.connect()
        }

        ivWatcher = findViewById(R.id.ivWatcher)

        ivWatcher.setOnClickListener {
            if(fabPlay.visibility == View.VISIBLE)
                fabPlay.hide()
            else fabPlay.show()
        }
    }

    private fun updateImage(data: ByteArray?) {
        val options = BitmapFactory.Options()
        options.inMutable = true
        val bmp = BitmapFactory.decodeByteArray(data, 0, data!!.size, options)
        if (bmp != null) {
            //Log.i(TAG, "run: null bmp");
            ivWatcher.setImageBitmap(bmp)
            //                                        bmp.setWidth(300);
            //                                        bmp.setHeight(300);
            //                                        iv.setImageBitmap(Bitmap.createScaledBitmap(bmp, 300,
            //                                                300, false));
        }
    }
}
