package com.kazakago.cueue.model

import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.exif.ExifIFD0Directory
import com.kazakago.cueue.exception.ImageDecodeException
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.*


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
            imageByte = getFixedImage(originalImageByte)
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

    private fun getFixedImage(originalImageByte: ByteArray): ByteArray {
        val rotatedDegree = ByteArrayInputStream(originalImageByte).use { getRotateDegree(it) }
        return ByteArrayInputStream(originalImageByte).use { inputStream ->
            val originalImage = ImageIO.read(inputStream)
            val shrinkImage = originalImage.shrink(shrinkMaxSize)
            val rotatedImage = shrinkImage.rotate(rotatedDegree)
            ByteArrayOutputStream().use { outputStream ->
                ImageIO.write(rotatedImage, extension, outputStream)
                outputStream.toByteArray()
            }
        }
    }

    private fun getRotateDegree(inputStream: InputStream): Double {
        val metadata = ImageMetadataReader.readMetadata(inputStream)
        val exif = metadata.getFirstDirectoryOfType(ExifIFD0Directory::class.java)
        return when (exif?.getInt(ExifIFD0Directory.TAG_ORIENTATION)) {
            1 -> 0.0
            6 -> 90.0
            3 -> 180.0
            8 -> 270.0
            else -> 0.0
        }
    }

    private fun BufferedImage.shrink(maxSize: Int): BufferedImage {
        val originalMaxSize = max(width, height)
        val scale = maxSize.toFloat() / originalMaxSize.toFloat()
        return if (scale < 1.0) {
            BufferedImage((width * scale).toInt(), (height * scale).toInt(), type).apply {
                val graphics = createGraphics()
                graphics.drawImage(this@shrink, 0, 0, (this@shrink.width * scale).toInt(), (this@shrink.height * scale).toInt(), null)
                graphics.dispose()
            }
        } else {
            this
        }
    }

    private fun BufferedImage.rotate(angle: Double): BufferedImage {
        val radian = Math.toRadians(angle)
        val sin = abs(sin(radian))
        val cos = abs(cos(radian))
        val newWidth = floor(width * cos + height * sin)
        val newHeight = floor(height * cos + width * sin)
        return BufferedImage(newWidth.toInt(), newHeight.toInt(), type).apply {
            val graphics = createGraphics()
            graphics.transform = AffineTransform().apply {
                translate((newWidth - this@rotate.width) / 2.0, (newHeight - this@rotate.height) / 2.0)
                rotate(radian, this@rotate.width / 2.0, this@rotate.height / 2.0)
            }
            graphics.drawImage(this@rotate, 0, 0, this@rotate.width, this@rotate.height, null)
            graphics.dispose()
        }
    }
}
