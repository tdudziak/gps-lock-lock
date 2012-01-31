#!/usr/bin/env python

IN_MONKEYRUNNER = True
try:
    from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
except ImportError:
    IN_MONKEYRUNNER = False

from time import time, sleep
from threading import Thread
import subprocess
import sys
import os
import re

SCRIPT_PATH = os.path.abspath(__file__)
TOOLS_DIR = os.path.dirname(SCRIPT_PATH)
PROJECT_ROOT = os.path.abspath(TOOLS_DIR + '/../')

APK_PATH = os.path.join(PROJECT_ROOT, 'bin', 'gps_lock_lock-debug.apk')
PACKAGE = 'com.github.tdudziak.gps_lock_lock'

class Avd(object):
    free_port = 5554

    def __init__(self, name, extra_options):
        self.name = name
        self.extra_options = extra_options
        self.port = Avd.free_port
        Avd.free_port = Avd.free_port + 2


    def create(self):
        opts = '--force -n %s %s' % (self.name, self.extra_options)
        subprocess.call('android -s create avd ' + opts, shell=True)


    def start(self):
        emu_cmd = "emulator -avd %s -port %d -wipe-data -no-window" % (self.name, self.port)
        self.emulator = subprocess.Popen(emu_cmd, shell=True)

        logcat_cmd = 'adb -s emulator-%d logcat' % self.port
        self.logcat = subprocess.Popen(logcat_cmd, stdout=subprocess.PIPE, shell=True)

        # wait at least until the launcher app is started
        for line in self.logcat.stdout:
            if line.find('Start proc com.android.launcher') != -1:
                break


    def stop(self):
        cmd = 'adb -s emulator-%d emu kill' % self.port
        subprocess.call(cmd, shell=True)
        self.emulator.wait()


def monkeyscript():
    device = MonkeyRunner.waitForConnection()
    device.installPackage(APK_PATH)

    device.startActivity(component=PACKAGE + '/' + PACKAGE + '.LauncherActivity')
    MonkeyRunner.sleep(5)
    device.takeSnapshot().writeToFile('ControlActivity.png', 'png')

    device.drag((10,0), (10,500))
    MonkeyRunner.sleep(5)
    device.takeSnapshot().writeToFile('NotificationUi.png', 'png')


def main():
    avds = [
            Avd('ics-tablet',   '-t "Google Inc.:Google APIs:14" --skin WSVGA'),
            Avd('froyo-wvga', '-t android-8 --skin WVGA800'),
            Avd('froyo-qvga', '-t android-8 --skin QVGA'),
        ]

    # creating avds sometimes requires user input so proceed synchronously
    for avd in avds:
        avd.create()

    # the rest can be done in parallel
    def async_work(avd):
        print '[%s] Starting emulator.' % avd.name
        avd.start()

        out_dir = os.path.join(PROJECT_ROOT, 'screenshots', avd.name)
        try:
            os.makedirs(out_dir)
        except OSError, e:
            if e.errno != 17:
                # if the directory already exists it's not a problem
                raise e # FIXME: is it a proper way to rethrow in Python?

        print '[%s] Invoking monkeyrunner.' % avd.name
        os.chdir(out_dir)
        monkey_cmd = 'monkeyrunner -p %d %s' % (avd.port, SCRIPT_PATH)
        subprocess.call(monkey_cmd, shell=True)

        print '[%s] Monkeyrunner finished; stopping the emulator.' % avd.name
        avd.stop()

    for avd in avds:
        async_work(avd)
        # FIXME: for some reason monkeyrunner fails if executed in parallel
        # Thread(target=async_work, args=[avd]).start()


if __name__ == '__main__':
    if IN_MONKEYRUNNER:
        monkeyscript()
    else:
        main()
