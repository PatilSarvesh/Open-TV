#!/usr/bin/env bash
# Build and check the launcher locally. This script never connects to a TV.
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
if [ -z "${JAVA_HOME:-}" ]; then
  export JAVA_HOME="$ROOT/.toolchain/jdk/amazon-corretto-17.jdk/Contents/Home"
fi
if [ -z "${ANDROID_HOME:-}" ]; then
  export ANDROID_HOME="$ROOT/.toolchain/sdk"
fi
export GRADLE_USER_HOME="${GRADLE_USER_HOME:-$ROOT/.toolchain/gradle}"
export ANDROID_USER_HOME="${ANDROID_USER_HOME:-$ROOT/.toolchain/android-user}"
if [ ! -x "$JAVA_HOME/bin/java" ] || [ ! -f "$ANDROID_HOME/platforms/android-34/android.jar" ] || [ ! -d "$ANDROID_HOME/build-tools/36.0.0" ]; then
  echo "Java 17 and Android SDK platform 34/build-tools 36.0.0 are required." >&2
  echo "Set JAVA_HOME and ANDROID_HOME to your existing installation. See README.md." >&2
  exit 1
fi
export PATH="$JAVA_HOME/bin:$PATH"
cd "$ROOT/launcher"
if [ "$#" -eq 0 ]; then set -- assembleDebug testDebugUnitTest lintDebug; fi
exec bash ./gradlew --no-daemon --console=plain "$@"
