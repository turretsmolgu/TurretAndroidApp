package com.dev.turret.knp.turretbtapp

import android.app.Application
import android.content.SharedPreferences
import android.support.v7.preference.PreferenceManager

class App : Application() {

    companion object {
        @JvmStatic var ipAddress : String? = null
            private set
        @JvmStatic var btAddress : String? = null
            private set
        @JvmStatic var width = 320
            private set
        @JvmStatic var height = 240
            private set
        @JvmStatic var delay = 700L
            private set
    }

    lateinit var pref: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        getValuesFromPref()
        pref.registerOnSharedPreferenceChangeListener { sharedPreferences, s ->
            getValuesFromPref()
        }
    }

    private fun getValuesFromPref(){
        ipAddress = pref.getString(getString(R.string.ip_add_key), "")
        btAddress = pref.getString(getString(R.string.bt_add_key), "")
        width = pref.getString(getString(R.string.im_w), "320")!!.toInt()
        height = pref.getString(getString(R.string.im_h), "240")!!.toInt()
        delay = pref.getString(getString(R.string.send_delay), "700")!!.toLong()
    }
}