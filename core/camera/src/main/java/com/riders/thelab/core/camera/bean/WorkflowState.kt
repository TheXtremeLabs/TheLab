package com.riders.thelab.core.camera.bean

/**
 * State set of the application workflow.
 */
enum class WorkflowState {
    NOT_STARTED,
    DETECTING,
    DETECTED,
    CONFIRMING,
    CONFIRMED,
    SEARCHING,
    SEARCHED
}