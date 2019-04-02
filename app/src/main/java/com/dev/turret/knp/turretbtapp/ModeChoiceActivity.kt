package com.dev.turret.knp.turretbtapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton

class ModeChoiceActivity : AppCompatActivity() {

    lateinit var fabCamera: FloatingActionButton
    lateinit var fabWatcher: FloatingActionButton
    lateinit var fabHandControl: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode_choice)

        initView()
    }

    private fun initView(){
        fabCamera = findViewById(R.id.fabCameraMode)
        fabWatcher = findViewById(R.id.fabWatcherMode)
        fabHandControl = findViewById(R.id.fabHandsMode)

        fabCamera.setOnClickListener {
            startActivity(Intent(this, CameraModeActivity::class.java))
        }
        fabWatcher.setOnClickListener {
            startActivity(Intent(this, WatcherActivity::class.java))
        }
        fabHandControl.setOnClickListener {
            startActivity(Intent(this, HandModeActivity::class.java))
        }
    }
}
