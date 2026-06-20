package com.hermes.studyvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hermes.studyvault.notifications.DeadlineReminderScheduler
import com.google.android.gms.ads.MobileAds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DeadlineReminderScheduler.createNotificationChannel(this)
        if (BuildConfig.ADS_ENABLED) {
            MobileAds.initialize(this) {}
        }
        setContent {
            StudyVaultApp(
                adsEnabled = BuildConfig.ADS_ENABLED,
                bannerAdUnitId = BuildConfig.ADMOB_BANNER_AD_UNIT_ID,
            )
        }
    }
}
