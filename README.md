# Open TV

[![Android checks](https://github.com/PatilSarvesh/Open-TV/actions/workflows/build.yml/badge.svg)](https://github.com/PatilSarvesh/Open-TV/actions/workflows/build.yml)

A simple, ad-free home screen for **Android TV / Google TV**, with remote-friendly favorites and a shortcut to your selected HDMI device.

Open TV is a native Java derivative of [CleanYourTV](https://github.com/Gathin23/cleanyourtv). It retains the upstream MIT notice and font license. See [UPSTREAM.md](UPSTREAM.md), [LICENSE](LICENSE), and [third-party notices](THIRD_PARTY_NOTICES.md).

**Status: experimental source, version 0.4.0-alpha01.** Canonical repository: [PatilSarvesh/Open-TV](https://github.com/PatilSarvesh/Open-TV). No signed public APK release is available yet. Xiaomi MiTV-MOOR0 on Android TV 11 is the first tested prototype device; the current alpha source still needs physical-device validation. Other models are unverified. See [COMPATIBILITY.md](COMPATIBILITY.md).

## Features

- Three-slot favorites dock, initially Live TV → YouTube → Prime Video; missing apps are skipped and slots remain empty.
- All other TV-launchable apps in the grid below, with on-TV favorite ordering and hide/show controls.
- Live TV opens a selected HDMI input. Choose the input on first use; hold OK on the tile to change it.
- Visible D-pad focus, a dark backdrop, clock, connection indicator, and local appearance settings.
- Preferences and last-selected app stored locally.
- Optional Home-button accessibility workaround for firmware that ignores normal home selection.
- No Internet permission, account, analytics backend, or ongoing computer connection.

Open TV opens your installed apps; subscriptions and ads inside those apps remain governed by those apps. The Live TV tile opens a physical input, not an included streaming service. HDMI discovery and Home behavior vary by firmware.

## Build

Requires **JDK 17**, Android SDK **platform 34**, and **build-tools 34.0.0**. Set `JAVA_HOME` and `ANDROID_HOME` to your installations. The Gradle wrapper downloads build dependencies on first use.

```bash
bash build.sh
```

This builds the debug APK, runs JVM tests, and runs Android lint. The script performs no TV operations.

Development APK: `launcher/app/build/outputs/apk/debug/app-debug.apk`.

Development application ID: `dev.opentv.launcher.debug`; public release ID: `dev.opentv.launcher`. Development builds can coexist with the public app. A separately built APK may use a different signing key and cannot necessarily update someone else's build.

For a shrunk release build:

```bash
bash build.sh assembleRelease
```

Without release credentials this produces **app-release-unsigned.apk**, which cannot be installed as a normal release. See [release signing](docs/RELEASING.md) before distributing an APK.

Existing local workspaces may use ignored tools under `.toolchain/`; contributors do not need that directory if JDK/SDK environment variables are configured. Android Studio can also open the `launcher/` directory.

## Try a development build

Enable debugging on your own TV and authorize the computer using the TV's prompt. Use Android platform-tools to install and open the APK:

```bash
adb install -r launcher/app/build/outputs/apk/debug/app-debug.apk
adb shell am start -W -n dev.opentv.launcher.debug/dev.opentv.launcher.MainActivity
```

For a legacy network-debugging TV, `adb connect TV_IP:5555` may be available. Pairing-code devices require the address, ports, and pairing flow shown on that device. A continuous ADB connection is unnecessary after installation; disable debugging when finished if you no longer need it.

## Use and customize

Use the remote's arrows and OK. Down reveals the app grid. Back returns toward the dock. Settings → Favorites and hidden apps lets you add/remove favorites, reorder them, and hide/show tiles. Hiding a tile does not disable or uninstall its app. Settings also exposes columns, motion controls, system settings, and the default-home picker.

Select Live TV and choose the HDMI input connected to your set-top box or other device. The selection is saved after Android accepts the launch request. Hold OK on Live TV to choose again. Inputs remain selectable even if the connected device is off; it must be powered on to show a picture. If the TV exposes no inputs or rejects the launch, use the remote's Input/Source button.

Holding OK on an ordinary app opens its Android app settings. App discovery focuses on TV launch entries; phone-only sideloaded apps may not appear.

## Home button and recovery

First test app launching, D-pad, Back, and HDMI access while keeping the stock launcher available. Use Open TV's default-home picker where the TV supports it.

Some firmware prioritizes the stock home even after another app receives the HOME role. The optional **Home button** service under TV Settings → Accessibility can route HOME/ALL_APPS keys to Open TV on compatible remotes. It does not retrieve screen content or store key events. Physical-remote behavior must be tested on each device, including after updates, reboot, and sleep/wake.

To return to the original home, turn off Open TV's Home button accessibility service and select the stock launcher in the system home picker where available. Stock package/component names vary by TV. This project does not automatically disable the stock launcher or other installed packages.

The personal prototype used a debug certificate with the public package ID. Migrating that prototype to a differently signed public APK needs a planned reinstall; see [RELEASING.md](docs/RELEASING.md).

## Project information

- [Device compatibility](COMPATIBILITY.md)
- [Validation and limitations](VALIDATION.md)
- [Contributing](CONTRIBUTING.md)
- [Security and privacy](SECURITY.md)
- [Roadmap](OPEN_SOURCE_ROADMAP.md)
- [Changelog](CHANGELOG.md)
- [Original repository research](RESEARCH.md)

Personal device inventories, cleanup records, signing keys, and local toolchains are excluded from the public source tree. Report issues with the device model, Android/firmware version, app version, and reproduction steps; redact personal details from logs and screenshots.
