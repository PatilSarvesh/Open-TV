# Upstream attribution

Open TV is a derivative of [CleanYourTV](https://github.com/Gathin23/cleanyourtv), previously named tv-vibehack, by Gathin and contributors. It began as a personal Xiaomi TV setup and is being prepared as an open-source Android TV / Google TV project.

Imported from commit `c524b27ce7bdf8dd8d7da0c494b2f54f231df4e9` on 20 October 2026 using GitHub's source archive. The native launcher, resources, Gradle wrapper, and existing JVM tests are derived from that snapshot. The upstream device administration scripts and debloat lists are not included.

The original MIT copyright notice is preserved in LICENSE. Bundled fonts retain their license files under launcher/. Application namespace and release ID are changed to `dev.opentv.launcher`; development builds use `dev.opentv.launcher.debug`. Open TV changes include on-TV favorite ordering and app visibility, persistent focus, a saved HDMI Live TV shortcut, build-only tooling, and public-project documentation. The personal HDMI default and forced dock migration were removed for the public source preparation. No upstream device administration or debloat script is bundled.

This project is independent of Xiaomi, Google, Anthropic, and the upstream authors. Installed app names and artwork remain those supplied by their respective apps.
