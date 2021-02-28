package com.kazakago.cueue.model

import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.exception.ImageDecodeException
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import javax.imageio.ImageIO

class DecodedImage(imageDataUri: String) {

    companion object {
        private const val extension = "webp"
        private const val mimeType = "image/$extension"
    }

    val imageByte: ByteArray
    val mimeType: String

    init {
        try {
            val originalImageByte = imageDataUri.substring((imageDataUri.indexOf("base64,") + "base64,".length) until imageDataUri.length).let {
                Base64.getDecoder().decode(it)
            }
            imageByte = ByteArrayInputStream(originalImageByte).use { inputStream ->
                ByteArrayOutputStream().use { outputStream ->
                    val originalImage = ImageIO.read(inputStream)
                    ImageIO.write(originalImage, extension, outputStream)
                    outputStream.toByteArray()
                }
            }
            mimeType = DecodedImage.mimeType
        } catch (exception: Exception) {
            throw ImageDecodeException(exception)
        }
    }

    fun createFilePath(workspace: WorkspaceEntity): String {
        return "workspaces/${workspace.id.value}/images/${UUID.randomUUID()}.$extension"
    }
}
