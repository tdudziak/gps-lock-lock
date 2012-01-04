package com.github.tdudziak.gps_lock_lock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LockService extends Service implements LocationListener {

	private final String TAG = "LockService";
	private final long GPS_MIN_TIME = 20000;
	private final long LOCK_LOCK_TIME = 15*60*60*1000;
	private final int NOTIFICATION_ID = 1;
	
	private boolean mIsActive = false;
	private Notification mNotification;
	private PendingIntent mNotificationIntent;
	private LocationManager mLocationManager;
	private NotificationManager mNotificationManager;

	private void setFixState(boolean has_fix) {
		assert mNotification != null;
		CharSequence title, text;
		
		if(has_fix) {
			title = getText(R.string.notification_title_available);
			text = getText(R.string.notification_text_available);
		} else {
			title = getText(R.string.notification_title_unavailable);
			text = getText(R.string.notification_text_unavailable);
		}
		
		mNotification.setLatestEventInfo(getApplicationContext(), title, text, mNotificationIntent);
		startForeground(NOTIFICATION_ID, mNotification);
	}
	
	private void showNotification() {
        int icon = android.R.drawable.stat_notify_sync_noanim; // FIXME
        CharSequence ticker = getText(R.string.notification_ticker);
        
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this, SettingsActivity.class);
        mNotificationIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        mNotification = new Notification(icon, ticker, System.currentTimeMillis());
        
        setFixState(false);
	}

	private void startListening() {
		mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_MIN_TIME, 0, this);
	}
	
	private void stopEverything() {
		Log.i(TAG, "Disabling lock-locking");
		mLocationManager.removeUpdates(this);
		stopForeground(true);
		mIsActive = false;
	}

	@Override
	public void onDestroy() {
		stopEverything();
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
        setFixState(true);
	}

	@Override
	public void onProviderDisabled(String provider) {
        // GPS explicitly turned off. The user obviously does not want a GPS fix.
        stopEverything();
	}

	@Override
	public void onProviderEnabled(String provider) {
		// This probably should never happen. If it does, ignore.
        Log.v(TAG, "onProviderEnabled()");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
        Log.v(TAG, "onStatusChanged(); status=" + status);
        
        if(status == LocationProvider.AVAILABLE) {
        	setFixState(true);
        } else {
        	setFixState(false);
        }
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null; // no support for binding
	}
}
