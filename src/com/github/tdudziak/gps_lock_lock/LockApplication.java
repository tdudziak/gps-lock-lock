package com.github.tdudziak.gps_lock_lock;

import java.lang.ref.WeakReference;

public class LockApplication extends android.app.Application
{
    WeakReference<LockService> mService;

    public void setLockService(LockService ca) {
        mService = new WeakReference<LockService>(ca);
    }

    public LockService getLockService() {
        if (mService != null) return mService.get();
        return null;
    }
}
