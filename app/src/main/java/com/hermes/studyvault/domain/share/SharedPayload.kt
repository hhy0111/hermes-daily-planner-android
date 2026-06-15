package com.hermes.studyvault.domain.share

import android.net.Uri

sealed interface SharedPayload {
    data class Text(val text: String, val subject: String?) : SharedPayload
    data class File(val uri: Uri, val mimeType: String?, val subject: String?) : SharedPayload
}
