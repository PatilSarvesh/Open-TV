# Releasing Open TV

Source publication and distributing an installable release APK are separate steps. The initial public source is experimental; a signed public APK has not yet been released.

## Signing identity

Public application ID: `dev.opentv.launcher`. Development ID: `dev.opentv.launcher.debug`.

The personal 0.3.0 prototype used the public ID with a local debug certificate. A future public release key will not update that APK in place. Preserve the personal APK/key locally and plan an explicit preference backup and reinstall before migrating that test TV. Do not work around a signature mismatch by sharing a debug private key.

Generate the public release key on the maintainer's trusted machine with Android's `keytool`, using its password prompts. Keep an encrypted backup outside the repository. Never put a keystore, password, signing environment file, or raw private key into Git, an issue, or a pull request.

Set all four variables in the local signing environment:

- `OPENTV_KEYSTORE_PATH`: absolute path to the private keystore.
- `OPENTV_KEYSTORE_PASSWORD`: keystore password.
- `OPENTV_KEY_ALIAS`: signing alias.
- `OPENTV_KEY_PASSWORD`: key password.

Then run:

```bash
bash build.sh assembleRelease testDebugUnitTest lintDebug
```

A signed build writes `launcher/app/build/outputs/apk/release/app-release.apk`. With none of the variables set, the output is `app-release-unsigned.apk`, which must not be advertised as installable. Partial credentials stop configuration with an error. The CI workflow receives no signing credentials and only uploads the development APK and reports.

## Alpha checklist

1. Verify the README, compatibility matrix, upstream notices, third-party notices, and changelog describe the actual version. Review notices for dependencies packaged in the APK and include the required license/notice material with binary distribution. Inspect the publication file list for private device records and credentials.
2. Build from a clean checkout using external JDK/SDK paths; run the checks. Validate the release signing path using a disposable test key before using the protected public key.
3. Test the public build on a real TV: first-time HDMI selection, saved choices, favorites editing, focus, playback/return, physical Home, cold boot, and sleep/wake. Do not reuse prototype observations as proof for a changed build.
4. Verify the signed APK with SDK build-tools `apksigner verify --verbose --print-certs`. Record its SHA-256 checksum and signing-certificate fingerprint.
5. Create a version tag and release containing the signed APK, checksum, certificate fingerprint, source reference, known limitations, and installation/recovery instructions. Never attach unsigned APKs as end-user downloads.
6. Enable private vulnerability reporting and the repository's available dependency/secret security features. Configure branch protection after the build workflow has run successfully.

Official Android signing reference: https://developer.android.com/studio/publish/app-signing
