package com.riders.thelab.core.data.local.model.snackbar

data class Action(val actionTitle: String, val block: () -> Unit)
