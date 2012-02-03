# What does this app do? #
You run it, it keeps your GPS on for a configurable amount of time. Typically,
when you have GPS enabled on your phone it isn't actively seeking your position
unless some application requests it. Using this app you can force the behavior
you want.

# Why would I want to do that? #
Even in perfect weather and with rapid data connection acquiring GPS position
seems to take a good couple of seconds. It's nice to have precisely-geotagged
photos but waiting for GPS while you are taking a picture can be annoying.
If you can anticipate that you will need GPS soon you can pre-launch it using
this app.

The other reason is to prevent GPS from going offline when you still need it.
Some applications (like Google Maps for instance) don't request GPS position
when in background. If you switch to another app to read a text or email you
almost immediately lose GPS position. Theoretically, you should be able to
re-acquire it quickly but in practice it sometimes feels almost like a cold
start.

# What this app doesn't do #
This app will not make the sole process of acquiring GPS fix any faster (though
using it you can start it earlier so the position will be available on time). It
doesn't track or store your position — it just keeps *requesting* it from the
system. It doesn't collect any data or display advertisements. None of these
features are planned.

# Disclaimer #
Please note that this program comes with **ABSOLUTELY NO WARRANTY**.
You are free to modify and redistribute this software within certain conditions.
Source code is available on [GitHub](https://github.com/tdudziak/gps-lock-lock).
Read the `COPYING` file in the source package for details.

Artwork distributed with this program is licensed under
[Creative Commons Attribution 3.0 Unported License](http://creativecommons.org/licenses/by/3.0/).
Notification icons were generated using [Android Asset Studio](http://android-ui-utils.googlecode.com/).
The application icon is loosely inspired by a stock Android drawable `stat_sys_gps_on`
available as a part of [Android Open Source Project](http://source.android.com/).
