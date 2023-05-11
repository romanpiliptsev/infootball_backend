package com.project.infootball.data_classes

import java.time.OffsetDateTime

data class JwtResponse(
    var token: String,
    var expiration: OffsetDateTime,
    var issuedAt: OffsetDateTime
)