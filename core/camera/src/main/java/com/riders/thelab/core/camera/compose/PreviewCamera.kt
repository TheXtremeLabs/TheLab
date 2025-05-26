package com.riders.thelab.core.camera.compose

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.utils.executor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.Executor
import java.util.concurrent.Executors


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun CameraSelectorButton(
    theme: AppTheme,
    darkTheme: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Box(
            modifier = Modifier
                .zIndex(1f)
                .then(modifier),
            contentAlignment = Alignment.Center
        ) {
            Button(
                modifier = Modifier
                    .size(56.dp)
                    .zIndex(2f),
                contentPadding = PaddingValues(8.dp),
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray.copy(alpha = .533f)),
                shape = CircleShape
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Rounded.Sync,
                    contentDescription = "toggle_camera_icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun BasicCameraSurface(
    modifier: Modifier,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    analyzer: ImageAnalysis.Analyzer? = null
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val cameraExecutor = Executors.newSingleThreadExecutor()
            val previewView = PreviewView(context).apply {
                this.scaleType = scaleType
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener(
                {
                    val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                    // CameraX Preview UseCase
                    // Here we set up the Composable legacy view compatibility wrapper with an instance of PreviewView,
                    // which comes from the camera-view package included above.
                    // We apply some additional useful configuration to it as well.
                    val preview = Preview.Builder()
                        .build()
                        .also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                    val imageCapture = ImageCapture.Builder().build()

                    val imageAnalyzer = ImageAnalysis.Builder()
                        .build()
                        .also {
                            if (null != analyzer) {
                                it.setAnalyzer(cameraExecutor, analyzer)
                            }
                        }

                    try {
                        // Unbind use cases before rebinding
                        cameraProvider.unbindAll()

                        // Bind use cases to camera
                        cameraProvider.bindToLifecycle(
                            context as ComponentActivity,
                            cameraSelector,
                            preview,
                            imageCapture,
                            imageAnalyzer
                        )
                    } catch (exc: Exception) {
                        Timber.e("DEBUG", "Use case binding failed", exc)
                    }
                },
                context.executor
            )
            previewView
        })
}

@SuppressLint("OpaqueUnitKey")
@Composable
fun AdvancedCameraSurface(
    theme: AppTheme,
    darkTheme: Boolean,
    modifier: Modifier,
    shouldToggleCamera: Boolean?,
    viewReferencedBlock: () -> Unit,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    analyzer: ImageAnalysis.Analyzer? = null
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val stateFlow = lifecycleOwner.lifecycle.currentStateFlow

    val currentLifecycleState by stateFlow.collectAsState()

    var cameraExecutor = Executors.newSingleThreadExecutor()
    var viewContext: Context? by remember { mutableStateOf(null) }
    var preview: Preview? by remember { mutableStateOf(null) }
    var cameraProvider: ProcessCameraProvider? by remember { mutableStateOf(null) }
    var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? by remember {
        mutableStateOf(null)
    }
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var imageAnalyzer: ImageAnalysis? by remember { mutableStateOf(null) }

    val lifecycleCameraController by remember {
        mutableStateOf(
            LifecycleCameraController(context).apply {
                if (analyzer != null) {
                    this.setImageAnalysisAnalyzer(
                        ContextCompat.getMainExecutor(context),
                        analyzer
                    )
                }

                // Enable image capture and image analysis
                // CameraController's enabled use cases: Preview + ImageCapture + ImageAnalysis
                // this.setEnabledUseCases(CameraController.IMAGE_CAPTURE or CameraController.IMAGE_ANALYSIS)
                this.isTapToFocusEnabled = true
                this.cameraSelector = cameraSelector

                bindToLifecycle(lifecycleOwner)
            })
    }
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        DisposableEffect(cameraProviderFuture) {
            onDispose {
                cameraProviderFuture?.get()?.unbindAll()
            }
        }

        DisposableEffect(
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .then(modifier),
                factory = { context ->
                    viewContext = context
                    cameraExecutor = Executors.newSingleThreadExecutor()

                    val previewView = PreviewView(context).apply {
                        this.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        this.scaleType = scaleType
                        this.layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        this.controller = lifecycleCameraController
                    }

                    cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                    cameraProviderFuture?.let {
                        it.addListener(
                            {
                                cameraProvider = it.get()

                                // CameraX Preview UseCase
                                // Here we set up the Composable legacy view compatibility wrapper with an instance of PreviewView,
                                // which comes from the camera-view package included above.
                                // We apply some additional useful configuration to it as well.
                                preview = Preview.Builder()
                                    .build()
                                    .also { view ->
                                        view.setSurfaceProvider(previewView.surfaceProvider)
                                    }

                                imageCapture = ImageCapture.Builder().build()

                                imageAnalyzer = ImageAnalysis.Builder()
                                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                    .setImageQueueDepth(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                    .setImageQueueDepth(10)
                                    .build()
                                    .also { imgAnalysis ->
                                        if (null != analyzer) {
                                            imgAnalysis.setAnalyzer(
                                                cameraExecutor,
                                                analyzer.apply {
                                                    /*if (this is BarcodeScannerAnalyzer) {
                                                        viewReferenced =
                                                            CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED
                                                    }*/

                                                    viewReferencedBlock.invoke()
                                                })
                                        }
                                    }

                                cameraProvider?.let { provider ->
                                    preview?.let { view ->
                                        imageCapture?.let { capture ->
                                            imageAnalyzer?.let { analyzer ->
                                                bindCamera(
                                                    lifecycleOwner = context as ComponentActivity,
                                                    cameraProvider = provider,
                                                    cameraSelector = if (null == shouldToggleCamera) cameraSelector else {
                                                        if (!shouldToggleCamera) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
                                                    },
                                                    preview = view,
                                                    imageCapture = capture,
                                                    imageAnalyzer = analyzer,
                                                    cameraController = lifecycleCameraController
                                                )
                                            }
                                        }
                                    }
                                }
                            },
                            context.executor
                        )
                    }

                    // initWithCoroutines()

                    previewView
                }
            ),
        ) {
            onDispose {
                /*cameraExecutor.shutdown()*/
                cameraProviderFuture?.get()?.unbindAll()
            }
        }
    }

    LifecycleStartEffect(Unit) {
        // ON_START code is executed here
        Timber.i("LifecycleStartEffect | ON_START code is executed here")

        onStopOrDispose {
            // do any needed clean up here
            Timber.e("LifecycleStartEffect | onStopOrDispose")
            cameraProviderFuture?.get()?.unbindAll()
            cameraExecutor.shutdown()
        }
    }

    LifecycleResumeEffect(Unit) {
        // ON_RESUME code is executed here
        Timber.d("LifecycleResumeEffect | ON_RESUME code is executed here")

        cameraProvider?.let { provider ->
            preview?.let { view ->
                imageCapture?.let { capture ->
                    imageAnalyzer?.let { analyzer ->
                        bindCamera(
                            lifecycleOwner = context as ComponentActivity,
                            cameraProvider = provider,
                            cameraSelector = if (null == shouldToggleCamera) cameraSelector else {
                                if (!shouldToggleCamera) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
                            },
                            preview = view,
                            imageCapture = capture,
                            imageAnalyzer = analyzer,
                            cameraController = lifecycleCameraController
                        )
                    }
                }
            }
        }

        onPauseOrDispose {
            // do any needed clean up here
            Timber.e("LifecycleResumeEffect | onPauseOrDispose")
        }
    }

    LaunchedEffect(currentLifecycleState) {
        Timber.d("LaunchedEffect | currentLifecycleState: $currentLifecycleState")

        when (currentLifecycleState) {
            Lifecycle.State.CREATED -> {
                Timber.d("LaunchedEffect | Lifecycle.State.CREATED | currentLifecycleState: ${currentLifecycleState.javaClass.simpleName}")
            }

            Lifecycle.State.STARTED -> {
                Timber.d("LaunchedEffect | Lifecycle.State.STARTED | currentLifecycleState: ${currentLifecycleState.javaClass.simpleName}")
            }

            Lifecycle.State.RESUMED -> {
                Timber.d("LaunchedEffect | Lifecycle.State.RESUMED | currentLifecycleState: ${currentLifecycleState.javaClass.simpleName}")
            }

            Lifecycle.State.DESTROYED -> {
                Timber.e("LaunchedEffect | Lifecycle.State.DESTROYED | currentLifecycleState: ${currentLifecycleState.javaClass.simpleName}")
            }

            Lifecycle.State.INITIALIZED -> {
                Timber.i("LaunchedEffect | Lifecycle.State.INITIALIZED | currentLifecycleState: ${currentLifecycleState.javaClass.simpleName}")
            }
        }
    }

    LaunchedEffect(shouldToggleCamera) {
        if (null != shouldToggleCamera) {

            cameraProviderFuture?.get()?.unbindAll()

            toggleCamera(lifecycleCameraController)

            cameraProvider?.let { provider ->
                preview?.let { view ->
                    imageCapture?.let { capture ->
                        imageAnalyzer?.let { analyzer ->
                            bindCamera(
                                lifecycleOwner = context as ComponentActivity,
                                cameraProvider = provider,
                                cameraSelector = if (!shouldToggleCamera) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA,
                                preview = view,
                                imageCapture = capture,
                                imageAnalyzer = analyzer,
                                cameraController = lifecycleCameraController
                            )
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("OpaqueUnitKey")
@Composable
fun CameraView(
    theme: AppTheme,
    darkTheme: Boolean,
    modifier: Modifier,
    viewReferencedBlock: () -> Unit,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    imageAnalyzer: ImageAnalysis.Analyzer? = null,
    galleryContent: (@Composable () -> Unit)? = null
) {
    var shouldToggleCamera: Boolean? by remember { mutableStateOf(null) }
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(modifier)
        ) {
            AdvancedCameraSurface(
                modifier = Modifier.align(Alignment.Center),
                theme = theme,
                darkTheme = darkTheme,
                shouldToggleCamera = shouldToggleCamera,
                scaleType = scaleType,
                cameraSelector = cameraSelector,
                viewReferencedBlock = viewReferencedBlock,
                analyzer = imageAnalyzer
            )

            CameraSelectorButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(48.dp),
                theme = theme,
                darkTheme = darkTheme
            ) {
                if (null == shouldToggleCamera) {
                    shouldToggleCamera = true
                }
                shouldToggleCamera = !shouldToggleCamera!!
            }

            if (null != galleryContent) {
                galleryContent()
            }
        }
    }
}

fun toggleCamera(cameraController: LifecycleCameraController) {
    Timber.d("toggleCamera()")

    if (cameraController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA
        && cameraController.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)
    ) {
        cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    } else if (cameraController.cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA
        && cameraController.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)
    ) {
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    }
}

fun bindCamera(
    lifecycleOwner: LifecycleOwner,
    cameraProvider: ProcessCameraProvider,
    cameraSelector: CameraSelector,
    preview: Preview,
    imageCapture: ImageCapture,
    imageAnalyzer: ImageAnalysis,
    cameraController: LifecycleCameraController
) {
    Timber.d("bindCamera()")

    try {
        // Unbind use cases before rebinding
        cameraProvider.unbindAll()

        // Bind use cases to camera
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture,
            imageAnalyzer
        )

        cameraController.bindToLifecycle(lifecycleOwner)
    } catch (exception: Exception) {
        Timber.e("bindCamera() | Use case binding failed ${exception.toString()}")
    }
}

fun initWithCoroutines(
    context: Context,
    coroutineScope: CoroutineScope,
    previewView: PreviewView,
    cameraExecutor: Executor,
    cameraSelector: CameraSelector,
    analyzer: ImageAnalysis.Analyzer? = null
) {
    run {
        coroutineScope.launch {
            context.getCameraProvider().apply {

                // CameraX Preview UseCase
                // Here we set up the Composable legacy view compatibility wrapper with an instance of PreviewView,
                // which comes from the camera-view package included above.
                // We apply some additional useful configuration to it as well.
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                val imageCapture = ImageCapture.Builder().build()

                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .setImageQueueDepth(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .setImageQueueDepth(10)
                    .build()
                    .also {
                        if (null != analyzer) {
                            it.setAnalyzer(cameraExecutor, analyzer)
                        }
                    }

                try {
                    // Unbind use cases before rebinding
                    this.unbindAll()

                    // Bind use cases to camera
                    this.bindToLifecycle(
                        context as ComponentActivity,
                        cameraSelector,
                        preview,
                        imageCapture,
                        imageAnalyzer
                    )
                } catch (exc: Exception) {
                    Timber.e("initWithCoroutines() | Use case binding failed : ${exc.toString()}")
                }
            }
        }
    }
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewCameraSelectorButton(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        CameraSelectorButton(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            modifier = Modifier
        ) {}
    }
}

@DevicePreviews
@Composable
private fun PreviewCameraSurface(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        AdvancedCameraSurface(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            modifier = Modifier,
            shouldToggleCamera = false,
            viewReferencedBlock = {})
    }
}

@DevicePreviews
@Composable
private fun PreviewCameraView(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        CameraView(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            modifier = Modifier.fillMaxSize(),
            viewReferencedBlock = {})
    }
}