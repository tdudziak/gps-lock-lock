GPS fix is not something that should be taken for granted and sometimes you
need to wait a good few seconds or minutes before you finally get your precise
position. Thus, I find it slightly annoying that every time I switch from
Google Maps to another application I lose track of my coordinates. While many
navigation apps keep GPS turned on while in background, the default
energy-preserving behavior of all other apps is slightly annoying.

I tried to fix that by running a navigation app in the background every time I
knew I want to use the GPS for a while but it was a bit of a hassle. This app
tries to be easier to use -- You just run it and it launches a background
service that will request GPS position for next 5 minutes. Remaining time is
shown in the notification area.

This is a quick hack and it should be considered work-in-progress. The locking
time is currently unconfigurable (unless you count editing the source code) and
it's probably buggy like hell. It doesn't even have a proper app icon!

The app has been tested on [Galaxy S](http://en.wikipedia.org/wiki/Samsung_Galaxy_S) 
with Android 2.2.1. If you find that this app works or doesn't work with any
other phone you're very welcome to inform me.

Please note that this program comes with **ABSOLUTELY NO WARRANTY**. I am not
liable for drained batteries, lost data, or any other unimaginable horrors
caused by this app.

You are free to modify and redistribute this software within certain
conditions. See COPYING for details.
