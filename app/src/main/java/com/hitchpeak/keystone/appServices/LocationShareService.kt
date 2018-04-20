package com.hitchpeak.keystone.appServices

import android.Manifest
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.*
import com.hitchpeak.keystone.R

class LocationShareService : Service() {
    // TODO("Start this on different thread")

    protected lateinit var locationProvider: FusedLocationProviderClient

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startLocationSharing()
        startForegroundNotification()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundNotification() {
        // TODO("extract this variables somewhere")
        val channelId = "App"
        val notificationId = 1234
        val notification: Notification = NotificationCompat.Builder(this,channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Hitchpeak")
                .setContentText("We are watching you!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

        startForeground(notificationId, notification)
    }

    private fun startLocationSharing() {
        // The gist of location sharing:
        locationProvider = LocationServices.getFusedLocationProviderClient(applicationContext)

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5

        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                // TODO("Handle location sharing here.")
                println(locationResult)
            }
        }

        // TODO("Handle case when permission is not granted")
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
            locationProvider.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
