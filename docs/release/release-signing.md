# Release Signing

Hermes release signing is configured through ignored local secrets. Do not commit keystores, passwords, or upload keys.

## Required Local Values

Add these values to `local.properties`, Gradle properties, or environment variables:

```properties
HERMES_RELEASE_STORE_FILE=D:\\secure\\hermes\\hermes-upload-key.jks
HERMES_RELEASE_STORE_PASSWORD=replace-with-keystore-password
HERMES_RELEASE_KEY_ALIAS=hermes-upload
HERMES_RELEASE_KEY_PASSWORD=replace-with-key-password
```

`local.properties` is already ignored by git. Keystore files ending in `.jks` or `.keystore` are also ignored.

## Generate An Upload Key

Use a secure location outside the repository:

```powershell
keytool -genkeypair `
  -v `
  -keystore D:\secure\hermes\hermes-upload-key.jks `
  -alias hermes-upload `
  -keyalg RSA `
  -keysize 4096 `
  -validity 10000
```

Store the keystore and passwords in a password manager. Losing the upload key can block future releases unless Play App Signing key reset is available for the account.

## Build The Signed Bundle

After local signing values are set:

```powershell
.\gradlew.bat :app:bundleRelease
jarsigner -verify -verbose -certs app\build\outputs\bundle\release\app-release.aab
```

The verification output must show that signatures are verified. If it says `jar is unsigned`, Play upload is not ready.

## Current State

The project can build `app\build\outputs\bundle\release\app-release.aab`, but without local signing values it is unsigned and must not be uploaded to Play production.
