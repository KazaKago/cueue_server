package com.kazakago.cooking_planner.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = TimeFrame.Serializer::class)
enum class TimeFrame {
    Breakfast,
    Lunch,
    SnackTime,
    Dinner;

    object Serializer : KSerializer<TimeFrame> {

        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("time_frame", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: TimeFrame) {
            encoder.encodeString(value.rawValue())
        }

        override fun deserialize(decoder: Decoder): TimeFrame {
            val rawValue = decoder.decodeString()
            return values().first { it.rawValue() == rawValue }
        }

        private fun TimeFrame.rawValue(): String {
            return when (this) {
                Breakfast -> "breakfast"
                Lunch -> "lunch"
                SnackTime -> "snack_time"
                Dinner -> "dinner"
            }
        }
    }
}
