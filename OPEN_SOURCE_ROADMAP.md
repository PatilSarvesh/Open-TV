# Open TV roadmap

Scope selected on 20 October 2026: **Android TV / Google TV, with Xiaomi verified first**. Repository destination: `PatilSarvesh/Open-TV`.

Open TV is a CleanYourTV derivative, focused on a simple local home screen, remote navigation, and configurable HDMI access. Preserve upstream attribution and the MIT/font notices.

## Public foundation

- [x] Replace the personal HDMI 3 assumption with a first-use input chooser and provider-neutral text.
- [x] Remove the personal dock migration; retain saved preferences.
- [x] Separate development and public application IDs.
- [x] Add optional release signing; unsigned builds no longer use the debug key.
- [x] Prepare public README, compatibility, contribution, privacy, issue, and release documentation.
- [x] Exclude personal device-operation records and signing/toolchain files from Git.
- [x] Add build, test, and lint workflow and dependency-update configuration.
- [x] Publish source at [PatilSarvesh/Open-TV](https://github.com/PatilSarvesh/Open-TV).
- GitHub-hosted check results are available in the [build workflow](https://github.com/PatilSarvesh/Open-TV/actions/workflows/build.yml).

## Before an installable public alpha

- [ ] Create and securely back up the protected public signing key.
- [ ] Test the changed public build on a TV: first-time HDMI setup, changing inputs, favorites editing, app playback/return, and preference persistence.
- [ ] Test physical Home, cold boot, and sleep/wake.
- [ ] Add a screenshot of the public build without personal account or network details.
- [ ] Publish a signed APK with checksum, certificate fingerprint, known limitations, and recovery instructions.

## After initial feedback

- Export/import settings, with explicit handling of inputs missing on another device.
- Multiple named HDMI shortcuts such as Set-top box and Console.
- Community device reports and manufacturer workarounds supported by evidence.
- Remote accessibility and performance improvements on lower-powered TVs.

Only the Xiaomi prototype currently has physical validation. Do not claim universal compatibility. Samsung Tizen and LG webOS need separate applications; Fire TV support is unverified. Keep automated debloating outside normal launcher setup.
