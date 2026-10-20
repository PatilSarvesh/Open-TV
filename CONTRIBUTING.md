# Contributing

Open TV targets Android TV / Google TV. Xiaomi MiTV-MOOR0 on Android 11 is the first device tested; support for other models needs evidence. Check [COMPATIBILITY.md](COMPATIBILITY.md) before reporting a device issue.

## Development

Install JDK 17, Android SDK platform 35, and build-tools 36.0.0. Use the included Gradle 9.8.1 wrapper with Android Gradle plugin 9.4.1. Set `JAVA_HOME` and `ANDROID_HOME`, then run:

```bash
bash build.sh
```

This builds the debug APK and runs JVM tests and lint. Debug builds use `dev.opentv.launcher.debug` so contributors can install them alongside the public release. Signing keys are local and must not be committed.

The launcher is native Java. Resources live under `launcher/app/src/main/res`, Java source under `launcher/app/src/main/java/dev/opentv/launcher`, and JVM tests under `launcher/app/src/test`.

## Changes

Keep pull requests focused. Describe the user-visible behavior and relevant validation. Add meaningful tests for navigation, preference migration, or input-selection logic when changing those behaviors. For UI changes, verify every control using only D-pad, OK, and Back on a TV or TV emulator; describe the device and attach screenshots where useful.

Preserve saved user choices across updates. Keep manufacturer workarounds isolated and document the exact tested device/firmware. Do not add automatic package disabling or clearing to launcher startup. Network access and new permissions need an explicit explanation of the feature and data involved.

Retain the CleanYourTV MIT attribution and font license. By contributing, you agree to distribute your contribution under this project's MIT license.

## Reports

Use the repository's bug/device report or feature-request form. Remove account details, Wi-Fi names, IP addresses, serial numbers, and tokens from logs and screenshots. Read [SECURITY.md](SECURITY.md) before reporting a vulnerability.

Communicate respectfully, explain disagreements with evidence, and avoid personal attacks or harassment. Maintainers may remove abusive content.
