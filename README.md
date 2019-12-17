# Pocket Watch Parent Gate Hook plugin

## Getting Started

Setup your Plugin dev environment as described here: https://developer-zapp.applicaster.com/dev-env/android.html

Clone the project from github, cd to the Android folder, and open in Android Studio

## Description

There is 1 plugins with corresponding gradle modules:

- `pocket_watch-hook-plugin`: COPPA logic;

`app` module is used as a local sample.
 
## Deployment

1. Update version for desired module to deploy:
```
$MODULE/gradle.properties
$MODULE/plugin-manifest.json
```
2. Deploy manifest from the shell. Circle ci will deploy to bintray automatically.
```
zappifest publish --manifest plugin-manifest.json
```
