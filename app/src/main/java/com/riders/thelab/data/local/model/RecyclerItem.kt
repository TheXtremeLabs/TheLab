package com.riders.thelab.data.local.model

import android.os.Parcelable
import com.riders.thelab.data.local.bean.RecyclerEnum
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecyclerItem constructor(var name: String? = null) : Parcelable {
    constructor(item: RecyclerEnum) : this() {
        name = item.fullName
    }
}