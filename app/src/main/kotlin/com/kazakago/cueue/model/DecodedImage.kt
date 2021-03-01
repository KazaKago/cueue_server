package com.kazakago.cueue.model

import com.kazakago.cueue.exception.ImageDecodeException
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.max

class DecodedImage(imageDataUri: String) {

    companion object {
        private const val extension = "webp"
        private const val shrinkMaxSize = 1280
    }

    val imageByte: ByteArray
    val mimeType = "image/$extension"
    val filePath = "images/${UUID.randomUUID()}.$extension"

    init {
        try {
            val rawImageData = getRawImageData(imageDataUri)
            val originalImageByte = Base64.getDecoder().decode(rawImageData)
            imageByte = getScaledImage(originalImageByte)
        } catch (exception: Exception) {
            throw ImageDecodeException(exception)
        }
    }

    private fun getRawImageData(imageDataUri: String): String {
        val rawImageDataIndex = imageDataUri.indexOf("base64,")
        return if (rawImageDataIndex != -1) {
            imageDataUri.substring((rawImageDataIndex + "base64,".length) until imageDataUri.length)
        } else {
            imageDataUri
        }
    }

    private fun getScaledImage(originalImageByte: ByteArray): ByteArray {
        return ByteArrayInputStream(originalImageByte).use { inputStream ->
            val originalImage = ImageIO.read(inputStream)
            val originalMaxSize = max(originalImage.width, originalImage.height)
            val scale = shrinkMaxSize.toFloat() / originalMaxSize.toFloat()
            val fixedImage = if (scale < 1.0) {
                BufferedImage((originalImage.width * scale).toInt(), (originalImage.height * scale).toInt(), originalImage.type).apply {
                    createGraphics().drawImage(originalImage, 0, 0, (originalImage.width * scale).toInt(), (originalImage.height * scale).toInt(), null)
                }
            } else {
                originalImage
            }
            ByteArrayOutputStream().use { outputStream ->
                ImageIO.write(fixedImage, extension, outputStream)
                outputStream.toByteArray()
            }
        }
    }
}
