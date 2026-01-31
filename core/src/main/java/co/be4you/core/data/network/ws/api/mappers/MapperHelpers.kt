package co.be4you.core.data.network.ws.api.mappers

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun String.toMillis(): Long? = runCatching {
    Instant.parse(this).toEpochMilliseconds()
}.getOrNull()
