package com.riders.thelab.data.local.model

import android.os.Parcelable
import com.riders.thelab.data.local.bean.RecyclerEnum
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecyclerItem constructor(var name: String? = null) : Parcelable {

    constructor(item: RecyclerEnum) : this() {
        name = item.fullName
    }

    override fun toString(): String {
        return "RecyclerItem(name=$name)"
    }
}