// Current sideloaded beta release. Bump APK_VERSION here (and upload the
// matching GitHub Release asset) on every release — everything else derives
// from it, so the version never has to be edited in more than one place.
export const APK_VERSION = "0.1.0-beta";

// Must exactly match the asset filename uploaded to the GitHub Release for
// this tag — release download URLs 404 on any filename mismatch.
export const APK_FILENAME = `lastwagon-${APK_VERSION}.apk`;

// Direct-download URL for the GitHub Release asset — a plain <a href> to this
// URL triggers a native browser download, no landing page or JS needed.
// Hardcoded to the current tag on purpose: no build-time/runtime GitHub API
// lookup of "latest" for v1; automate that once there's a real release cadence.
//
// Sideload notes:
// - Users will likely need to allow "install from unknown sources" on their
//   device to install this APK. That's expected for sideloaded apps, not a bug.
// - Deliberately NO user-agent detection or platform gating for v1 (no
//   "you're not on Android" warnings) — keep it a simple direct link.
export const APK_DOWNLOAD_URL = `https://github.com/jackofall1232/lastwagon/releases/download/${APK_VERSION}/${APK_FILENAME}`;
