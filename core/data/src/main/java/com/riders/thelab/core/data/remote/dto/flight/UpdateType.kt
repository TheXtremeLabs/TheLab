package com.riders.thelab.core.data.remote.dto.flight

/**
 *
 * P=projected, O=oceanic, Z=radar, A=ADS-B, M=multilateration, D=datalink, X=surface
 * and near surface (ADS-B and ASDE-X), S=space-based.
 * Allowed: P┃O┃Z┃A┃M┃D┃X┃S┃
 */
enum class UpdateType(val type: String) {
    PROJECTED("P"),
    OCEANIC("O"),
    RADAR("Z"),
    ADS_B("A"),
    MULTI_LATERATION("M"),
    DATA_LINK("D"),
    SURFACE("X"),
    SPACE_BASED("S");
}