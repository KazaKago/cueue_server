package com.kazakago.cueue.model

interface Content {
    val imageByte: ByteArray
    val mimeType: String
    val filePath: String
}
