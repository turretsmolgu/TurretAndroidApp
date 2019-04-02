package com.dev.turret.knp.turretbtapp

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.view.WindowManager
import android.view.Gravity



class DialogMaster(var context: Context) {

    fun showListStringDialog(title: String, list: MutableList<String>){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)

        val  lv = ListView(context)
        lv.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        lv.adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list)

        builder.setView(lv)
        builder.setCancelable(true)

        builder.create().show()
    }

    fun showNotificationDialog(time: Int){
        var dialog: AlertDialog? = null

        val builder = AlertDialog.Builder(context)
        var dialogView = LayoutInflater.from(context).inflate(R.layout.notification_dialog_layout, null)
        builder.setView(dialogView)
        dialog = builder.create()
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val wlp = dialog.window.getAttributes()

        wlp.gravity = Gravity.TOP
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        dialog.window.attributes = wlp
        dialog.show()

    }
}