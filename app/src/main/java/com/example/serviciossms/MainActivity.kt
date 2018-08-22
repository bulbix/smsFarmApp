package com.example.serviciossms

import android.Manifest
import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.example.serviciossms.service.MyServiceBackground


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "AppCompatActivity"
    private val RECORD_REQUEST_CODE = 101

    @BindView(R.id.start_background_service_button)
    lateinit var buttonInicio: Button

    @BindView(R.id.stop_background_service_button)
    lateinit var buttonCerrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        buttonInicio?.setOnClickListener(this)
        buttonCerrar?.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.start_background_service_button -> {
                val permission = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.SEND_SMS)

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission to record denied")
                    makeRequest()
                }else{
                    val starServiceIntent = Intent(this, MyServiceBackground::class.java)
                    startService(starServiceIntent)
                }

            }
            R.id.stop_background_service_button -> {
                Toast.makeText(this, "Termina servicio", Toast.LENGTH_SHORT).show()
                val myService = Intent(this@MainActivity, MyServiceBackground::class.java)
                stopService(myService)
            }

        }
    }


    private fun makeRequest() {
      return  ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.SEND_SMS),
                RECORD_REQUEST_CODE)
    }



}
