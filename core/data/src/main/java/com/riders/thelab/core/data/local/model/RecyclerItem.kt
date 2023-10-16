package com.riders.thelab.core.data.local.model

import android.os.Parcelable
import com.riders.thelab.core.data.local.bean.RecyclerEnum
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecyclerItem constructor(var name: String? = null) : Parcelable {
    constructor(item: RecyclerEnum) : this() {
        name = item.fullName
    }
}