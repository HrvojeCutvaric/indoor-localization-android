package co.be4you.core.domain.models

import java.time.LocalDateTime

data class Asset(
    val id: Long,
    val name: String,
    val active: Boolean,
    val floorMapId: Long,
    val x: Double?,
    val y: Double?,
    val lastSync: LocalDateTime?,
    val colorHex: String,
)