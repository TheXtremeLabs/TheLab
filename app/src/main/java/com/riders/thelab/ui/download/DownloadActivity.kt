package com.riders.thelab.ui.download

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.riders.thelab.R
import com.riders.thelab.data.local.model.Download
import com.riders.thelab.databinding.ActivityDownloadBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class DownloadActivity
    : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()


    private var _viewBinding: ActivityDownloadBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private val mViewModel: DownloadViewModel by viewModels()


    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityDownloadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewsModelsObservers()

        downloadFile()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("onDestroy()")
        _viewBinding = null
    }

    @SuppressLint("SetTextI18n")
    private fun initViewsModelsObservers() {
        mViewModel.getPercentData().observe(this, {
            binding.progressBar.progress = it
        })

        mViewModel.getDownloadStatus().observe(this, {
            when (it) {
                is Download.Started -> {
                    Timber.d("${it.started}")
                    binding.tvDownloadStatus.text = "Started"
                }

                is Download.Done -> {
                    Timber.d("${it.done}")
                    binding.progressBar.progress = 100
                    binding.progressBar.setIndicatorColor(
                        ContextCompat.getColor(this@DownloadActivity, R.color.success)
                    )

                    binding.tvDownloadStatus.text = "Success ! Download Done"
                }

                is Download.Error -> {
                    Timber.d("${it.error}")
                    showDownloadError("Error ! Download Failed")
                }
                is Download.Finished -> { // Ignored
                }
                is Download.Progress -> { // Ignored
                }
            }
        })
    }


    @SuppressLint("SetTextI18n")
    @DelicateCoroutinesApi
    private fun downloadFile() {

        GlobalScope.launch {
            try {
                supervisorScope {
                    mViewModel.downloadFile()
                }
            } catch (exception: Exception) {
                exception.printStackTrace()

                if (exception is UnknownHostException) {
                    Timber.e("Check your connection, cannot reach host ${exception.message}, cause : ${exception.cause}")
                    showDownloadError("Check your connection, cannot reach host ${exception.message}")
                }
            }
        }
    }

    private fun showDownloadError(message: String) {
        CoroutineScope(coroutineContext).launch {
            binding.progressBar.progress = 100
            binding.progressBar.setIndicatorColor(
                ContextCompat.getColor(this@DownloadActivity, R.color.error)
            )
            binding.tvDownloadStatus.text = message
        }
    }
}