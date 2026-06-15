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
