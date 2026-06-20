package com.hermes.studyvault.release

import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReleaseConfigurationTest {
    @Test
    fun manifestUsesLocalizedAppNameForLauncherLabel() {
        val manifest = projectFile("src/main/AndroidManifest.xml", "app/src/main/AndroidManifest.xml")
        val document = DocumentBuilderFactory.newInstance()
            .apply { isNamespaceAware = true }
            .newDocumentBuilder()
            .parse(manifest)
        val application = document.getElementsByTagName("application").item(0)
        val label = application.attributes.getNamedItemNS(AndroidNamespace, "label").nodeValue

        assertEquals("@string/app_name", label)
    }

    @Test
    fun appTargetsCurrentGooglePlayApiRequirement() {
        val buildFile = projectFile("build.gradle.kts", "app/build.gradle.kts")
        val source = buildFile.readText()
        val targetSdk = Regex("""targetSdk\s*=\s*(\d+)""")
            .find(source)
            ?.groupValues
            ?.get(1)
            ?.toInt()

        assertTrue("targetSdk must be Android 15/API 35 or higher for current Play submissions.", targetSdk != null && targetSdk >= 35)
    }

    @Test
    fun firstPublicReleaseUsesProductionVersionName() {
        val buildFile = projectFile("build.gradle.kts", "app/build.gradle.kts")
        val source = buildFile.readText()

        assertTrue(source.contains("""versionCode = 1"""))
        assertTrue(source.contains("""versionName = "1.0.0""""))
    }

    @Test
    fun releaseSigningCanBeConfiguredWithoutCommittingSecrets() {
        val buildFile = projectFile("build.gradle.kts", "app/build.gradle.kts")
        val source = buildFile.readText()

        assertTrue(source.contains("HERMES_RELEASE_STORE_FILE"))
        assertTrue(source.contains("HERMES_RELEASE_STORE_PASSWORD"))
        assertTrue(source.contains("HERMES_RELEASE_KEY_ALIAS"))
        assertTrue(source.contains("HERMES_RELEASE_KEY_PASSWORD"))
        assertTrue(source.contains("""create("release")"""))
        assertTrue(source.contains("signingConfig = signingConfigs.getByName(\"release\")"))
    }

    @Test
    fun admobUsesRegisteredAppAndBannerIdsWithDebugTestAds() {
        val buildFile = projectFile("build.gradle.kts", "app/build.gradle.kts")
        val buildSource = buildFile.readText()
        val versionCatalog = projectFile("../gradle/libs.versions.toml", "gradle/libs.versions.toml")
        val catalogSource = versionCatalog.readText()
        val manifest = projectFile("src/main/AndroidManifest.xml", "app/src/main/AndroidManifest.xml")
        val manifestSource = manifest.readText()

        assertTrue(catalogSource.contains("play-services-ads"))
        assertTrue(buildSource.contains("libs.google.play.services.ads"))
        assertTrue(buildSource.contains("ca-app-pub-4402708884038037/8784463877"))
        assertTrue(buildSource.contains("ca-app-pub-3940256099942544/6300978111"))
        assertTrue(manifestSource.contains("com.google.android.gms.ads.APPLICATION_ID"))
        assertTrue(manifestSource.contains("ca-app-pub-4402708884038037~2410627211"))
    }

    @Test
    fun appInitializesMobileAdsAndBannerSlotLoadsAdMobBanner() {
        val activity = projectFile(
            "src/main/java/com/hermes/studyvault/MainActivity.kt",
            "app/src/main/java/com/hermes/studyvault/MainActivity.kt",
        )
        val activitySource = activity.readText()
        val bannerSlot = projectFile(
            "src/main/java/com/hermes/studyvault/ui/ads/AdBannerSlot.kt",
            "app/src/main/java/com/hermes/studyvault/ui/ads/AdBannerSlot.kt",
        )
        val bannerSource = bannerSlot.readText()

        assertTrue(activitySource.contains("MobileAds.initialize"))
        assertTrue(activitySource.contains("BuildConfig.ADS_ENABLED"))
        assertTrue(activitySource.contains("BuildConfig.ADMOB_BANNER_AD_UNIT_ID"))
        assertTrue(bannerSource.contains("AdView"))
        assertTrue(bannerSource.contains("AdRequest.Builder"))
        assertTrue(bannerSource.contains("adUnitId"))
    }

    private fun projectFile(vararg candidates: String): File {
        return candidates
            .map(::File)
            .firstOrNull(File::isFile)
            ?: error("Could not locate project file from candidates: ${candidates.joinToString()}")
    }

    private companion object {
        const val AndroidNamespace = "http://schemas.android.com/apk/res/android"
    }
}
