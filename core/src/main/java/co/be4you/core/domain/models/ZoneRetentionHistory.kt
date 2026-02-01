package co.be4you.core.domain.models

data class ZoneRetentionHistory(
    val id: Long,
    val assetId: Long,
    val zoneId: Long,
    val enterDateTime: Long,
    val exitDateTime: Long
){
    val retentionTimeMillis: Long
        get() = (exitDateTime - enterDateTime).coerceAtLeast(0L)
}