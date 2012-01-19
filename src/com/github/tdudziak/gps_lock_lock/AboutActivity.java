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
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.webkit.WebView;
import android.widget.TextView;

public class AboutActivity extends Activity
{
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mWebView = new WebView(this);
        mWebView.loadUrl("file:///android_asset/help.html");
        setContentView(mWebView);

        super.onCreate(savedInstanceState);
    }
}