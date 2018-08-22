package com.example.serviciossms.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

import java.util.concurrent.Executors

class MyServiceBackground : Service() {

    private val scheduler = Executors.newScheduledThreadPool(1)


    override fun onBind(intent: Intent): IBinder {
        throw  UnsupportedOperationException("Not yet implemented") as Throwable;
    }

    override fun onCreate() {

        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        envioAlert()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Invoke background service onDestroy method.", Toast.LENGTH_SHORT).show()
    }

    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
    }

    fun envioAlert() {
        Toast.makeText(this, "Inicio envio de mensajes.", Toast.LENGTH_SHORT).show()
         var envio: EnvioSMS
        envio = EnvioSMS()

    }

}





