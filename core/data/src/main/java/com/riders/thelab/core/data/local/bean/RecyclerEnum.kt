package com.riders.thelab.core.data.local.bean

import com.riders.thelab.core.data.local.model.RecyclerItem

enum class RecyclerEnum(val fullName: String) {
    ALEX("Alex Rider"),
    KEN("Ken Rider"),
    HALLEY_BECKSMANN("Halley Becksmann"),
    PI_ERRE("PI'ERRE"),
    NESRINE("Nesrine"),
    MIKE_TYSON("Mike Tyson"),
    MICHAEL_B_JORDAN("Michael B. Jordan"),
    CARLOS_ESPOSIIO("Carlos Esposito"),
    STEPH_DANN("Steph Dann"),
    KEN_INVERSION("Ken Iverson");

    companion object {
        fun getRecyclerItems(): List<RecyclerItem> {
            val list: MutableList<RecyclerItem> = ArrayList()
            for (item in values()) {
                list.add(RecyclerItem(item))
            }
            return list
        }
    }
}