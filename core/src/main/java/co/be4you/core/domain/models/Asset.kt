package co.be4you.core.domain.models

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
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
) {
    val color: Color = colorHex?.let { colorHex ->
        try {
            Color(colorHex.toColorInt())
        } catch (_: Exception) {
            Color.Gray
        }
    } ?: Color.Gray
}
