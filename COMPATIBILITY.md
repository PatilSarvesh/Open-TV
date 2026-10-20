# Device compatibility

Open TV targets Android TV / Google TV, minimum Android 8.0 (API 26). That minimum is a build requirement, not proof that every supported Android version or TV has been tested.

| Device | OS | Tested version | Observed behavior | Outstanding checks |
| --- | --- | --- | --- | --- |
| Xiaomi MiTV-MOOR0 (`venom`) | Android TV 11 | Personal prototype 0.3.0 | Three-item dock, D-pad selection, HDMI 3 video/audio, and physical Xiaomi remote Home with optional accessibility service | Public alpha, cold boot, sleep/wake, playback/return, editor persistence |
| Other Android TV / Google TV devices | API 26+ | None | Unverified | All device checks |

Samsung Tizen and LG webOS cannot run this Android APK. Fire TV support is unverified.

## Manufacturer differences

HDMI discovery and switching depend on the TV exposing inputs to third-party apps. An external streaming box may expose no inputs. Use the TV remote's Input/Source button if Open TV cannot discover or launch an input. The Live TV shortcut launches a connected device's HDMI picture; it does not provide a streaming channel service.

On the tested Xiaomi firmware, assigning the HOME role did not override the stock launcher's activity priority. Open TV's optional Home button accessibility service worked with the physical remote. Software-injected Home did not behave the same way. No universal behavior is assumed for other TVs.

## Submit a device report

Include brand/model, Android version, firmware version, Open TV version/build type, and results for:

- Launching apps and returning with Back/Home.
- All D-pad directions, focus visibility, OK, and long-OK.
- Setting and restoring the default home.
- HDMI discovery, switching, and changing the saved input.
- Sleep/wake, cold boot, and an APK update retaining preferences.

Do not include network addresses, device serial numbers, account information, or unredacted logs.
