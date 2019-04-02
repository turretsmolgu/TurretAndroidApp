package com.dev.turret.knp.turretbtapp

import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.params.OutputConfiguration
import android.os.Handler
import android.view.Surface
import android.view.TextureView

class CameraMaster(val textureView: TextureView) : TextureView.SurfaceTextureListener {
    override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture?, p1: Int, p2: Int) {

    }

    override fun onSurfaceTextureUpdated(p0: SurfaceTexture?) {

    }

    override fun onSurfaceTextureDestroyed(p0: SurfaceTexture?): Boolean {
        return false
    }

    override fun onSurfaceTextureAvailable(p0: SurfaceTexture?, p1: Int, p2: Int) {

    }

    val cameraCaptureSession = object : CameraCaptureSession.StateCallback(){
        override fun onConfigureFailed(p0: CameraCaptureSession) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onConfigured(p0: CameraCaptureSession) {
  //          TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

    var cam: CameraDevice? = null

    val textureListener = this
    val stateCallback = object : CameraDevice.StateCallback(){
        override fun onOpened(p0: CameraDevice) {
            cam = p0
            val prevBuilder = cam?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            var texture = textureView.surfaceTexture
            texture.setDefaultBufferSize(600,600)
            var surface = Surface(texture)
            prevBuilder?.addTarget(surface)
            cam?.createCaptureSession(listOf(surface),cameraCaptureSession, Handler())

        }

        override fun onDisconnected(p0: CameraDevice) {
            cam?.close()
        }

        override fun onError(p0: CameraDevice, p1: Int) {
            cam?.close()
        }

    }
}