package com.riders.thelab.core.data.remote.dto.kat

import android.annotation.SuppressLint
import com.riders.thelab.core.utils.LabCompatibilityManager
import java.util.*
import java.util.stream.Collectors

data class Kat constructor(
    var chatId: String? = null,
    var messageId: String? = null,
    var senderId: String? = null,
    var message: String? = null,
    var messageType: String? = null,
    var timestamp: Long = 0
) {
    companion object {
        /*Comparator for sorting the list by Student Name*/
        private var katComparator: Comparator<Kat> =
            Comparator<Kat> { s1, s2 ->
                val timestamp1: Long = s1.timestamp
                val timestamp2: Long = s2.timestamp

                //ascending order
                timestamp1.toString().compareTo(timestamp2.toString())

                //descending order
                //return StudentName2.compareTo(StudentName1);
            }

        @SuppressLint("NewApi")
        fun buildKatMessagesList(katModelDto: HashMap<String, Any>): List<Kat> {
            val list: MutableList<Kat> = ArrayList()

            // Loop on value to create a fetched list of message from database
            for ((_, value) in katModelDto) {
                val innerMap = value as Map<String, Any>
                val kat = Kat()
                for ((key, value1) in innerMap) {
                    when (key) {
                        "chatId" -> kat.chatId = value1 as String
                        "messageId" -> kat.messageId = value1 as String
                        "senderId" -> kat.senderId = value1 as String
                        "message" -> kat.message = value1 as String
                        "messageType" -> kat.messageType = value1 as String
                        "timestamp" -> kat.timestamp = value1 as Long
                        else -> {
                        }
                    }
                }
                list.add(kat)
            }


            return if (!LabCompatibilityManager.isNougat()) {
                Collections.sort(list, katComparator)
                list
            } else {
                list
                    .stream()
                    .sorted { o1: Kat, o2: Kat ->
                        o1.timestamp.toString().compareTo(o2.timestamp.toString())
                    }
                    .collect(Collectors.toList())
            }
        }

    }


    constructor() : this("", "", "", "", "", 0)

    constructor(item: Map.Entry<String, Any>) : this() {
        when (item.key) {
            "chatId" -> chatId = item.value as String
            "messageId" -> messageId = item.value as String
            "senderId" -> senderId = item.value as String
            "message" -> message = item.value as String
            "messageType" -> messageType = item.value as String
            "timestamp" -> timestamp = item.value as Long
            else -> {
            }
        }
    }
}
