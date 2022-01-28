package com.riders.thelab.ui.compose

import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceActivity
import androidx.activity.ComponentActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riders.thelab.R
import com.riders.thelab.data.local.model.Message
import com.riders.thelab.databinding.ActivityComposeBinding

class ComposeActivity : PreferenceActivity() {

    companion object {
        private const val senderPierre = "Pi'erre"
        private const val senderCarti = "Carti"
        private const val senderNudy = "Nudy"
        private const val senderUzi = "Uzi"

        val conversationSample = mutableListOf(
            Message(senderNudy, "Ayoo! Yo Pi'erre you wanna come out here ?"),
            Message(senderPierre, "Heeeeinnn"),
            Message(senderUzi, "Wooow it's Lil Uzi Vert"),
            Message(senderCarti, "Pu n bout that shi"),
            Message(senderPierre, "What on my what ?"),
            Message(senderCarti, "See that shi we eat at the police"),
            Message(senderPierre, "What?"),
            Message(senderNudy, "Who?"),
            Message(senderUzi, "And I'm not from Earth, I'm from outta space?"),
            Message(senderPierre, "Wow?"),
            Message(senderUzi, "That's different"),
            Message(
                senderUzi,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
            )
        ) as List<Message>
    }

    private var _viewBinding: ActivityComposeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityComposeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.greeting.setContent {
            MaterialTheme {
                PreviewConversation()
            }
        }
    }
}

@Composable
fun MessageCard(msg: Message) {
    Row {
        Image(
            painter = painterResource(R.drawable.logo_colors),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                // Set image size to 40 dp
                .size(40.dp)
                // Clip image to be shaped as a circle
                .clip(CircleShape)
        )

        // Add a horizontal space between the image and the column
        Spacer(modifier = Modifier.width(8.dp))

        // We keep track if the message is expanded or not in this
        // variable
        var isExpanded by remember { mutableStateOf(false) }
        // surfaceColor will be updated gradually from one color to the other
        val surfaceColor: Color by animateColorAsState(
            if (isExpanded) MaterialTheme.colors.primary else colorResource(id = R.color.default_dark),
        )

        // We toggle the isExpanded variable when we click on this Column
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = msg.sender,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )

            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 1.dp,
                // surfaceColor color will be changing gradually from primary to surface
                color = surfaceColor,
                // animateContentSize will change the Surface size gradually
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    modifier = Modifier.padding(all = 4.dp),
                    // If the message is expanded, we display all its content
                    // otherwise we only display the first line
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.body2,
                    text = msg.message,
                    color = Color.White
                )
            }
        }

    }

}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewMessageCard() {

    Column(modifier = Modifier.padding(all = 8.dp)) {
        Column {
            Text(
                text = "This is a column!",
                color = Color.White
            )
            Text(
                text = "Hello World!",
                color = Color.White
            )
            Text(
                text = "Hello World!",
                color = Color.White
            )
            Text(
                text = "Hello World!",
                color = Color.White
            )
        }
        Row {
            Text(
                text = "Row 1",
                color = Color.White
            )
            Text(
                text = "Row 2",
                color = Color.White
            )
            Text(
                text = "Row 3",
                color = Color.White
            )
        }
        Text(
            text = "Hello World!",
            color = Color.White
        )
    }

}

@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn {
        // Add n items
        items(messages) { message ->
            MessageCard(message)
        }
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewConversation() {
    Column(modifier = Modifier.padding(all = 8.dp)) {
        Conversation(ComposeActivity.conversationSample)
    }

}