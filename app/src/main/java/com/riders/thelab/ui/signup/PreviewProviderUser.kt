package com.riders.thelab.ui.signup

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.common.utils.encodeToSha256
import com.riders.thelab.core.data.local.model.User
import com.riders.thelab.core.data.local.model.compose.UserState

class PreviewProviderUser : PreviewParameterProvider<User> {
    override val values: Sequence<User>
        get() = sequenceOf(
            User(
                "Jane",
                "Doe",
                "JaneDoe345",
                "jane.doe@test.com",
                "test1234".encodeToSha256(),
                1702222514L
            ),
            User(
                "John",
                "Smith",
                "JohnSmith27",
                "john.smith@test.com",
                "test1234".encodeToSha256(),
                1702222522L
            ),
            User(
                "Mike",
                "Law",
                "Mike1552",
                "mike@test.fr",
                "test1234".encodeToSha256(),
                1702222536L
            )
        )
}

class PreviewProviderUserState : PreviewParameterProvider<UserState> {
    override val values: Sequence<UserState>
        get() = sequenceOf(
            UserState.Saving,
            UserState.Saved(1L),
            UserState.NotSaved
        )
}