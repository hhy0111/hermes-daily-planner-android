# Hermes Study Vault Android MVP Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build the first local-first Android MVP for Hermes Study Vault: capture sources, create evidence blocks, write notes, track deadlines, review cards, search locally, and export data.

**Architecture:** Native Android app using Kotlin, Jetpack Compose, Room, and a repository/use-case structure. Room is the local source of truth, app-specific storage holds imported files, and UI screens observe ViewModels through immutable state.

**Tech Stack:** Kotlin, Android Gradle Plugin, Jetpack Compose, Material 3, Room/SQLite, DataStore, Navigation Compose, JUnit, AndroidX test libraries.

---

## Scope Check

This plan covers Phase 1 Local MVP only. It intentionally excludes AI, cloud sync, OCR, full PDF annotation, Google Calendar integration, desktop apps, and collaboration. Those are separate product phases and should receive separate specs/plans.

The current repository contains only planning documents. This plan starts by creating the Android project skeleton.

Local environment observed on 2026-06-16:

- JDK 17 is installed.
- Gradle 8.7 is installed globally.
- `ANDROID_HOME` and `ANDROID_SDK_ROOT` are not set.

Implementation prerequisite:

- Install Android SDK Platform 37 and Android SDK Build-Tools through Android Studio or command-line tools.
- Set `ANDROID_HOME` to the SDK directory before running Android builds.
- Use the Gradle Wrapper created in this plan, not the global Gradle installation.

## File Structure

Create these project files:

- `settings.gradle.kts`: Gradle plugin management and module registration.
- `build.gradle.kts`: root build configuration.
- `gradle/libs.versions.toml`: dependency versions.
- `gradle/wrapper/gradle-wrapper.properties`: wrapper distribution.
- `app/build.gradle.kts`: Android app configuration and dependencies.
- `app/src/main/AndroidManifest.xml`: main activity and Android share target.
- `app/src/main/java/com/hermes/studyvault/MainActivity.kt`: Compose entry point.
- `app/src/main/java/com/hermes/studyvault/StudyVaultApp.kt`: app-level composition.

Create core domain files:

- `app/src/main/java/com/hermes/studyvault/domain/review/ReviewGrade.kt`
- `app/src/main/java/com/hermes/studyvault/domain/review/ReviewScheduler.kt`
- `app/src/main/java/com/hermes/studyvault/domain/share/SharedPayload.kt`
- `app/src/main/java/com/hermes/studyvault/domain/export/MarkdownExporter.kt`
- `app/src/main/java/com/hermes/studyvault/domain/export/JsonBackupExporter.kt`
- `app/src/main/java/com/hermes/studyvault/domain/today/TodayOverview.kt`
- `app/src/main/java/com/hermes/studyvault/domain/today/GetTodayOverviewUseCase.kt`
- `app/src/main/java/com/hermes/studyvault/domain/search/SearchVaultUseCase.kt`

Create data files:

- `app/src/main/java/com/hermes/studyvault/data/local/StudyVaultDatabase.kt`
- `app/src/main/java/com/hermes/studyvault/data/local/Converters.kt`
- `app/src/main/java/com/hermes/studyvault/data/local/entity/SourceEntity.kt`
- `app/src/main/java/com/hermes/studyvault/data/local/entity/EvidenceBlockEntity.kt`
- `app/src/main/java/com/hermes/studyvault/data/local/entity/NoteEntity.kt`
- `app/src/main/java/com/hermes/studyvault/data/local/entity/DeadlineEntity.kt`
- `app/src/main/java/com/hermes/studyvault/data/local/entity/ReviewCardEntity.kt`
- `app/src/main/java/com/hermes/studyvault/data/local/entity/TagEntity.kt`
- `app/src/main/java/com/hermes/studyvault/data/local/entity/CollectionEntity.kt`
- `app/src/main/java/com/hermes/studyvault/data/local/dao/SourceDao.kt`
- `app/src/main/java/com/hermes/studyvault/data/local/dao/EvidenceBlockDao.kt`
- `app/src/main/java/com/hermes/studyvault/data/local/dao/NoteDao.kt`
- `app/src/main/java/com/hermes/studyvault/data/local/dao/DeadlineDao.kt`
- `app/src/main/java/com/hermes/studyvault/data/local/dao/ReviewCardDao.kt`
- `app/src/main/java/com/hermes/studyvault/data/repository/VaultRepository.kt`
- `app/src/main/java/com/hermes/studyvault/data/repository/RoomVaultRepository.kt`

Create UI files:

- `app/src/main/java/com/hermes/studyvault/navigation/VaultRoute.kt`
- `app/src/main/java/com/hermes/studyvault/navigation/VaultNavHost.kt`
- `app/src/main/java/com/hermes/studyvault/ui/theme/Color.kt`
- `app/src/main/java/com/hermes/studyvault/ui/theme/Theme.kt`
- `app/src/main/java/com/hermes/studyvault/ui/components/VaultBottomBar.kt`
- `app/src/main/java/com/hermes/studyvault/ui/today/TodayScreen.kt`
- `app/src/main/java/com/hermes/studyvault/ui/inbox/InboxScreen.kt`
- `app/src/main/java/com/hermes/studyvault/ui/vault/VaultScreen.kt`
- `app/src/main/java/com/hermes/studyvault/ui/write/WriteScreen.kt`
- `app/src/main/java/com/hermes/studyvault/ui/review/ReviewScreen.kt`

Create tests:

- `app/src/test/java/com/hermes/studyvault/domain/review/ReviewSchedulerTest.kt`
- `app/src/test/java/com/hermes/studyvault/domain/share/CreateSourceFromShareUseCaseTest.kt`
- `app/src/test/java/com/hermes/studyvault/domain/export/MarkdownExporterTest.kt`
- `app/src/test/java/com/hermes/studyvault/domain/export/JsonBackupExporterTest.kt`
- `app/src/test/java/com/hermes/studyvault/domain/today/GetTodayOverviewUseCaseTest.kt`
- `app/src/test/java/com/hermes/studyvault/domain/search/SearchVaultUseCaseTest.kt`
- `app/src/androidTest/java/com/hermes/studyvault/data/local/StudyVaultDatabaseInstrumentedTest.kt`
- `app/src/androidTest/java/com/hermes/studyvault/navigation/VaultNavigationInstrumentedTest.kt`

---

### Task 1: Android Project Skeleton

**Files:**
- Create: `settings.gradle.kts`
- Create: `build.gradle.kts`
- Create: `gradle/libs.versions.toml`
- Create: `gradle/wrapper/gradle-wrapper.properties`
- Create: `gradle.properties`
- Create: `app/build.gradle.kts`
- Create: `app/src/main/AndroidManifest.xml`
- Create: `app/src/main/java/com/hermes/studyvault/MainActivity.kt`
- Create: `app/src/main/java/com/hermes/studyvault/StudyVaultApp.kt`
- Create: `app/src/main/java/com/hermes/studyvault/ui/theme/Color.kt`
- Create: `app/src/main/java/com/hermes/studyvault/ui/theme/Theme.kt`

- [ ] **Step 1: Create Gradle settings**

Create `settings.gradle.kts`:

```kotlin
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "HermesStudyVault"
include(":app")
```

- [ ] **Step 2: Create root build configuration**

Create `build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
}
```

- [ ] **Step 3: Create dependency catalog**

Create `gradle/libs.versions.toml`:

```toml
[versions]
agp = "9.2.0"
kotlin = "2.4.0"
ksp = "2.3.9"
composeBom = "2026.04.01"
coreKtx = "1.17.0"
activityCompose = "1.11.0"
lifecycle = "2.9.4"
navigationCompose = "2.9.5"
room = "2.8.4"
datastore = "1.1.7"
coroutines = "1.10.2"
junit = "4.13.2"
androidxJunit = "1.3.0"
espresso = "3.7.0"

[libraries]
androidx-core-ktx = { module = "androidx.core:core-ktx", version.ref = "coreKtx" }
androidx-activity-compose = { module = "androidx.activity:activity-compose", version.ref = "activityCompose" }
androidx-lifecycle-runtime-ktx = { module = "androidx.lifecycle:lifecycle-runtime-ktx", version.ref = "lifecycle" }
androidx-lifecycle-viewmodel-compose = { module = "androidx.lifecycle:lifecycle-viewmodel-compose", version.ref = "lifecycle" }
androidx-navigation-compose = { module = "androidx.navigation:navigation-compose", version.ref = "navigationCompose" }
androidx-compose-bom = { module = "androidx.compose:compose-bom", version.ref = "composeBom" }
androidx-compose-ui = { module = "androidx.compose.ui:ui" }
androidx-compose-ui-tooling-preview = { module = "androidx.compose.ui:ui-tooling-preview" }
androidx-compose-ui-tooling = { module = "androidx.compose.ui:ui-tooling" }
androidx-compose-ui-test-junit4 = { module = "androidx.compose.ui:ui-test-junit4" }
androidx-compose-ui-test-manifest = { module = "androidx.compose.ui:ui-test-manifest" }
androidx-material3 = { module = "androidx.compose.material3:material3" }
androidx-room-runtime = { module = "androidx.room:room-runtime", version.ref = "room" }
androidx-room-ktx = { module = "androidx.room:room-ktx", version.ref = "room" }
androidx-room-compiler = { module = "androidx.room:room-compiler", version.ref = "room" }
androidx-room-testing = { module = "androidx.room:room-testing", version.ref = "room" }
androidx-datastore-preferences = { module = "androidx.datastore:datastore-preferences", version.ref = "datastore" }
kotlinx-coroutines-test = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-test", version.ref = "coroutines" }
junit = { module = "junit:junit", version.ref = "junit" }
androidx-junit = { module = "androidx.test.ext:junit", version.ref = "androidxJunit" }
androidx-espresso-core = { module = "androidx.test.espresso:espresso-core", version.ref = "espresso" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
```

- [ ] **Step 4: Create Gradle Wrapper properties**

Create `gradle/wrapper/gradle-wrapper.properties`:

```properties
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-9.4.1-bin.zip
networkTimeout=10000
validateDistributionUrl=true
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```

- [ ] **Step 5: Create Gradle runtime properties**

Create `gradle.properties`:

```properties
org.gradle.jvmargs=-Xmx4096m -Dfile.encoding=UTF-8
org.gradle.parallel=true
android.useAndroidX=true
```

- [ ] **Step 6: Create Android app build file**

Create `app/build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.hermes.studyvault"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.hermes.studyvault"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "0.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.datastore.preferences)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.room.testing)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
```

- [ ] **Step 7: Create manifest**

Create `app/src/main/AndroidManifest.xml`:

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="Hermes Study Vault"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.HermesStudyVault">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
            <intent-filter>
                <action android:name="android.intent.action.SEND" />
                <category android:name="android.intent.category.DEFAULT" />
                <data android:mimeType="text/plain" />
            </intent-filter>
            <intent-filter>
                <action android:name="android.intent.action.SEND" />
                <category android:name="android.intent.category.DEFAULT" />
                <data android:mimeType="image/*" />
            </intent-filter>
            <intent-filter>
                <action android:name="android.intent.action.SEND" />
                <category android:name="android.intent.category.DEFAULT" />
                <data android:mimeType="application/pdf" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```

Also create minimal resource files:

`app/src/main/res/values/styles.xml`

```xml
<resources>
    <style name="Theme.HermesStudyVault" parent="android:style/Theme.Material.Light.NoActionBar" />
</resources>
```

`app/src/main/res/xml/backup_rules.xml`

```xml
<full-backup-content />
```

`app/src/main/res/xml/data_extraction_rules.xml`

```xml
<data-extraction-rules>
    <cloud-backup>
        <include domain="database" path="." />
        <include domain="file" path="." />
    </cloud-backup>
</data-extraction-rules>
```

- [ ] **Step 8: Create Compose entry point**

Create `app/src/main/java/com/hermes/studyvault/MainActivity.kt`:

```kotlin
package com.hermes.studyvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyVaultApp()
        }
    }
}
```

Create `app/src/main/java/com/hermes/studyvault/StudyVaultApp.kt`:

```kotlin
package com.hermes.studyvault

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.hermes.studyvault.ui.theme.HermesStudyVaultTheme

@Composable
fun StudyVaultApp() {
    HermesStudyVaultTheme {
        Text("Hermes Study Vault")
    }
}
```

Create `app/src/main/java/com/hermes/studyvault/ui/theme/Color.kt`:

```kotlin
package com.hermes.studyvault.ui.theme

import androidx.compose.ui.graphics.Color

val VaultInk = Color(0xFF17201A)
val VaultPaper = Color(0xFFF8FAF6)
val VaultGreen = Color(0xFF2F6B4F)
val VaultAmber = Color(0xFFD0913D)
```

Create `app/src/main/java/com/hermes/studyvault/ui/theme/Theme.kt`:

```kotlin
package com.hermes.studyvault.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightScheme = lightColorScheme(
    primary = VaultGreen,
    secondary = VaultAmber,
    background = VaultPaper,
    onBackground = VaultInk
)

@Composable
fun HermesStudyVaultTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightScheme,
        content = content
    )
}
```

- [ ] **Step 9: Verify project skeleton builds**

Run:

```powershell
.\gradlew.bat :app:assembleDebug
```

Expected: `BUILD SUCCESSFUL`.

If the command fails because `.\gradlew.bat` is missing, run:

```powershell
gradle wrapper --gradle-version 9.4.1
.\gradlew.bat :app:assembleDebug
```

Expected after wrapper creation: `BUILD SUCCESSFUL`.

If the command fails because Android SDK is missing, install Android SDK Platform 37 and set `ANDROID_HOME`, then rerun the command.

- [ ] **Step 10: Commit**

```powershell
git add settings.gradle.kts build.gradle.kts gradle app
git commit -m "chore: create Android project skeleton"
```

---

### Task 2: Review Scheduling Domain

**Files:**
- Create: `app/src/test/java/com/hermes/studyvault/domain/review/ReviewSchedulerTest.kt`
- Create: `app/src/main/java/com/hermes/studyvault/domain/review/ReviewGrade.kt`
- Create: `app/src/main/java/com/hermes/studyvault/domain/review/ReviewScheduler.kt`

- [ ] **Step 1: Write failing review scheduler tests**

Create `app/src/test/java/com/hermes/studyvault/domain/review/ReviewSchedulerTest.kt`:

```kotlin
package com.hermes.studyvault.domain.review

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class ReviewSchedulerTest {
    private val now = Instant.parse("2026-06-16T00:00:00Z")

    @Test
    fun againSchedulesNextReviewToday() {
        val result = ReviewScheduler.nextReviewAt(now, ReviewGrade.Again, reviewCount = 2)
        assertEquals(Instant.parse("2026-06-16T00:30:00Z"), result)
    }

    @Test
    fun hardSchedulesNextReviewTomorrow() {
        val result = ReviewScheduler.nextReviewAt(now, ReviewGrade.Hard, reviewCount = 2)
        assertEquals(Instant.parse("2026-06-17T00:00:00Z"), result)
    }

    @Test
    fun goodSchedulesLongerIntervalsAsReviewCountGrows() {
        val first = ReviewScheduler.nextReviewAt(now, ReviewGrade.Good, reviewCount = 0)
        val fourth = ReviewScheduler.nextReviewAt(now, ReviewGrade.Good, reviewCount = 3)

        assertEquals(Instant.parse("2026-06-19T00:00:00Z"), first)
        assertEquals(Instant.parse("2026-07-06T00:00:00Z"), fourth)
    }

    @Test
    fun easySchedulesOneMonthWhenCardHasHistory() {
        val result = ReviewScheduler.nextReviewAt(now, ReviewGrade.Easy, reviewCount = 5)
        assertEquals(Instant.parse("2026-07-16T00:00:00Z"), result)
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests "com.hermes.studyvault.domain.review.ReviewSchedulerTest"
```

Expected: FAIL because `ReviewScheduler` and `ReviewGrade` do not exist.

- [ ] **Step 3: Implement review scheduler**

Create `app/src/main/java/com/hermes/studyvault/domain/review/ReviewGrade.kt`:

```kotlin
package com.hermes.studyvault.domain.review

enum class ReviewGrade {
    Again,
    Hard,
    Good,
    Easy
}
```

Create `app/src/main/java/com/hermes/studyvault/domain/review/ReviewScheduler.kt`:

```kotlin
package com.hermes.studyvault.domain.review

import java.time.Duration
import java.time.Instant

object ReviewScheduler {
    fun nextReviewAt(now: Instant, grade: ReviewGrade, reviewCount: Int): Instant {
        val safeCount = reviewCount.coerceAtLeast(0)
        val delay = when (grade) {
            ReviewGrade.Again -> Duration.ofMinutes(30)
            ReviewGrade.Hard -> Duration.ofDays(1)
            ReviewGrade.Good -> Duration.ofDays(
                when {
                    safeCount == 0 -> 3
                    safeCount == 1 -> 7
                    safeCount == 2 -> 14
                    else -> 20
                }
            )
            ReviewGrade.Easy -> Duration.ofDays(
                when {
                    safeCount == 0 -> 7
                    safeCount <= 2 -> 14
                    else -> 30
                }
            )
        }
        return now.plus(delay)
    }
}
```

- [ ] **Step 4: Run test to verify it passes**

Run:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests "com.hermes.studyvault.domain.review.ReviewSchedulerTest"
```

Expected: PASS.

- [ ] **Step 5: Commit**

```powershell
git add app/src/test/java/com/hermes/studyvault/domain/review app/src/main/java/com/hermes/studyvault/domain/review
git commit -m "feat: add review scheduling domain"
```

---

### Task 3: Local Room Database

**Files:**
- Create: `app/src/androidTest/java/com/hermes/studyvault/data/local/StudyVaultDatabaseInstrumentedTest.kt`
- Create: `app/src/main/java/com/hermes/studyvault/data/local/Converters.kt`
- Create: `app/src/main/java/com/hermes/studyvault/data/local/StudyVaultDatabase.kt`
- Create: `app/src/main/java/com/hermes/studyvault/data/local/entity/SourceEntity.kt`
- Create: `app/src/main/java/com/hermes/studyvault/data/local/entity/EvidenceBlockEntity.kt`
- Create: `app/src/main/java/com/hermes/studyvault/data/local/entity/NoteEntity.kt`
- Create: `app/src/main/java/com/hermes/studyvault/data/local/entity/DeadlineEntity.kt`
- Create: `app/src/main/java/com/hermes/studyvault/data/local/entity/ReviewCardEntity.kt`
- Create: `app/src/main/java/com/hermes/studyvault/data/local/entity/TagEntity.kt`
- Create: `app/src/main/java/com/hermes/studyvault/data/local/entity/CollectionEntity.kt`
- Create: `app/src/main/java/com/hermes/studyvault/data/local/dao/SourceDao.kt`
- Create: `app/src/main/java/com/hermes/studyvault/data/local/dao/EvidenceBlockDao.kt`
- Create: `app/src/main/java/com/hermes/studyvault/data/local/dao/NoteDao.kt`
- Create: `app/src/main/java/com/hermes/studyvault/data/local/dao/DeadlineDao.kt`
- Create: `app/src/main/java/com/hermes/studyvault/data/local/dao/ReviewCardDao.kt`

- [ ] **Step 1: Write failing database instrumented test**

Create `app/src/androidTest/java/com/hermes/studyvault/data/local/StudyVaultDatabaseInstrumentedTest.kt`:

```kotlin
package com.hermes.studyvault.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hermes.studyvault.data.local.entity.EvidenceBlockEntity
import com.hermes.studyvault.data.local.entity.SourceEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant

@RunWith(AndroidJUnit4::class)
class StudyVaultDatabaseInstrumentedTest {
    private lateinit var db: StudyVaultDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, StudyVaultDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun sourceAndEvidenceCanBeStoredAndRead() = runBlocking {
        val now = Instant.parse("2026-06-16T00:00:00Z")
        val source = SourceEntity(
            id = "source-1",
            title = "Research Article",
            type = "web",
            url = "https://example.com/article",
            localFilePath = null,
            capturedText = "An article about learning.",
            status = "unread",
            collectionId = null,
            createdAt = now,
            updatedAt = now
        )
        val evidence = EvidenceBlockEntity(
            id = "evidence-1",
            sourceId = "source-1",
            quoteText = "Learning improves with retrieval.",
            userThought = "This supports the review feature.",
            sourceLocation = "paragraph-4",
            tagsCsv = "learning,review",
            linkedNoteIdsCsv = "",
            linkedDeadlineIdsCsv = "",
            linkedReviewCardIdsCsv = "",
            createdAt = now,
            updatedAt = now
        )

        db.sourceDao().insert(source)
        db.evidenceBlockDao().insert(evidence)

        val sources = db.sourceDao().getAllOnce()
        val evidenceBlocks = db.evidenceBlockDao().getAllOnce()

        assertEquals(listOf(source), sources)
        assertEquals(listOf(evidence), evidenceBlocks)
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run on an emulator or connected Android device:

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.hermes.studyvault.data.local.StudyVaultDatabaseInstrumentedTest
```

Expected: FAIL because database, entities, and DAOs do not exist.

- [ ] **Step 3: Implement entities**

Create `app/src/main/java/com/hermes/studyvault/data/local/entity/SourceEntity.kt`:

```kotlin
package com.hermes.studyvault.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "sources")
data class SourceEntity(
    @PrimaryKey val id: String,
    val title: String,
    val type: String,
    val url: String?,
    val localFilePath: String?,
    val capturedText: String?,
    val status: String,
    val collectionId: String?,
    val createdAt: Instant,
    val updatedAt: Instant
)
```

Create `app/src/main/java/com/hermes/studyvault/data/local/entity/EvidenceBlockEntity.kt`:

```kotlin
package com.hermes.studyvault.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "evidence_blocks")
data class EvidenceBlockEntity(
    @PrimaryKey val id: String,
    val sourceId: String?,
    val quoteText: String,
    val userThought: String,
    val sourceLocation: String?,
    val tagsCsv: String,
    val linkedNoteIdsCsv: String,
    val linkedDeadlineIdsCsv: String,
    val linkedReviewCardIdsCsv: String,
    val createdAt: Instant,
    val updatedAt: Instant
)
```

Create `app/src/main/java/com/hermes/studyvault/data/local/entity/NoteEntity.kt`:

```kotlin
package com.hermes.studyvault.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey val id: String,
    val title: String,
    val bodyBlocksJson: String,
    val collectionId: String?,
    val tagsCsv: String,
    val createdAt: Instant,
    val updatedAt: Instant
)
```

Create `app/src/main/java/com/hermes/studyvault/data/local/entity/DeadlineEntity.kt`:

```kotlin
package com.hermes.studyvault.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "deadlines")
data class DeadlineEntity(
    @PrimaryKey val id: String,
    val title: String,
    val dueAt: Instant,
    val type: String,
    val priority: Int,
    val linkedSourceIdsCsv: String,
    val linkedNoteIdsCsv: String,
    val linkedEvidenceBlockIdsCsv: String,
    val completedAt: Instant?
)
```

Create `app/src/main/java/com/hermes/studyvault/data/local/entity/ReviewCardEntity.kt`:

```kotlin
package com.hermes.studyvault.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "review_cards")
data class ReviewCardEntity(
    @PrimaryKey val id: String,
    val front: String,
    val back: String,
    val sourceEvidenceBlockId: String?,
    val sourceNoteId: String?,
    val nextReviewAt: Instant,
    val difficulty: Int,
    val reviewCount: Int,
    val lastReviewedAt: Instant?
)
```

Create `app/src/main/java/com/hermes/studyvault/data/local/entity/TagEntity.kt`:

```kotlin
package com.hermes.studyvault.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey val id: String,
    val name: String,
    val color: String,
    val createdAt: Instant
)
```

Create `app/src/main/java/com/hermes/studyvault/data/local/entity/CollectionEntity.kt`:

```kotlin
package com.hermes.studyvault.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "collections")
data class CollectionEntity(
    @PrimaryKey val id: String,
    val name: String,
    val kind: String,
    val createdAt: Instant,
    val updatedAt: Instant
)
```

- [ ] **Step 4: Implement DAOs and database**

Create `app/src/main/java/com/hermes/studyvault/data/local/dao/SourceDao.kt`:

```kotlin
package com.hermes.studyvault.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hermes.studyvault.data.local.entity.SourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SourceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(source: SourceEntity)

    @Query("SELECT * FROM sources ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<SourceEntity>>

    @Query("SELECT * FROM sources ORDER BY createdAt DESC")
    suspend fun getAllOnce(): List<SourceEntity>

    @Query("SELECT * FROM sources WHERE status = :status ORDER BY createdAt DESC")
    fun observeByStatus(status: String): Flow<List<SourceEntity>>
}
```

Create `app/src/main/java/com/hermes/studyvault/data/local/dao/EvidenceBlockDao.kt`:

```kotlin
package com.hermes.studyvault.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hermes.studyvault.data.local.entity.EvidenceBlockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EvidenceBlockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(evidenceBlock: EvidenceBlockEntity)

    @Query("SELECT * FROM evidence_blocks ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<EvidenceBlockEntity>>

    @Query("SELECT * FROM evidence_blocks ORDER BY createdAt DESC")
    suspend fun getAllOnce(): List<EvidenceBlockEntity>
}
```

Create `app/src/main/java/com/hermes/studyvault/data/local/dao/NoteDao.kt`:

```kotlin
package com.hermes.studyvault.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hermes.studyvault.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity)

    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    suspend fun getAllOnce(): List<NoteEntity>
}
```

Create `app/src/main/java/com/hermes/studyvault/data/local/dao/DeadlineDao.kt`:

```kotlin
package com.hermes.studyvault.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hermes.studyvault.data.local.entity.DeadlineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeadlineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deadline: DeadlineEntity)

    @Query("SELECT * FROM deadlines ORDER BY dueAt ASC")
    fun observeAll(): Flow<List<DeadlineEntity>>

    @Query("SELECT * FROM deadlines ORDER BY dueAt ASC")
    suspend fun getAllOnce(): List<DeadlineEntity>
}
```

Create `app/src/main/java/com/hermes/studyvault/data/local/dao/ReviewCardDao.kt`:

```kotlin
package com.hermes.studyvault.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hermes.studyvault.data.local.entity.ReviewCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reviewCard: ReviewCardEntity)

    @Query("SELECT * FROM review_cards ORDER BY nextReviewAt ASC")
    fun observeAll(): Flow<List<ReviewCardEntity>>

    @Query("SELECT * FROM review_cards ORDER BY nextReviewAt ASC")
    suspend fun getAllOnce(): List<ReviewCardEntity>
}
```

Create `app/src/main/java/com/hermes/studyvault/data/local/Converters.kt`:

```kotlin
package com.hermes.studyvault.data.local

import androidx.room.TypeConverter
import java.time.Instant

class Converters {
    @TypeConverter
    fun instantToString(value: Instant?): String? = value?.toString()

    @TypeConverter
    fun stringToInstant(value: String?): Instant? = value?.let(Instant::parse)
}
```

Create `app/src/main/java/com/hermes/studyvault/data/local/StudyVaultDatabase.kt`:

```kotlin
package com.hermes.studyvault.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hermes.studyvault.data.local.dao.DeadlineDao
import com.hermes.studyvault.data.local.dao.EvidenceBlockDao
import com.hermes.studyvault.data.local.dao.NoteDao
import com.hermes.studyvault.data.local.dao.ReviewCardDao
import com.hermes.studyvault.data.local.dao.SourceDao
import com.hermes.studyvault.data.local.entity.CollectionEntity
import com.hermes.studyvault.data.local.entity.DeadlineEntity
import com.hermes.studyvault.data.local.entity.EvidenceBlockEntity
import com.hermes.studyvault.data.local.entity.NoteEntity
import com.hermes.studyvault.data.local.entity.ReviewCardEntity
import com.hermes.studyvault.data.local.entity.SourceEntity
import com.hermes.studyvault.data.local.entity.TagEntity

@Database(
    entities = [
        SourceEntity::class,
        EvidenceBlockEntity::class,
        NoteEntity::class,
        DeadlineEntity::class,
        ReviewCardEntity::class,
        TagEntity::class,
        CollectionEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class StudyVaultDatabase : RoomDatabase() {
    abstract fun sourceDao(): SourceDao
    abstract fun evidenceBlockDao(): EvidenceBlockDao
    abstract fun noteDao(): NoteDao
    abstract fun deadlineDao(): DeadlineDao
    abstract fun reviewCardDao(): ReviewCardDao
}
```

- [ ] **Step 5: Run database test to verify it passes**

Run:

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.hermes.studyvault.data.local.StudyVaultDatabaseInstrumentedTest
```

Expected: PASS.

- [ ] **Step 6: Commit**

```powershell
git add app/src/main/java/com/hermes/studyvault/data app/src/androidTest/java/com/hermes/studyvault/data
git commit -m "feat: add local Room database"
```

---

### Task 4: Share Capture Domain

**Files:**
- Create: `app/src/test/java/com/hermes/studyvault/domain/share/CreateSourceFromShareUseCaseTest.kt`
- Create: `app/src/main/java/com/hermes/studyvault/domain/share/SharedPayload.kt`
- Create: `app/src/main/java/com/hermes/studyvault/domain/share/CreateSourceFromShareUseCase.kt`
- Create: `app/src/main/java/com/hermes/studyvault/data/repository/VaultRepository.kt`
- Create: `app/src/main/java/com/hermes/studyvault/data/repository/RoomVaultRepository.kt`

- [ ] **Step 1: Write failing share capture test**

Create `app/src/test/java/com/hermes/studyvault/domain/share/CreateSourceFromShareUseCaseTest.kt`:

```kotlin
package com.hermes.studyvault.domain.share

import com.hermes.studyvault.data.local.entity.SourceEntity
import com.hermes.studyvault.data.repository.VaultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class CreateSourceFromShareUseCaseTest {
    @Test
    fun textShareCreatesUnreadTextSource() = runTest {
        val repository = FakeVaultRepository()
        val clock = Clock.fixed(Instant.parse("2026-06-16T00:00:00Z"), ZoneOffset.UTC)
        val useCase = CreateSourceFromShareUseCase(repository, clock)

        val source = useCase.create(
            SharedPayload.Text(
                text = "https://example.com\nImportant article",
                subject = "Example Article"
            )
        )

        assertNotNull(source.id)
        assertEquals("Example Article", source.title)
        assertEquals("text", source.type)
        assertEquals("unread", source.status)
        assertEquals(source, repository.insertedSource)
    }

    private class FakeVaultRepository : VaultRepository {
        var insertedSource: SourceEntity? = null

        override suspend fun insertSource(source: SourceEntity) {
            insertedSource = source
        }

        override fun observeSources(): Flow<List<SourceEntity>> = emptyFlow()
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests "com.hermes.studyvault.domain.share.CreateSourceFromShareUseCaseTest"
```

Expected: FAIL because repository and share use case do not exist.

- [ ] **Step 3: Implement repository interface and Room repository**

Create `app/src/main/java/com/hermes/studyvault/data/repository/VaultRepository.kt`:

```kotlin
package com.hermes.studyvault.data.repository

import com.hermes.studyvault.data.local.entity.SourceEntity
import kotlinx.coroutines.flow.Flow

interface VaultRepository {
    suspend fun insertSource(source: SourceEntity)
    fun observeSources(): Flow<List<SourceEntity>>
}
```

Create `app/src/main/java/com/hermes/studyvault/data/repository/RoomVaultRepository.kt`:

```kotlin
package com.hermes.studyvault.data.repository

import com.hermes.studyvault.data.local.StudyVaultDatabase
import com.hermes.studyvault.data.local.entity.SourceEntity
import kotlinx.coroutines.flow.Flow

class RoomVaultRepository(
    private val database: StudyVaultDatabase
) : VaultRepository {
    override suspend fun insertSource(source: SourceEntity) {
        database.sourceDao().insert(source)
    }

    override fun observeSources(): Flow<List<SourceEntity>> {
        return database.sourceDao().observeAll()
    }
}
```

- [ ] **Step 4: Implement share payload and use case**

Create `app/src/main/java/com/hermes/studyvault/domain/share/SharedPayload.kt`:

```kotlin
package com.hermes.studyvault.domain.share

import android.net.Uri

sealed interface SharedPayload {
    data class Text(val text: String, val subject: String?) : SharedPayload
    data class File(val uri: Uri, val mimeType: String?, val subject: String?) : SharedPayload
}
```

Create `app/src/main/java/com/hermes/studyvault/domain/share/CreateSourceFromShareUseCase.kt`:

```kotlin
package com.hermes.studyvault.domain.share

import com.hermes.studyvault.data.local.entity.SourceEntity
import com.hermes.studyvault.data.repository.VaultRepository
import java.time.Clock
import java.util.UUID

class CreateSourceFromShareUseCase(
    private val repository: VaultRepository,
    private val clock: Clock
) {
    suspend fun create(payload: SharedPayload): SourceEntity {
        val now = clock.instant()
        val source = when (payload) {
            is SharedPayload.Text -> SourceEntity(
                id = UUID.randomUUID().toString(),
                title = payload.subject?.takeIf { it.isNotBlank() } ?: payload.text.lineSequence().firstOrNull().orEmpty().ifBlank { "Text capture" },
                type = "text",
                url = payload.text.lineSequence().firstOrNull { it.startsWith("http://") || it.startsWith("https://") },
                localFilePath = null,
                capturedText = payload.text,
                status = "unread",
                collectionId = null,
                createdAt = now,
                updatedAt = now
            )
            is SharedPayload.File -> SourceEntity(
                id = UUID.randomUUID().toString(),
                title = payload.subject?.takeIf { it.isNotBlank() } ?: payload.uri.lastPathSegment ?: "Imported file",
                type = payload.mimeType ?: "file",
                url = null,
                localFilePath = payload.uri.toString(),
                capturedText = null,
                status = "unread",
                collectionId = null,
                createdAt = now,
                updatedAt = now
            )
        }
        repository.insertSource(source)
        return source
    }
}
```

- [ ] **Step 5: Run test to verify it passes**

Run:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests "com.hermes.studyvault.domain.share.CreateSourceFromShareUseCaseTest"
```

Expected: PASS.

- [ ] **Step 6: Commit**

```powershell
git add app/src/test/java/com/hermes/studyvault/domain/share app/src/main/java/com/hermes/studyvault/domain/share app/src/main/java/com/hermes/studyvault/data/repository
git commit -m "feat: add share capture domain"
```

---

### Task 5: Search Domain

**Files:**
- Create: `app/src/test/java/com/hermes/studyvault/domain/search/SearchVaultUseCaseTest.kt`
- Create: `app/src/main/java/com/hermes/studyvault/domain/search/SearchVaultUseCase.kt`
- Modify: `app/src/main/java/com/hermes/studyvault/data/repository/VaultRepository.kt`
- Modify: `app/src/main/java/com/hermes/studyvault/data/repository/RoomVaultRepository.kt`

- [ ] **Step 1: Write failing search test**

Create `app/src/test/java/com/hermes/studyvault/domain/search/SearchVaultUseCaseTest.kt`:

```kotlin
package com.hermes.studyvault.domain.search

import com.hermes.studyvault.data.local.entity.EvidenceBlockEntity
import com.hermes.studyvault.data.local.entity.NoteEntity
import com.hermes.studyvault.data.local.entity.SourceEntity
import com.hermes.studyvault.data.repository.VaultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class SearchVaultUseCaseTest {
    @Test
    fun searchFindsSourcesEvidenceAndNotesCaseInsensitively() = runTest {
        val repository = FakeVaultRepository()
        val useCase = SearchVaultUseCase(repository)

        val result = useCase.search("retrieval")

        assertEquals(
            listOf("source:source-1", "evidence:evidence-1", "note:note-1"),
            result.map { "${it.type}:${it.id}" }
        )
    }

    private class FakeVaultRepository : VaultRepository {
        private val now = Instant.parse("2026-06-16T00:00:00Z")

        override suspend fun insertSource(source: SourceEntity) = Unit
        override fun observeSources(): Flow<List<SourceEntity>> = flowOf(emptyList())

        override suspend fun getSourcesOnce(): List<SourceEntity> = listOf(
            SourceEntity("source-1", "Retrieval practice", "web", null, null, null, "unread", null, now, now)
        )

        override suspend fun getEvidenceBlocksOnce(): List<EvidenceBlockEntity> = listOf(
            EvidenceBlockEntity("evidence-1", "source-1", "retrieval improves memory", "", "p4", "", "", "", "", now, now)
        )

        override suspend fun getNotesOnce(): List<NoteEntity> = listOf(
            NoteEntity("note-1", "Exam plan", """[{"text":"Use retrieval cards"}]""", null, "study", now, now)
        )
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests "com.hermes.studyvault.domain.search.SearchVaultUseCaseTest"
```

Expected: FAIL because search use case and repository methods do not exist.

- [ ] **Step 3: Extend repository interface and implementation**

Modify `VaultRepository.kt`:

```kotlin
package com.hermes.studyvault.data.repository

import com.hermes.studyvault.data.local.entity.EvidenceBlockEntity
import com.hermes.studyvault.data.local.entity.NoteEntity
import com.hermes.studyvault.data.local.entity.SourceEntity
import kotlinx.coroutines.flow.Flow

interface VaultRepository {
    suspend fun insertSource(source: SourceEntity)
    fun observeSources(): Flow<List<SourceEntity>>
    suspend fun getSourcesOnce(): List<SourceEntity>
    suspend fun getEvidenceBlocksOnce(): List<EvidenceBlockEntity>
    suspend fun getNotesOnce(): List<NoteEntity>
}
```

Modify `RoomVaultRepository.kt`:

```kotlin
package com.hermes.studyvault.data.repository

import com.hermes.studyvault.data.local.StudyVaultDatabase
import com.hermes.studyvault.data.local.entity.EvidenceBlockEntity
import com.hermes.studyvault.data.local.entity.NoteEntity
import com.hermes.studyvault.data.local.entity.SourceEntity
import kotlinx.coroutines.flow.Flow

class RoomVaultRepository(
    private val database: StudyVaultDatabase
) : VaultRepository {
    override suspend fun insertSource(source: SourceEntity) {
        database.sourceDao().insert(source)
    }

    override fun observeSources(): Flow<List<SourceEntity>> {
        return database.sourceDao().observeAll()
    }

    override suspend fun getSourcesOnce(): List<SourceEntity> = database.sourceDao().getAllOnce()

    override suspend fun getEvidenceBlocksOnce(): List<EvidenceBlockEntity> {
        return database.evidenceBlockDao().getAllOnce()
    }

    override suspend fun getNotesOnce(): List<NoteEntity> {
        return database.noteDao().getAllOnce()
    }
}
```

- [ ] **Step 4: Implement search use case**

Create `app/src/main/java/com/hermes/studyvault/domain/search/SearchVaultUseCase.kt`:

```kotlin
package com.hermes.studyvault.domain.search

import com.hermes.studyvault.data.repository.VaultRepository

data class SearchResult(
    val id: String,
    val type: String,
    val title: String,
    val preview: String
)

class SearchVaultUseCase(
    private val repository: VaultRepository
) {
    suspend fun search(query: String): List<SearchResult> {
        val normalized = query.trim().lowercase()
        if (normalized.isEmpty()) return emptyList()

        val sourceResults = repository.getSourcesOnce()
            .filter { source ->
                source.title.contains(normalized, ignoreCase = true) ||
                    source.capturedText.orEmpty().contains(normalized, ignoreCase = true) ||
                    source.url.orEmpty().contains(normalized, ignoreCase = true)
            }
            .map { source ->
                SearchResult(
                    id = source.id,
                    type = "source",
                    title = source.title,
                    preview = source.capturedText ?: source.url ?: source.type
                )
            }

        val evidenceResults = repository.getEvidenceBlocksOnce()
            .filter { evidence ->
                evidence.quoteText.contains(normalized, ignoreCase = true) ||
                    evidence.userThought.contains(normalized, ignoreCase = true) ||
                    evidence.tagsCsv.contains(normalized, ignoreCase = true)
            }
            .map { evidence ->
                SearchResult(
                    id = evidence.id,
                    type = "evidence",
                    title = evidence.quoteText.take(48),
                    preview = evidence.userThought
                )
            }

        val noteResults = repository.getNotesOnce()
            .filter { note ->
                note.title.contains(normalized, ignoreCase = true) ||
                    note.bodyBlocksJson.contains(normalized, ignoreCase = true) ||
                    note.tagsCsv.contains(normalized, ignoreCase = true)
            }
            .map { note ->
                SearchResult(
                    id = note.id,
                    type = "note",
                    title = note.title,
                    preview = note.tagsCsv
                )
            }

        return sourceResults + evidenceResults + noteResults
    }
}
```

- [ ] **Step 5: Run test to verify it passes**

Run:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests "com.hermes.studyvault.domain.search.SearchVaultUseCaseTest"
```

Expected: PASS.

- [ ] **Step 6: Commit**

```powershell
git add app/src/test/java/com/hermes/studyvault/domain/search app/src/main/java/com/hermes/studyvault/domain/search app/src/main/java/com/hermes/studyvault/data/repository
git commit -m "feat: add local vault search"
```

---

### Task 6: Today Overview Domain

**Files:**
- Create: `app/src/test/java/com/hermes/studyvault/domain/today/GetTodayOverviewUseCaseTest.kt`
- Create: `app/src/main/java/com/hermes/studyvault/domain/today/TodayOverview.kt`
- Create: `app/src/main/java/com/hermes/studyvault/domain/today/GetTodayOverviewUseCase.kt`
- Modify: `app/src/main/java/com/hermes/studyvault/data/repository/VaultRepository.kt`
- Modify: `app/src/main/java/com/hermes/studyvault/data/repository/RoomVaultRepository.kt`

- [ ] **Step 1: Write failing Today overview test**

Create `app/src/test/java/com/hermes/studyvault/domain/today/GetTodayOverviewUseCaseTest.kt`:

```kotlin
package com.hermes.studyvault.domain.today

import com.hermes.studyvault.data.local.entity.DeadlineEntity
import com.hermes.studyvault.data.local.entity.ReviewCardEntity
import com.hermes.studyvault.data.local.entity.SourceEntity
import com.hermes.studyvault.data.repository.VaultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class GetTodayOverviewUseCaseTest {
    @Test
    fun overviewIncludesUnreadSourcesDueReviewsAndUpcomingDeadlines() = runTest {
        val now = Instant.parse("2026-06-16T09:00:00Z")
        val overview = GetTodayOverviewUseCase(FakeVaultRepository()).get(now)

        assertEquals(1, overview.unreadSources.size)
        assertEquals(1, overview.dueReviewCards.size)
        assertEquals(1, overview.upcomingDeadlines.size)
    }

    private class FakeVaultRepository : VaultRepository {
        private val now = Instant.parse("2026-06-16T09:00:00Z")

        override suspend fun insertSource(source: SourceEntity) = Unit
        override fun observeSources(): Flow<List<SourceEntity>> = flowOf(emptyList())
        override suspend fun getSourcesOnce(): List<SourceEntity> = listOf(
            SourceEntity("source-1", "Article", "web", null, null, null, "unread", null, now, now),
            SourceEntity("source-2", "Archived", "web", null, null, null, "archived", null, now, now)
        )
        override suspend fun getEvidenceBlocksOnce() = emptyList<com.hermes.studyvault.data.local.entity.EvidenceBlockEntity>()
        override suspend fun getNotesOnce() = emptyList<com.hermes.studyvault.data.local.entity.NoteEntity>()
        override suspend fun getDeadlinesOnce(): List<DeadlineEntity> = listOf(
            DeadlineEntity("deadline-1", "Paper review", Instant.parse("2026-06-18T23:59:00Z"), "paper_review", 2, "", "", "", null)
        )
        override suspend fun getReviewCardsOnce(): List<ReviewCardEntity> = listOf(
            ReviewCardEntity("card-1", "What is retrieval?", "Active recall", null, null, Instant.parse("2026-06-16T08:00:00Z"), 2, 1, null)
        )
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests "com.hermes.studyvault.domain.today.GetTodayOverviewUseCaseTest"
```

Expected: FAIL because Today domain and repository methods do not exist.

- [ ] **Step 3: Extend repository methods**

Add to `VaultRepository.kt`:

```kotlin
suspend fun getDeadlinesOnce(): List<DeadlineEntity>
suspend fun getReviewCardsOnce(): List<ReviewCardEntity>
```

Add matching imports:

```kotlin
import com.hermes.studyvault.data.local.entity.DeadlineEntity
import com.hermes.studyvault.data.local.entity.ReviewCardEntity
```

Add to `RoomVaultRepository.kt`:

```kotlin
override suspend fun getDeadlinesOnce(): List<DeadlineEntity> = database.deadlineDao().getAllOnce()

override suspend fun getReviewCardsOnce(): List<ReviewCardEntity> {
    return database.reviewCardDao().getAllOnce()
}
```

- [ ] **Step 4: Implement Today overview use case**

Create `app/src/main/java/com/hermes/studyvault/domain/today/TodayOverview.kt`:

```kotlin
package com.hermes.studyvault.domain.today

import com.hermes.studyvault.data.local.entity.DeadlineEntity
import com.hermes.studyvault.data.local.entity.ReviewCardEntity
import com.hermes.studyvault.data.local.entity.SourceEntity

data class TodayOverview(
    val unreadSources: List<SourceEntity>,
    val dueReviewCards: List<ReviewCardEntity>,
    val upcomingDeadlines: List<DeadlineEntity>
)
```

Create `app/src/main/java/com/hermes/studyvault/domain/today/GetTodayOverviewUseCase.kt`:

```kotlin
package com.hermes.studyvault.domain.today

import com.hermes.studyvault.data.repository.VaultRepository
import java.time.Duration
import java.time.Instant

class GetTodayOverviewUseCase(
    private val repository: VaultRepository
) {
    suspend fun get(now: Instant): TodayOverview {
        val deadlineWindowEnd = now.plus(Duration.ofDays(7))
        return TodayOverview(
            unreadSources = repository.getSourcesOnce().filter { it.status == "unread" },
            dueReviewCards = repository.getReviewCardsOnce().filter { !it.nextReviewAt.isAfter(now) },
            upcomingDeadlines = repository.getDeadlinesOnce()
                .filter { it.completedAt == null && !it.dueAt.isBefore(now) && !it.dueAt.isAfter(deadlineWindowEnd) }
                .sortedBy { it.dueAt }
        )
    }
}
```

- [ ] **Step 5: Run test to verify it passes**

Run:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests "com.hermes.studyvault.domain.today.GetTodayOverviewUseCaseTest"
```

Expected: PASS.

- [ ] **Step 6: Commit**

```powershell
git add app/src/test/java/com/hermes/studyvault/domain/today app/src/main/java/com/hermes/studyvault/domain/today app/src/main/java/com/hermes/studyvault/data
git commit -m "feat: add Today overview domain"
```

---

### Task 7: Markdown Export

**Files:**
- Create: `app/src/test/java/com/hermes/studyvault/domain/export/MarkdownExporterTest.kt`
- Create: `app/src/main/java/com/hermes/studyvault/domain/export/MarkdownExporter.kt`

- [ ] **Step 1: Write failing Markdown export test**

Create `app/src/test/java/com/hermes/studyvault/domain/export/MarkdownExporterTest.kt`:

```kotlin
package com.hermes.studyvault.domain.export

import com.hermes.studyvault.data.local.entity.EvidenceBlockEntity
import com.hermes.studyvault.data.local.entity.NoteEntity
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class MarkdownExporterTest {
    @Test
    fun noteExportIncludesEvidenceAndSourceLocation() {
        val now = Instant.parse("2026-06-16T00:00:00Z")
        val note = NoteEntity(
            id = "note-1",
            title = "Memory Study",
            bodyBlocksJson = """[{"type":"paragraph","text":"Draft body"}]""",
            collectionId = null,
            tagsCsv = "psychology",
            createdAt = now,
            updatedAt = now
        )
        val evidence = listOf(
            EvidenceBlockEntity("ev-1", "source-1", "Retrieval helps memory.", "Use this in intro.", "p.4", "memory", "note-1", "", "", now, now)
        )

        val markdown = MarkdownExporter.exportNote(note, evidence)

        assertTrue(markdown.contains("# Memory Study"))
        assertTrue(markdown.contains("Draft body"))
        assertTrue(markdown.contains("> Retrieval helps memory."))
        assertTrue(markdown.contains("Source location: p.4"))
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests "com.hermes.studyvault.domain.export.MarkdownExporterTest"
```

Expected: FAIL because `MarkdownExporter` does not exist.

- [ ] **Step 3: Implement Markdown exporter**

Create `app/src/main/java/com/hermes/studyvault/domain/export/MarkdownExporter.kt`:

```kotlin
package com.hermes.studyvault.domain.export

import com.hermes.studyvault.data.local.entity.EvidenceBlockEntity
import com.hermes.studyvault.data.local.entity.NoteEntity

object MarkdownExporter {
    fun exportNote(note: NoteEntity, evidenceBlocks: List<EvidenceBlockEntity>): String {
        val bodyText = note.bodyBlocksJson
            .replace("[", "")
            .replace("]", "")
            .replace("{", "")
            .replace("}", "")
            .replace("\"type\":\"paragraph\",", "")
            .replace("\"text\":\"", "")
            .replace("\"", "")

        val evidenceMarkdown = evidenceBlocks
            .filter { evidence -> evidence.linkedNoteIdsCsv.split(",").map { it.trim() }.contains(note.id) }
            .joinToString(separator = "\n\n") { evidence ->
                buildString {
                    appendLine("> ${evidence.quoteText}")
                    if (evidence.userThought.isNotBlank()) appendLine()
                    if (evidence.userThought.isNotBlank()) appendLine(evidence.userThought)
                    if (!evidence.sourceLocation.isNullOrBlank()) appendLine("Source location: ${evidence.sourceLocation}")
                }.trim()
            }

        return buildString {
            appendLine("# ${note.title}")
            appendLine()
            appendLine(bodyText)
            if (evidenceMarkdown.isNotBlank()) {
                appendLine()
                appendLine("## Evidence")
                appendLine()
                appendLine(evidenceMarkdown)
            }
        }.trimEnd()
    }
}
```

- [ ] **Step 4: Run test to verify it passes**

Run:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests "com.hermes.studyvault.domain.export.MarkdownExporterTest"
```

Expected: PASS.

- [ ] **Step 5: Commit**

```powershell
git add app/src/test/java/com/hermes/studyvault/domain/export app/src/main/java/com/hermes/studyvault/domain/export
git commit -m "feat: add Markdown note export"
```

---

### Task 8: JSON Backup Export

**Files:**
- Create: `app/src/test/java/com/hermes/studyvault/domain/export/JsonBackupExporterTest.kt`
- Create: `app/src/main/java/com/hermes/studyvault/domain/export/JsonBackupExporter.kt`

- [ ] **Step 1: Write failing JSON backup test**

Create `app/src/test/java/com/hermes/studyvault/domain/export/JsonBackupExporterTest.kt`:

```kotlin
package com.hermes.studyvault.domain.export

import com.hermes.studyvault.data.local.entity.SourceEntity
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class JsonBackupExporterTest {
    @Test
    fun backupIncludesSourceFields() {
        val now = Instant.parse("2026-06-16T00:00:00Z")
        val source = SourceEntity("source-1", "Article", "web", "https://example.com", null, "text", "unread", null, now, now)

        val json = JsonBackupExporter.exportSources(listOf(source))

        assertTrue(json.contains("\"id\":\"source-1\""))
        assertTrue(json.contains("\"title\":\"Article\""))
        assertTrue(json.contains("\"url\":\"https://example.com\""))
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests "com.hermes.studyvault.domain.export.JsonBackupExporterTest"
```

Expected: FAIL because `JsonBackupExporter` does not exist.

- [ ] **Step 3: Implement JSON backup exporter**

Create `app/src/main/java/com/hermes/studyvault/domain/export/JsonBackupExporter.kt`:

```kotlin
package com.hermes.studyvault.domain.export

import com.hermes.studyvault.data.local.entity.SourceEntity

object JsonBackupExporter {
    fun exportSources(sources: List<SourceEntity>): String {
        return sources.joinToString(prefix = """{"sources":[""", separator = ",", postfix = "]}") { source ->
            buildString {
                append("{")
                appendJsonField("id", source.id)
                append(",")
                appendJsonField("title", source.title)
                append(",")
                appendJsonField("type", source.type)
                append(",")
                appendJsonField("url", source.url)
                append(",")
                appendJsonField("status", source.status)
                append("}")
            }
        }
    }

    private fun StringBuilder.appendJsonField(name: String, value: String?) {
        append("\"")
        append(escape(name))
        append("\":")
        if (value == null) {
            append("null")
        } else {
            append("\"")
            append(escape(value))
            append("\"")
        }
    }

    private fun escape(value: String): String {
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
    }
}
```

- [ ] **Step 4: Run test to verify it passes**

Run:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests "com.hermes.studyvault.domain.export.JsonBackupExporterTest"
```

Expected: PASS.

- [ ] **Step 5: Commit**

```powershell
git add app/src/test/java/com/hermes/studyvault/domain/export app/src/main/java/com/hermes/studyvault/domain/export
git commit -m "feat: add JSON backup export"
```

---

### Task 9: Navigation and Main Screens

**Files:**
- Create: `app/src/androidTest/java/com/hermes/studyvault/navigation/VaultNavigationInstrumentedTest.kt`
- Modify: `app/src/main/java/com/hermes/studyvault/StudyVaultApp.kt`
- Create: `app/src/main/java/com/hermes/studyvault/navigation/VaultRoute.kt`
- Create: `app/src/main/java/com/hermes/studyvault/navigation/VaultNavHost.kt`
- Create: `app/src/main/java/com/hermes/studyvault/ui/components/VaultBottomBar.kt`
- Create: `app/src/main/java/com/hermes/studyvault/ui/today/TodayScreen.kt`
- Create: `app/src/main/java/com/hermes/studyvault/ui/inbox/InboxScreen.kt`
- Create: `app/src/main/java/com/hermes/studyvault/ui/vault/VaultScreen.kt`
- Create: `app/src/main/java/com/hermes/studyvault/ui/write/WriteScreen.kt`
- Create: `app/src/main/java/com/hermes/studyvault/ui/review/ReviewScreen.kt`

- [ ] **Step 1: Write failing navigation UI test**

Create `app/src/androidTest/java/com/hermes/studyvault/navigation/VaultNavigationInstrumentedTest.kt`:

```kotlin
package com.hermes.studyvault.navigation

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hermes.studyvault.MainActivity
import org.junit.Rule
import org.junit.Test

class VaultNavigationInstrumentedTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun bottomTabsNavigateBetweenMainScreens() {
        composeRule.onNodeWithText("Today").assertExists()
        composeRule.onNodeWithText("Inbox").performClick()
        composeRule.onNodeWithText("Capture Inbox").assertExists()
        composeRule.onNodeWithText("Vault").performClick()
        composeRule.onNodeWithText("Knowledge Vault").assertExists()
        composeRule.onNodeWithText("Write").performClick()
        composeRule.onNodeWithText("Write Note").assertExists()
        composeRule.onNodeWithText("Review").performClick()
        composeRule.onNodeWithText("Review Queue").assertExists()
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run:

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.hermes.studyvault.navigation.VaultNavigationInstrumentedTest
```

Expected: FAIL because navigation and screen content do not exist.

- [ ] **Step 3: Implement route model and bottom bar**

Create `app/src/main/java/com/hermes/studyvault/navigation/VaultRoute.kt`:

```kotlin
package com.hermes.studyvault.navigation

enum class VaultRoute(val route: String, val label: String) {
    Today("today", "Today"),
    Inbox("inbox", "Inbox"),
    Vault("vault", "Vault"),
    Write("write", "Write"),
    Review("review", "Review")
}
```

Create `app/src/main/java/com/hermes/studyvault/ui/components/VaultBottomBar.kt`:

```kotlin
package com.hermes.studyvault.ui.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.hermes.studyvault.navigation.VaultRoute

@Composable
fun VaultBottomBar(
    currentRoute: String?,
    onRouteSelected: (VaultRoute) -> Unit
) {
    NavigationBar {
        VaultRoute.entries.forEach { route ->
            NavigationBarItem(
                selected = currentRoute == route.route,
                onClick = { onRouteSelected(route) },
                icon = { Text(route.label.take(1)) },
                label = { Text(route.label) }
            )
        }
    }
}
```

- [ ] **Step 4: Implement first-pass screen content**

Create `app/src/main/java/com/hermes/studyvault/ui/today/TodayScreen.kt`:

```kotlin
package com.hermes.studyvault.ui.today

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TodayScreen() {
    Column(Modifier.padding(16.dp)) {
        Text("Today", style = MaterialTheme.typography.headlineMedium)
        Text("Reviews, readings, deadlines, and recent captures.")
    }
}
```

Create `app/src/main/java/com/hermes/studyvault/ui/inbox/InboxScreen.kt`:

```kotlin
package com.hermes.studyvault.ui.inbox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InboxScreen() {
    Column(Modifier.padding(16.dp)) {
        Text("Capture Inbox", style = MaterialTheme.typography.headlineMedium)
        Text("Shared links, files, images, and text waiting to be processed.")
    }
}
```

Create `app/src/main/java/com/hermes/studyvault/ui/vault/VaultScreen.kt`:

```kotlin
package com.hermes.studyvault.ui.vault

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VaultScreen() {
    Column(Modifier.padding(16.dp)) {
        Text("Knowledge Vault", style = MaterialTheme.typography.headlineMedium)
        Text("Search sources, evidence, notes, tags, and collections.")
    }
}
```

Create `app/src/main/java/com/hermes/studyvault/ui/write/WriteScreen.kt`:

```kotlin
package com.hermes.studyvault.ui.write

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WriteScreen() {
    Column(Modifier.padding(16.dp)) {
        Text("Write Note", style = MaterialTheme.typography.headlineMedium)
        Text("Draft notes and insert evidence blocks.")
    }
}
```

Create `app/src/main/java/com/hermes/studyvault/ui/review/ReviewScreen.kt`:

```kotlin
package com.hermes.studyvault.ui.review

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReviewScreen() {
    Column(Modifier.padding(16.dp)) {
        Text("Review Queue", style = MaterialTheme.typography.headlineMedium)
        Text("Study due cards and deadline-linked material.")
    }
}
```

- [ ] **Step 5: Implement NavHost and app composition**

Create `app/src/main/java/com/hermes/studyvault/navigation/VaultNavHost.kt`:

```kotlin
package com.hermes.studyvault.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hermes.studyvault.ui.components.VaultBottomBar
import com.hermes.studyvault.ui.inbox.InboxScreen
import com.hermes.studyvault.ui.review.ReviewScreen
import com.hermes.studyvault.ui.today.TodayScreen
import com.hermes.studyvault.ui.vault.VaultScreen
import com.hermes.studyvault.ui.write.WriteScreen

@Composable
fun VaultNavHost() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            VaultBottomBar(currentRoute = currentRoute) { route ->
                navController.navigate(route.route) {
                    popUpTo(VaultRoute.Today.route)
                    launchSingleTop = true
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = VaultRoute.Today.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(VaultRoute.Today.route) { TodayScreen() }
            composable(VaultRoute.Inbox.route) { InboxScreen() }
            composable(VaultRoute.Vault.route) { VaultScreen() }
            composable(VaultRoute.Write.route) { WriteScreen() }
            composable(VaultRoute.Review.route) { ReviewScreen() }
        }
    }
}
```

Modify `StudyVaultApp.kt`:

```kotlin
package com.hermes.studyvault

import androidx.compose.runtime.Composable
import com.hermes.studyvault.navigation.VaultNavHost
import com.hermes.studyvault.ui.theme.HermesStudyVaultTheme

@Composable
fun StudyVaultApp() {
    HermesStudyVaultTheme {
        VaultNavHost()
    }
}
```

- [ ] **Step 6: Run navigation test to verify it passes**

Run:

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.hermes.studyvault.navigation.VaultNavigationInstrumentedTest
```

Expected: PASS.

- [ ] **Step 7: Commit**

```powershell
git add app/src/main/java/com/hermes/studyvault app/src/androidTest/java/com/hermes/studyvault/navigation
git commit -m "feat: add MVP navigation screens"
```

---

### Task 10: Final MVP Verification Pass

**Files:**
- Modify: `docs/superpowers/specs/2026-06-16-hermes-study-vault-design.md` only if verification reveals a documented requirement mismatch.
- Modify: `docs/superpowers/plans/2026-06-16-hermes-study-vault-android-mvp.md` only if command names or paths changed during execution.

- [ ] **Step 1: Run unit tests**

Run:

```powershell
.\gradlew.bat :app:testDebugUnitTest
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 2: Run instrumented tests**

Run with an emulator or connected Android device:

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 3: Build debug APK**

Run:

```powershell
.\gradlew.bat :app:assembleDebug
```

Expected: `BUILD SUCCESSFUL` and APK generated under `app/build/outputs/apk/debug/`.

- [ ] **Step 4: Inspect git status**

Run:

```powershell
git status --short
```

Expected: no output.

If generated files such as Room schemas are intentionally tracked, add and commit them before this step passes.

- [ ] **Step 5: Commit verification adjustments**

If Step 4 shows only intentional documentation or schema changes:

```powershell
git add docs app/schemas
git commit -m "chore: finalize MVP verification"
```

Expected: commit succeeds.

If Step 4 has no output, skip this commit.

## Self-Review Notes

Spec coverage:

- Product is Android phone first: covered by Task 1.
- Local-first storage: covered by Task 3.
- Source capture: covered by Task 4.
- Evidence data model: covered by Task 3.
- Search: covered by Task 5.
- Today view domain: covered by Task 6.
- Markdown export: covered by Task 7.
- JSON backup export: covered by Task 8.
- Bottom tabs and MVP screens: covered by Task 9.
- Final verification: covered by Task 10.

Known implementation risks:

- Android SDK is not currently configured in the shell environment.
- Task 1 must end with a successful Gradle build before any app feature work starts.
- The Markdown exporter in Task 7 is intentionally simple; it only supports the MVP paragraph JSON shape used in its test.
