package com.riders.thelab.ui.signup

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import com.riders.thelab.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.findActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun SignUpHeader(viewModel: SignUpViewModel, currentDestination: NavDestination) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val progressBarEULA = remember { mutableFloatStateOf(0f) }
    val animatedProgressBarEULA = remember { Animatable(progressBarEULA.value) }
    val progressBarForm = remember { mutableFloatStateOf(0f) }
    val animatedProgressBarForm = remember { Animatable(progressBarForm.value) }
    val progressBarSignUpSuccess = remember { mutableFloatStateOf(0f) }
    val animatedProgressBarSignUpSuccess = remember { Animatable(progressBarSignUpSuccess.value) }


    TheLabTheme(darkTheme = viewModel.isDarkMode) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            color = if (!isSystemInDarkTheme()) Color.Black else Color.White,
                            shape = CircleShape
                        )
                        .clip(shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier
                            .padding(16.dp)
                            .background(if (!isSystemInDarkTheme()) Color.Black else Color.White),
                        painter = painterResource(id = R.drawable.ic_lab_6_lab),
                        contentDescription = "the_lab_icon",
                        colorFilter = ColorFilter.tint(if (!isSystemInDarkTheme()) Color.White else Color.Black)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // ProgressBar EULA
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LinearProgressIndicator(
                            progress = if (LocalInspectionMode.current) .24f else animatedProgressBarEULA.value,
                            strokeCap = StrokeCap.Round
                        )
                        Text(
                            text = stringResource(id = R.string.title_activity_license_agreement),
                            style = TextStyle(fontSize = 16.sp)
                        )
                    }

                    // ProgressBar Form
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LinearProgressIndicator(
                            progress = if (LocalInspectionMode.current) .24f else animatedProgressBarForm.value,
                            strokeCap = StrokeCap.Round
                        )
                        Text(
                            text = stringResource(id = com.riders.thelab.core.ui.R.string.title_activity_user_inscription_form),
                            style = TextStyle(fontSize = 16.sp)
                        )
                    }

                    // ProgressBar SignUp Success
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LinearProgressIndicator(
                            progress = if (LocalInspectionMode.current) .24f else animatedProgressBarSignUpSuccess.value,
                            strokeCap = StrokeCap.Round
                        )
                        Text(
                            text = stringResource(id = R.string.title_activity_successful_sign_up),
                            style = TextStyle(fontSize = 16.sp)
                        )
                    }
                }
            }

            Icon(
                modifier = Modifier
                    .size(56.dp)
                    .padding(top = 24.dp, start = 8.dp)
                    .align(Alignment.TopStart)
                    .clickable {
                        when (currentDestination.route) {
                            SignUpScreen.EULA.route -> {
                                (context.findActivity() as SignUpActivity).finish()
                            }

                            else -> {
                                viewModel.updateShouldShowExitDialogConfirmation(true)
                            }
                        }


                    },
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "back_icon",
                tint = if (!isSystemInDarkTheme()) Color.Black else Color.White
            )
        }
    }

    LaunchedEffect(currentDestination) {
        when (currentDestination.route) {
            SignUpScreen.EULA.route -> {
                animatedProgressBarEULA.animateTo(1f)
                decreaseProgressBar(scope, animatedProgressBarSignUpSuccess)
                decreaseProgressBar(scope, animatedProgressBarForm)
            }

            SignUpScreen.Form.route -> {
                increaseProgressBar(scope, animatedProgressBarEULA)
                decreaseProgressBar(scope, animatedProgressBarSignUpSuccess)
                animatedProgressBarForm.animateTo(1f)
            }

            SignUpScreen.SignUpSuccess.route -> {
                increaseProgressBar(scope, animatedProgressBarEULA)
                increaseProgressBar(scope, animatedProgressBarForm)
                animatedProgressBarSignUpSuccess.animateTo(1f)
            }

            else -> {
                Timber.e("Else branch")
            }
        }
    }
}

fun decreaseProgressBar(
    coroutineScope: CoroutineScope,
    targetAnimatedFloat: Animatable<Float, AnimationVector1D>
) {
    if (0f != targetAnimatedFloat.value) {
        coroutineScope.launch { targetAnimatedFloat.animateTo(0f) }
    }
}

fun increaseProgressBar(
    coroutineScope: CoroutineScope,
    targetAnimatedFloat: Animatable<Float, AnimationVector1D>
) {
    if (1f != targetAnimatedFloat.value) {
        coroutineScope.launch { targetAnimatedFloat.animateTo(1f) }
    }
}

///////////////////////////////
//
// PREVIEWS
//
///////////////////////////////
@DevicePreviews
@Composable
private fun PreviewSignUpHeader() {
    val viewModel: SignUpViewModel = hiltViewModel()

    TheLabTheme(darkTheme = viewModel.isDarkMode) {
        viewModel.currentDestination?.let {
            SignUpHeader(
                viewModel = viewModel,
                currentDestination = it
            )
        }
    }
}