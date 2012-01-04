package com.github.tdudziak.gps_lock_lock;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LockService extends Service implements LocationListener {

	private boolean mIsActive = false;
	private Notification mNotification;

	private final String TAG = "LockService";
	private final long MIN_TIME = 20000;
	private final int NOTIFICATION_ID = 1;

	private void showNotification() {
        int icon = android.R.drawable.stat_notify_sync_noanim; // FIXME
        Context context = getApplicationContext();
        Intent notificationIntent = new Intent(this, SettingsActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        CharSequence title = getText(R.string.notification_title);
        CharSequence text = getText(R.string.notification_text);

        mNotification = new Notification(icon, title, System.currentTimeMillis());
        mNotification.setLatestEventInfo(context, title, text, contentIntent);

        startForeground(NOTIFICATION_ID, mNotification);
	}

	private void startListening() {
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, 0, this);
	}

	@Override
	public void onDestroy() {
		stopForeground(true);
		Log.i(TAG, "Shutting down");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO: Not sure if synchronization really required; check.
		synchronized (this) {
			if (mIsActive)
				return START_STICKY;
			mIsActive = true;
		}

		showNotification();
		startListening();
		Log.i(TAG, "Service started");

		return START_STICKY;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
        Log.v(TAG, "onLocationChanged()");
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
        Log.v(TAG, "onProviderDisabled()");
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
        Log.v(TAG, "onProviderEnabled()");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
        Log.v(TAG, "onStatusChanged(); status=" + status);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null; // no support for binding
	}
}
