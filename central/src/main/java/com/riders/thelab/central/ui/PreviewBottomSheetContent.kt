package com.riders.thelab.central.ui

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.riders.thelab.central.R
import com.riders.thelab.core.common.utils.Constants
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import kotlinx.coroutines.launch


///////////////////////////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////////////////////////
@Composable
fun CentralBottomSheetTooltipContent(
    theme: AppTheme,
    darkTheme: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 90.dp)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(
                    12.dp,
                    Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(vertical = 8.dp),
                    onClick = {
                        // Open project's url in a browser
                        uiEvent.invoke(UiEvent.OnOpenProjectInBrowserClicked(Constants.PROJECT_URL))
                    }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            modifier = Modifier
                                .weight(1.5f)
                                .size(24.dp),
                            painter = painterResource(com.riders.thelab.core.ui.R.drawable.ic_github),
                            contentDescription = null,
                            tint = if (!darkTheme) Color.Black else Color.White,
                        )
                        Text(modifier = Modifier.weight(1f), text = "View on Github")
                    }
                }

                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(2.dp)
                        .padding(vertical = 12.dp)
                )

                Button(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(vertical = 8.dp),
                    onClick = {
                        // Open a dialog so the user enters his email to send him the project link directly
                        uiEvent.invoke(UiEvent.OnSendProjectWithEmailClicked)
                    }) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            modifier = Modifier
                                .weight(1.5f)
                                .size(20.dp)
                                .rotate(315f),
                            imageVector = Icons.Rounded.Send,
                            contentDescription = null,
                            tint = if (!darkTheme) Color.Black else Color.White,
                        )
                        Text(modifier = Modifier.weight(1f), text = "Send via e-mail")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    theme: AppTheme,
    darkTheme: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val tooltipState = rememberTooltipState(isPersistent = true)

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Icon(
                    modifier = Modifier.weight(1.5f),
                    painter = painterResource(if (!darkTheme) R.drawable.ic_the_lab_central else R.drawable.ic_the_lab_central_white),
                    contentDescription = null
                )

                Text(modifier = Modifier.weight(1f), text = stringResource(R.string.app_name))
            }

            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp)
                    .padding(vertical = 12.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            ) {
                TooltipBox(
                    state = tooltipState,
                    positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                        TooltipAnchorPosition.Above
                    ),
                    tooltip = {
                        CentralBottomSheetTooltipContent(
                            theme = theme,
                            darkTheme = darkTheme,
                            uiEvent = uiEvent
                        )
                    }
                ) {
                    Button(
                        onClick = { scope.launch { tooltipState.show(MutatePriority.UserInput) } }
                    ) {
                        Text(text = stringResource(R.string.msg_share_project))
                    }
                }

                Text(text = "a TheLab © Company")
            }
        }
    }
}


///////////////////////////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////////////////////////
@DevicePreviews
@Composable
fun PreviewCentralBottomSheetTooltipContent() {
    TheLabTheme(theme = AppTheme.Default, darkTheme = isSystemInDarkTheme()) {
        CentralBottomSheetTooltipContent(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme()
        ) {}
    }
}

@DevicePreviews
@Composable
fun PreviewBottomSheetContent() {
    TheLabTheme(theme = AppTheme.Default, darkTheme = isSystemInDarkTheme()) {
        BottomSheetContent(theme = AppTheme.Default, darkTheme = isSystemInDarkTheme()) {}
    }
}