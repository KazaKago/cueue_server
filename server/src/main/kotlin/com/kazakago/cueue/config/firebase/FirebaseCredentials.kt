package com.kazakago.cueue.config.firebase

import org.apache.commons.codec.binary.Base64
import java.io.InputStream

data class FirebaseCredentials(private val credentials: String) {

    val inputStream: InputStream = if (Base64.isBase64(credentials)) {
        Base64.decodeBase64(credentials).inputStream()
    } else {
        credentials.byteInputStream()
    }
}
