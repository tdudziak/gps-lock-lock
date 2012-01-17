# What does this app do? #
You run it, it keeps your GPS on for a configurable amount of time. Typically,
when you have GPS enabled on your phone it isn't actively seeking your position
unless some application requests it. Using this app you can force the behavior
you want.

# Why would I want to do that? #
Acquiring GPS position is a process that will always take some time. Things
like aGPS can speed this up but you may still need to wait for about 20 seconds
depending on weather, your data connection, etc. This can be especially
annoying in the camera application which starts requesting your position in
preview mode.  It's hard to imagine waiting for GPS fix while people are posing
for your photo. I assume users don't usually wait that much and settle for less
precise non-gps coordinates but sometimes you would really like to tag you
photos precisely. If you can anticipate that you will need GPS in a few
minutes you can pre-launch it using this app.

The other reason is to prevent GPS from going offline when you still need it.
Some applications (like Google Maps for instance) don't request GPS position
when in background. If you switch to another app to read a text or email you
almost immediately lose GPS position. Theoretically, you should be able to
re-acquire it quickly but in practice it sometimes feels almost like a cold
start.

# What doesn't this app do? #
This app will not make the sole process of acquiring GPS fix any faster (though
using it you can start it earlier so the position will be available on time). It
doesn't track or store your position -- it just keeps *requesting* it from the
system. It doesn't collect any data or display advertisements. None of these
features are planned.

# State of implemenation #
The application seems to work properly but I'm occasionally tweaking details,
changing layout, etc. It should be considered work-in-progress.

# Devices #
I'm using [Samsung Galaxy S](http://en.wikipedia.org/wiki/Samsung_Galaxy_S)
with Android 2.2.1. That's currently the only device on which I can test this
app but in theory it should be compatible with Android 2.2+. I'm not using any
APIs outside of standard Android stuff.

# Disclaimer #
Please note that this program comes with **ABSOLUTELY NO WARRANTY**. I am not
liable for drained batteries, lost data, or any other unimaginable horrors
caused by this app.

You are free to modify and redistribute this software within certain
conditions. See COPYING for details.

Artwork distributed with this program is licensed under
[Creative Commons Attribution 3.0 Unported License](http://creativecommons.org/licenses/by/3.0/).
Notification icons were generated using [Android Asset Studio](http://android-ui-utils.googlecode.com/).
The application icon is loosely inspired by a stock Android drawable `stat_sys_gps_on`
available as a part of [Android Open Source Project](http://source.android.com/).
