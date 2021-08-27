package com.kazakago.cueue.model

import com.kazakago.cueue.mapper.rawValue
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = WorkspaceType.Serializer::class)
enum class WorkspaceType {
    Personal;

    companion object {
        fun resolve(value: String): WorkspaceType {
            return values().first { it.rawValue() == value }
        }
    }

    object Serializer : KSerializer<WorkspaceType> {

        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("workspace_type", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: WorkspaceType) {
            encoder.encodeString(value.rawValue())
        }

        override fun deserialize(decoder: Decoder): WorkspaceType {
            val rawValue = decoder.decodeString()
            return values().first { it.rawValue() == rawValue }
        }
    }
}
