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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import android.text.method.LinkMovementMethod;

public class SettingsActivity extends Activity implements OnClickListener
{
    TextView mTextStatus;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Enable links in textInfo.
        TextView info = (TextView) findViewById(R.id.textInfo);
        info.setMovementMethod(LinkMovementMethod.getInstance());

        findViewById(R.id.buttonStop).setOnClickListener(this);

        Button restart = (Button) findViewById(R.id.buttonRestart);
        restart.setOnClickListener(this);
        String r_format = getResources().getString(R.string.button_restart);
        restart.setText(String.format(r_format, LockService.LOCK_LOCK_MINUTES));

        mTextStatus = (TextView) findViewById(R.id.textStatus);
        String s_format = getResources().getString(R.string.text_status);
        mTextStatus.setText(String.format(s_format, LockService.LOCK_LOCK_MINUTES)); // FIXME: temporary and incorrect
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
        case R.id.buttonRestart:
            start();
            break;

        case R.id.buttonStop:
            stop();
            finish();
            break; // unreachable?
        }
    }

    private void start() {
        startService(new Intent(this, LockService.class));
    }

    private void stop() {
        Intent intent = new Intent(LockService.ACTION_SHUTDOWN);
        intent.setClass(this, LockService.class);
        startService(intent);
    }
}
