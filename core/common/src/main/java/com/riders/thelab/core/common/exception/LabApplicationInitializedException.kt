package com.riders.thelab.core.common.exception

class LabApplicationInitializedException : Exception() {
    override val message: String
        get() = "Application initialization failed, maybe object parameters are null"

}