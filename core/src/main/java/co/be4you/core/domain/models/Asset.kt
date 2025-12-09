package co.be4you.core.domain.models

import java.time.Instant

data class Asset(
    val id: Long,
    val name: String,
    val colorHex: String?,
    val x: Double?,
    val y: Double?,
    val floorMapId: Long,
    val active: Boolean,
    val lastSync: Instant?,
)
