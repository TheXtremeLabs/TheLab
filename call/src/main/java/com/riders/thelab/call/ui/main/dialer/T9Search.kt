package com.riders.thelab.call.ui.main.dialer

object T9Search {

    private val t9Map = mapOf(
        'a' to '2', 'b' to '2', 'c' to '2',
        'd' to '3', 'e' to '3', 'f' to '3',
        'g' to '4', 'h' to '4', 'i' to '4',
        'j' to '5', 'k' to '5', 'l' to '5',
        'm' to '6', 'n' to '6', 'o' to '6',
        'p' to '7', 'q' to '7', 'r' to '7', 's' to '7',
        't' to '8', 'u' to '8', 'v' to '8',
        'w' to '9', 'x' to '9', 'y' to '9', 'z' to '9'
    )

    fun search(query: String, contacts: List<String>): List<String> {
        if (query.isEmpty()) {
            return contacts
        }

        return contacts.filter { contact ->
            val contactAsT9 = contact.lowercase().mapNotNull { t9Map[it] }.joinToString("")
            contactAsT9.contains(query) || contact.contains(query)
        }
    }
}
