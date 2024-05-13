package com.riders.thelab.core.data.local.model

data class Message(val sender: String, val message: String, val isUser: Boolean) {
    companion object {
        private const val senderPierre = "Pi'erre"
        private const val senderCarti = "Carti"
        private const val senderNudy = "Nudy"
        private const val senderUzi = "Uzi"

        val conversationSample: List<Message> = listOf(
            Message(senderNudy, "Ayoo! Yo Pi'erre you wanna come out here ?", false),
            Message(senderPierre, "Heeeeinnn", true),
            Message(senderUzi, "Wooow it's Lil Uzi Vert", false),
            Message(senderCarti, "Pu n bout that shi", false),
            Message(senderPierre, "What on my what ?", true),
            Message(senderCarti, "See that shi we eat at the police", false),
            Message(senderPierre, "What?", true),
            Message(senderNudy, "Who?", false),
            Message(senderUzi, "And I'm not from Earth, I'm from outta space?", false),
            Message(senderPierre, "Wow?", true),
            Message(senderUzi, "That's different", false),
            Message(
                senderUzi,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                false
            )
        )
    }
}