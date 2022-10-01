package com.riders.thelab.ui.about

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.riders.thelab.BuildConfig
import com.riders.thelab.R
import com.riders.thelab.databinding.ActivityAboutBinding
import com.riders.thelab.ui.webview.WebViewActivity
import com.riders.thelab.utils.Constants
import timber.log.Timber

class AboutActivity : AppCompatActivity(), View.OnClickListener {
    private var _viewBinding: ActivityAboutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setListeners()

        binding.tvAppVersion.text = BuildConfig.VERSION_NAME
    }

    override fun onDestroy() {
        _viewBinding = null
        super.onDestroy()
    }

    private fun setListeners() {
        Timber.d("setListeners()")
        binding.tvAppAuthor.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (R.id.tv_app_author == view?.id) {
            startActivity(
                Intent(
                    this,
                    WebViewActivity::class.java
                ).apply {
                    this.putExtra(
                        Constants.WEB_URL,
                        "https://www.${binding.tvAppAuthor.text}"
                    )
                })
        }
    }
}