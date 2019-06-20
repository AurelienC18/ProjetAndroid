package com.example.myapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    var lightState = true
    var cameraManager: CameraManager? = null
    var camera: Camera? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, permissions,0)
        }

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            val cameraList = cameraManager!!.cameraIdList
            for (str in cameraList)
                println(str)
        } catch (e: Exception) {
            Log.e("error", e.message)
        }
    }

    fun switchLight(view: View) {
        lightState = !lightState
        goSwitch()
    }

    private fun goSwitch() {
        if (lightState) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    cameraManager!!.setTorchMode("0", false)
                } catch (e: Exception) {
                    Log.e("error", e.message)
                }
            } else {
                if (camera != null) {
                    camera!!.stopPreview()
                    camera!!.release()
                    camera = null
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    cameraManager!!.setTorchMode("0", true)
                } catch (e: Exception) {
                    Log.e("error", e.message)
                }
            } else {
                val pm = packageManager
                val features = pm.systemAvailableFeatures

                for(f in features) {
                    if(PackageManager.FEATURE_CAMERA_FLASH==f.name) {
                        if (null == camera){
                            camera = Camera.open()
                        }

                        val parameters = camera!!.parameters
                        parameters.flashMode = Camera.Parameters.FLASH_MODE_TORCH
                        camera!!.parameters = parameters
                        camera!!.startPreview()

                    }
                }
            }
        }
    }

    fun toRecorder(view: View) {
        val intent = Intent(this, VoiceRecorder::class.java)
        // start your next activity
        startActivity(intent)
    }

    fun toCompass(view: View) {
        val intent = Intent(this, CompassActivity::class.java)
        startActivity(intent)
    }

    fun toMap(view: View) {
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
    }

    fun toInfo(view: View) {
        val intent = Intent(this, InfoActivity::class.java)
        startActivity(intent)
    }
}
