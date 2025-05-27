package com.riders.thelab.feature.palette

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.base.observeLifecycleEvents
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class PaletteActivity : BaseComponentActivity() {

    private val viewModel: PaletteViewModel by viewModels()

    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getWallpaperImages(this@PaletteActivity)

        enableEdgeToEdge()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    // Register lifecycle events
                    viewModel.observeLifecycleEvents(LocalLifecycleOwner.current.lifecycle)

                    val theme: AppTheme by viewModel.uiRepository
                        .getTheme()
                        .collectAsStateWithLifecycle(initialValue = AppTheme.Default)
                    val isDarkTheme: Boolean by viewModel.uiRepository
                        .isThemeDarkMode()
                        .collectAsStateWithLifecycle(initialValue = false)

                    val hasInternetConnection by viewModel.hasInternetConnection.collectAsStateWithLifecycle()

                    val paletteState by viewModel.paletteUiState.collectAsStateWithLifecycle()

                    TheLabTheme(theme = theme, darkTheme = isDarkTheme) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            PaletteContent(
                                theme = theme,
                                darkTheme = isDarkTheme,
                                hasInternetConnection = hasInternetConnection,
                                paletteUiState = paletteState,
                                paletteNameList = viewModel.paletteNameList,
                                onRefreshedClicked = {
                                    viewModel.updateIsRefreshing(true)
                                    viewModel.getWallpaperImages(this@PaletteActivity)
                                },
                                isRefreshing = viewModel.isRefreshing
                            )
                        }
                    }
                }
            }
        }
    }

    override fun backPressed() {
        Timber.e("backPressed")
        showAppClosingDialog()
    }


    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    private fun showAppClosingDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Warning")
            .setMessage("Do you really want to close the app?")
            .setPositiveButton("Yes") { _, _ -> finish() }
            .setNegativeButton("No", null)
            .show()
    }
}