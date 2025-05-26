package com.riders.thelab.core.ui.compose.component.draw

import android.graphics.Bitmap
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach

class DrawController internal constructor(val trackHistory: (undoCount: Int, redoCount: Int) -> Unit = { _, _ -> }) {

    private val _redoPathList = mutableStateListOf<PathWrapper>()
    private val _undoPathList = mutableStateListOf<PathWrapper>()
    internal val pathList: SnapshotStateList<PathWrapper> = _undoPathList


    private val _historyTracker = MutableSharedFlow<String>(extraBufferCapacity = 1)
    private val historyTracker = _historyTracker.asSharedFlow()

    fun trackHistory(
        scope: CoroutineScope,
        trackHistory: (undoCount: Int, redoCount: Int) -> Unit
    ) {
        historyTracker
            .onEach { trackHistory(_undoPathList.size, _redoPathList.size) }
            .launchIn(scope)
    }


    private val _bitmapGenerators = MutableSharedFlow<Bitmap.Config>(extraBufferCapacity = 1)
    private val bitmapGenerators = _bitmapGenerators.asSharedFlow()

    fun saveBitmap(config: Bitmap.Config = Bitmap.Config.ARGB_8888) =
        _bitmapGenerators.tryEmit(config)

    var opacity:Float by mutableFloatStateOf(1f)
        private set

    var strokeWidth:Float by mutableFloatStateOf(10f)
        private set

    var color by mutableStateOf(Color.Red)
        private set

    var bgColor by mutableStateOf(Color.Black)
        private set

    fun changeOpacity(value: Float) {
        opacity = value
    }

    fun changeColor(value: Color) {
        color = value
    }

    fun changeBgColor(value: Color) {
        bgColor = value
    }

    fun changeStrokeWidth(value: Float) {
        strokeWidth = value
    }

    fun importPath(drawBoxPayLoad: DrawBoxPayLoad) {
        reset()
        bgColor = drawBoxPayLoad.bgColor
        _undoPathList.addAll(drawBoxPayLoad.path)
        _historyTracker.tryEmit("${_undoPathList.size}")
    }

    fun exportPath() = DrawBoxPayLoad(bgColor, pathList.toList())


    fun unDo() {
        if (_undoPathList.isNotEmpty()) {
            val last = _undoPathList.last()
            _redoPathList.add(last)
            _undoPathList.remove(last)
            trackHistory(_undoPathList.size, _redoPathList.size)
            _historyTracker.tryEmit("Undo - ${_undoPathList.size}")
        }
    }

    fun reDo() {
        if (_redoPathList.isNotEmpty()) {
            val last = _redoPathList.last()
            _undoPathList.add(last)
            _redoPathList.remove(last)
            trackHistory(_undoPathList.size, _redoPathList.size)
            _historyTracker.tryEmit("Redo - ${_redoPathList.size}")
        }
    }


    fun reset() {
        _redoPathList.clear()
        _undoPathList.clear()
        _historyTracker.tryEmit("-")
    }

    fun updateLatestPath(newPoint: Offset) {
        val index = _undoPathList.lastIndex
        _undoPathList[index].points.add(newPoint)
    }

    fun insertNewPath(newPoint: Offset) {
        val pathWrapper = PathWrapper(
            points = mutableStateListOf(newPoint),
            strokeColor = color,
            alpha = opacity,
            strokeWidth = strokeWidth,
        )
        _undoPathList.add(pathWrapper)
        _redoPathList.clear()
        _historyTracker.tryEmit("${_undoPathList.size}")
    }

    fun trackBitmaps(
        it: View,
        coroutineScope: CoroutineScope,
        onCaptured: (ImageBitmap?, Throwable?) -> Unit
    ) = bitmapGenerators
        .mapNotNull { config -> it.drawBitmapFromView(it.context, config) }
        .onEach { bitmap -> onCaptured(bitmap.asImageBitmap(), null) }
        .catch { error -> onCaptured(null, error) }
        .launchIn(coroutineScope)
}

@Composable
fun rememberDrawController(): DrawController {
    return remember { DrawController() }
}