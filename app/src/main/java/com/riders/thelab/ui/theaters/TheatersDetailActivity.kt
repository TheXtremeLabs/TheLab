package com.riders.thelab.ui.theaters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import kotlinx.coroutines.launch
import timber.log.Timber

class TheatersDetailActivity : ComponentActivity() {

    private val mViewModel: TheatersDetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.getBundle(this@TheatersDetailActivity, this.intent)

        lifecycleScope.launch {
            Timber.d("coroutine launch with name ${this.coroutineContext}")
            repeatOnLifecycle(Lifecycle.State.CREATED) {

                setContent {
                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            TheatersDetailContent(viewModel = mViewModel)
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_MOVIE = "MOVIE"
    }
}