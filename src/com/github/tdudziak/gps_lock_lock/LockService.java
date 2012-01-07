/*
 *  This file is a part of GPS Lock-Lock Android application.
 *  Copyright (C) 2011 Tomasz Dudziak
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.tdudziak.gps_lock_lock;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class LockService extends Service implements LocationListener {

    private final String TAG = "LockService";
    private final long GPS_MIN_TIME = 0; // 20000; FIXME FIXME

    public final static String ACTION_SHUTDOWN = "com.github.tdudziak.gps_lock_lock.LockService.ACTION_SHUTDOWN";
    public final static String ACTION_UI_UPDATE = "com.github.tdudziak.gps_lock_lock.LockService.ACTION_UI_UPDATE";
    public static final String EXTRA_TIME_LEFT = "com.github.tdudziak.gps_lock_lock.LockService.EXTRA_TIME_LEFT";
    public static final String EXTRA_LAST_FIX = "com.github.tdudziak.gps_lock_lock.LockService.EXTRA_LAST_FIX";

    public static final long LOCK_LOCK_MINUTES = 5;

    private boolean mIsActive = false; // TODO: Get rid of this field.
    private long mStartTime;
    private long mLastFixTime = 0;
    private LocationManager mLocationManager;
    private RefreshHandler mHandler;
    private NotificationUi mNotificationUi;

    private class RefreshHandler extends Handler {
        private final int WHAT = 0;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(!mIsActive) return;

            // FIXME: Ugly duplicate code
            long minutes = (System.currentTimeMillis() - mStartTime)/(1000*60);
            long remaining = LOCK_LOCK_MINUTES - minutes;

            if(remaining <= 0) {
                stopSelf();
                return;
            }

            broadcastMessage(false);

            // Schedule another update.
            removeMessages(WHAT);
            sendEmptyMessageDelayed(WHAT, 1000); // FIXME: the delay is way too small
        }

        public void broadcastMessage(boolean last_message) {
            long minutes = (System.currentTimeMillis() - mStartTime)/(1000*60);
            long remaining = LOCK_LOCK_MINUTES - minutes;
            long last_fix = (System.currentTimeMillis() - mLastFixTime)/(1000*60);

            if(mLastFixTime == 0) last_fix = -1; // special value meaning "never"
            if(remaining <= 0 || last_message) remaining = 0; // special value meaning "no time left"

            Intent intent = new Intent(ACTION_UI_UPDATE);
            intent.putExtra(EXTRA_TIME_LEFT, (int) remaining);
            intent.putExtra(EXTRA_LAST_FIX, (int) last_fix);

            LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(LockService.this);
            lbm.sendBroadcast(intent);
        }

        public void broadcastNow() {
            removeMessages(WHAT);
            sendEmptyMessage(WHAT);
        }
    }

    @Override
    public void onDestroy() {
        mHandler.broadcastMessage(true);
        mLocationManager.removeUpdates(this);
        stopForeground(true);
        mIsActive = false;
        mNotificationUi.disable();
        Log.i(TAG, "Shutting down");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // FIXME: Is using startService() to stop a service really in the spirit
        // of this API?
        if(ACTION_SHUTDOWN.equals(intent.getAction())) {
            if(mIsActive) stopSelf();
            return START_NOT_STICKY;
        }

        // TODO: Not sure if synchronization really required; check.
        synchronized (this) {
            if (mIsActive) {
                // To restart just reset the start time.
                mStartTime = System.currentTimeMillis();
                Log.i(TAG, "Restarting.");
                return START_STICKY;
            }
            mIsActive = true;
        }

        mStartTime = System.currentTimeMillis();

        // Setup UI
        mNotificationUi = new NotificationUi(this);
        mNotificationUi.enable();

        // Start periodical UI updates.
        mHandler = new RefreshHandler();
        mHandler.broadcastNow();

        // Tell Android that this service is related to a visible notification.
        // FIXME: mNotificationUi.getNotification() is not registered at this point!
        // startForeground(NotificationUi.NOTIFICATION_ID, mNotificationUi.getNotification());

        // Setup GPS listening.
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_MIN_TIME, 0, this);

        Log.i(TAG, "Service started");
        return START_STICKY;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(TAG, "onLocationChanged()");
        mLastFixTime = System.currentTimeMillis();
    }

    @Override
    public void onProviderDisabled(String provider) {
        // GPS explicitly turned off. The user obviously does not want a GPS fix.
        stopSelf();
    }

    @Override
    public void onProviderEnabled(String provider) {
        // This probably should never happen. If it does, ignore.
        Log.v(TAG, "onProviderEnabled()");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.v(TAG, "onStatusChanged(); status=" + status);
        mHandler.broadcastNow();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // no support for binding
    }
}
