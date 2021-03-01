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
        private const val mimeType = "image/$extension"
        private const val shrinkMaxSize = 1280
    }

    val imageByte: ByteArray
    val mimeType = DecodedImage.mimeType
    val filePath = "images/${UUID.randomUUID()}.$extension"

    init {
        try {
            val originalImageByte = imageDataUri.substring((imageDataUri.indexOf("base64,") + "base64,".length) until imageDataUri.length).let {
                Base64.getDecoder().decode(it)
            }
            imageByte = scaleImage(originalImageByte)
        } catch (exception: Exception) {
            throw ImageDecodeException(exception)
        }
    }

    private fun scaleImage(originalImageByte: ByteArray): ByteArray {
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
