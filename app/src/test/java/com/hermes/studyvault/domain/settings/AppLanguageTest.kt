package com.hermes.studyvault.domain.settings

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppLanguageTest {
    @Test
    fun supportedLanguagesIncludeSystemDefaultAndMajorLanguages() {
        val codes = AppLanguage.entries.map { language -> language.code }

        assertEquals(
            listOf(
                "system",
                "en",
                "ko",
                "ja",
                "zh-CN",
                "zh-TW",
                "fr",
                "es",
                "de",
                "it",
                "pt",
                "ru",
                "ar",
                "hi",
                "id",
                "vi",
                "th",
                "tr",
            ),
            codes,
        )
    }

    @Test
    fun languageMetadataUsesNativeDisplayNamesAndLocaleTags() {
        assertEquals("System Default", AppLanguage.SystemDefault.displayName)
        assertEquals("", AppLanguage.SystemDefault.localeTag)
        assertEquals("한국어", AppLanguage.Korean.displayName)
        assertEquals("ko", AppLanguage.Korean.localeTag)
        assertEquals("简体中文", AppLanguage.SimplifiedChinese.displayName)
        assertEquals("zh-CN", AppLanguage.SimplifiedChinese.localeTag)
        assertEquals("繁體中文", AppLanguage.TraditionalChinese.displayName)
        assertEquals("zh-TW", AppLanguage.TraditionalChinese.localeTag)
    }

    @Test
    fun fromCodeFallsBackToSystemDefaultForUnknownCodes() {
        assertEquals(AppLanguage.English, AppLanguage.fromCode("en"))
        assertEquals(AppLanguage.Korean, AppLanguage.fromCode("ko"))
        assertEquals(AppLanguage.SystemDefault, AppLanguage.fromCode("missing"))
        assertEquals(AppLanguage.SystemDefault, AppLanguage.fromCode(null))
    }

    @Test
    fun everyLanguageCodeIsUnique() {
        val codes = AppLanguage.entries.map { language -> language.code }

        assertTrue(codes.size == codes.toSet().size)
    }
}
