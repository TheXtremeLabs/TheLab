package com.riders.thelab.core.data.remote.dto.flight

/**
 * Source indicator of the predicted time of the gate departure event. Only available from /foresight endpoints.
 * Allowed: ┃Foresight┃Historical Average
 */
enum class PredictedSource {
    Foresight, Historical, Average;
}