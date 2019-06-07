package com.example.myapp

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_lampetorche.*

class LampeTorche : AppCompatActivity() {

    var lightState = false
    var cameraManager: CameraManager? = null
    var mCameraId: String? = null
    var switch: Button? = null
    var camera: Camera? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lampetorche)
        val isAvailable = applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        switch = imageView3 as Button
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

        if (lightState) {
            lampStateLib.text = "On"
        } else {
            lampStateLib.text = "Off"
        }
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

    companion object {
        private var isOpen =false
        private val camera:android.hardware.Camera?=null
    }
}
