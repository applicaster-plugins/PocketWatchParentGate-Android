zappifest publish pocket_watch-hook-plugin/plugin-manifest.json --account 5d642ecd161adb0008ea8e50
./gradlew assembleRelease
./gradlew :pocket_watch-hook-plugin:bintrayUpload
