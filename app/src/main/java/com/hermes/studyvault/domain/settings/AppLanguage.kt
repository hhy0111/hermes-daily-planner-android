package com.hermes.studyvault.domain.settings

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val localeTag: String,
) {
    SystemDefault("system", "System Default", ""),
    English("en", "English", "en"),
    Korean("ko", "한국어", "ko"),
    Japanese("ja", "日本語", "ja"),
    SimplifiedChinese("zh-CN", "简体中文", "zh-CN"),
    TraditionalChinese("zh-TW", "繁體中文", "zh-TW"),
    French("fr", "Français", "fr"),
    Spanish("es", "Español", "es"),
    German("de", "Deutsch", "de"),
    Italian("it", "Italiano", "it"),
    Portuguese("pt", "Português", "pt"),
    Russian("ru", "Русский", "ru"),
    Arabic("ar", "العربية", "ar"),
    Hindi("hi", "हिन्दी", "hi"),
    Indonesian("id", "Bahasa Indonesia", "id"),
    Vietnamese("vi", "Tiếng Việt", "vi"),
    Thai("th", "ไทย", "th"),
    Turkish("tr", "Türkçe", "tr");

    companion object {
        fun fromCode(code: String?): AppLanguage {
            return entries.firstOrNull { language -> language.code == code } ?: SystemDefault
        }
    }
}
