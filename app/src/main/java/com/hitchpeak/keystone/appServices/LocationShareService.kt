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
import com.hitchpeak.keystone.BuildConfig
import com.hitchpeak.keystone.R
import com.hitchpeak.keystone.models.LocationShareModel
import com.hitchpeak.keystone.utils.HttpClient

class LocationShareService : Service() {

    protected lateinit var locationProvider: FusedLocationProviderClient

    val channelId = BuildConfig.NOTIF_CHANNEL_ID
    val notificationId = BuildConfig.LOCATION_NOTIF_ID

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startLocationSharing()
        startForegroundNotification()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundNotification() {
        val notification: Notification = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(getString(R.string.app_title))
                .setContentText(getString(R.string.notification_text))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

        startForeground(notificationId, notification)
    }

    private fun startLocationSharing() {
        // The gist of location sharing:
        locationProvider = LocationServices.getFusedLocationProviderClient(applicationContext)

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = BuildConfig.LOCATION_SHARE_INTERVAL

        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return

                val location = locationResult.lastLocation
                val locationModel = LocationShareModel(
                        location.latitude,
                        location.longitude,
                        location.time,
                        location.accuracy)

                HttpClient.execute(Runnable {
                    val res = HttpClient.postForObject(HttpClient.BASE_URL + HttpClient.POST, locationModel, String::class.java)
                })
            }
        }

        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationProvider.requestLocationUpdates(locationRequest, locationCallback, null)
        } else {
            // TODO("Handle case when permission is not granted")
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
