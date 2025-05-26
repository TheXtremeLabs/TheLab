package com.riders.thelab.core.ui.compose.component.draw

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.riders.thelab.core.ui.BuildConfig

@Composable
fun DrawBox(
    drawController: DrawController,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    bitmapCallback: (ImageBitmap?, Throwable?) -> Unit,
    addNewTouchEvent: (Offset, Int) -> Unit,
    trackHistory: (undoCount: Int, redoCount: Int) -> Unit = { _, _ -> }
) = AndroidView(
    modifier = modifier,
    factory = {
        ComposeView(it).apply {
            setContent {
                val context = LocalContext.current
                var lastestOffset: Offset = Offset.Zero

                LaunchedEffect(drawController) {
                    drawController.changeBgColor(backgroundColor)
                    drawController.trackBitmaps(this@apply, this, bitmapCallback)
                    drawController.trackHistory(this, trackHistory)
                }

                Canvas(
                    modifier = modifier
                        .background(drawController.bgColor)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { offset ->
                                    //   println("TAP!")
                                    drawController.insertNewPath(offset)
                                    drawController.updateLatestPath(offset)
                                    drawController.pathList

                                    if (BuildConfig.DEBUG) {
                                        /*(context.findActivity() as InkRecognitionActivity).mViewModel.addNewTouchEvent(
                                            offset,
                                            MotionEvent.ACTION_DOWN
                                        )
                                        (context.findActivity() as InkRecognitionActivity).mViewModel.addNewTouchEvent(
                                            offset,
                                            MotionEvent.ACTION_UP
                                        )*/

                                        addNewTouchEvent(offset, MotionEvent.ACTION_DOWN)
                                        addNewTouchEvent(offset, MotionEvent.ACTION_UP)
                                    }
                                }
                            )
                        }
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    lastestOffset = Offset.Zero

                                    drawController.insertNewPath(offset)

                                    if (BuildConfig.DEBUG) {
                                        /*(context.findActivity() as InkRecognitionActivity).mViewModel.addNewTouchEvent(
                                            offset,
                                            MotionEvent.ACTION_DOWN
                                        )*/
                                        addNewTouchEvent(offset, MotionEvent.ACTION_DOWN)
                                    }
                                    // println("DRAG!")
                                },
                                onDragEnd = {

                                    if (BuildConfig.DEBUG) {
                                        /*(context.findActivity() as InkRecognitionActivity).mViewModel.addNewTouchEvent(
                                            lastestOffset,
                                            MotionEvent.ACTION_UP
                                        )*/
                                        addNewTouchEvent(lastestOffset, MotionEvent.ACTION_UP)

                                    }
                                }
                            ) { change, _ ->
                                val newPoint: Offset = change.position
                                drawController.updateLatestPath(newPoint)

                                if (BuildConfig.DEBUG) {
                                    /*(context.findActivity() as InkRecognitionActivity).mViewModel.addNewTouchEvent(
                                        change.position,
                                        MotionEvent.ACTION_MOVE
                                    )*/
                                    addNewTouchEvent(change.position, MotionEvent.ACTION_MOVE)
                                    lastestOffset = newPoint
                                }
                            }

                        }
                ) {
                    drawController.pathList.forEach { pw ->
                        drawPath(
                            createPath(pw.points),
                            color = pw.strokeColor,
                            alpha = pw.alpha,
                            style = Stroke(
                                width = pw.strokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                }
            }
        }
    }
)
