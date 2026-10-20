# Validation

## Public-source preparation: 0.4.0-alpha01

Prepared on 20 October 2026. Native Java, JDK 17, compile/target API 34, minimum API 26. Debug ID is `dev.opentv.launcher.debug`; release ID is `dev.opentv.launcher`.

Command:

```bash
bash build.sh assembleDebug assembleRelease testDebugUnitTest lintDebug
```

The public-default changes build successfully for debug and unsigned release. All 79 JVM tests pass (zero failures, errors, or skips). Debug lint reports zero errors, 52 warnings, and 43 informational findings. All 64 source XML documents parse, and the workflow/issue YAML files parse successfully. The [Android checks workflow](https://github.com/PatilSarvesh/Open-TV/actions/workflows/build.yml) repeats builds, tests, and lint on GitHub; its run history records hosted results for each commit.

An exact copy of the 136 staged public files was built in a fresh directory without the workspace's `.toolchain/` folder, using explicit external JDK/SDK paths and the existing dependency cache. The same build/test/lint checks passed. Packaged application IDs and absence of Internet permission were checked for debug and release.

Release signing was verified using a disposable key, which was deleted afterward. No-credential builds are unsigned; a partially configured signing environment fails with the expected message; a fully configured environment produces an APK accepted by `apksigner verify`. No protected public release key has been created. The staged-file audit excluded personal device records, raw backups, local toolchains, private machine paths, network addresses, and signing files.

Changes in this preparation: first-use HDMI selection with provider-neutral text, removal of the personal dock migration, separate development application ID, and release signing from optional environment variables. No APK from this preparation has been installed on the physical TV yet.

## Physical prototype evidence

Xiaomi MiTV-MOOR0 (`venom`), Android TV 11, prototype 0.3.0:

- Dock and D-pad selection inspected on an actual 1920×1080 TV.
- Selecting Live TV opened the native TV player and the configured HDMI input.
- User confirmed picture and sound from the connected set-top box.
- User confirmed physical Xiaomi remote Home returned to Open TV with its optional Home accessibility service enabled.
- Stock launcher remained installed and enabled.

These are observations for the prototype, not certification of the new public alpha. Cold boot, sleep/wake, streaming playback/return, favorites editing persistence, update migration, and additional devices remain to be tested. No emulator run has been performed.

The personal prototype used a local debug signing key. Public release signing is a separate identity; see docs/RELEASING.md. Private device-operation records remain local and are not part of the published source.
