package com.hitchpeak.keystone.appServices

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.Looper
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.*
import com.hitchpeak.keystone.BuildConfig
import com.hitchpeak.keystone.R
import com.hitchpeak.keystone.models.LocationShareModel
import com.hitchpeak.keystone.utils.HttpClient

class LocationShareService : Service() {

    private lateinit var locationProvider: FusedLocationProviderClient

    val mChannelId = BuildConfig.NOTIF_CHANNEL_ID
    val mNotificationId = BuildConfig.LOCATION_NOTIF_ID

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startLocationSharing()
        startForegroundNotification()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundNotification() {
        val notification: Notification = NotificationCompat.Builder(this, mChannelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(getString(R.string.app_title))
                .setContentText(getString(R.string.notification_text))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

        startForeground(mNotificationId, notification)
    }

    private fun setupLocationRequest(): Pair<LocationRequest, LocationCallback> {
        val locationRequest = LocationRequest.create()
        val requestPriority = BuildConfig.LOCATION_REQUEST_PRIORITY

        locationRequest.priority = when(requestPriority) {
            "HIGH_ACCURACY" -> LocationRequest.PRIORITY_HIGH_ACCURACY
            "BALANCED_POWER_ACCURACY" -> LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            "LOW_POWER" -> LocationRequest.PRIORITY_LOW_POWER
            "NO_POWER" -> LocationRequest.PRIORITY_LOW_POWER
            else -> LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationRequest.interval = BuildConfig.LOCATION_SHARE_INTERVAL

        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return

                val location = locationResult.lastLocation
                val locationPost = LocationShareModel(
                        location.latitude,
                        location.longitude,
                        location.time,
                        location.accuracy)

                HttpClient.execute(Runnable {
                    HttpClient.postForObject(HttpClient.BASE_URL + HttpClient.LOCATION_SHARE_POST, locationPost, String::class.java)
                })
            }
        }

        return Pair(locationRequest, locationCallback)
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates(locationProvider: FusedLocationProviderClient, locationRequest:
    LocationRequest, locationCallback: LocationCallback, looper: Looper?) {
        locationProvider.requestLocationUpdates(locationRequest, locationCallback, looper)
    }

    private fun startLocationSharing() {
        // The gist of location sharing:
        locationProvider = LocationServices.getFusedLocationProviderClient(applicationContext)
        val (locationRequest, locationCallback) = setupLocationRequest()

        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates(locationProvider, locationRequest, locationCallback, null)
        } else {
            // TODO("Handle case when permission is not granted")
        }
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
