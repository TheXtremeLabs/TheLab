package com.riders.thelab.data.local.model

import com.riders.thelab.data.local.bean.RecyclerEnum
import org.parceler.Parcel

@Parcel
data class RecyclerItem constructor(var name: String? = null) {

    constructor(item: RecyclerEnum) : this() {
        name = item.getName()
    }

    override fun toString(): String {
        return "RecyclerItem(name=$name)"
    }
}