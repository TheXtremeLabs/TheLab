package com.riders.thelab.ui.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.riders.thelab.databinding.ActivityWebviewBinding
import com.riders.thelab.ui.builtin.MyWebChromeClient
import com.riders.thelab.utils.Constants
import timber.log.Timber

class WebViewActivity : AppCompatActivity() {

    private var _viewBinding: ActivityWebviewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private var mUrl: String? = null
    private var m_downX = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        getBundle()
        initWebView()
        bindWebView()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        _viewBinding = null
        super.onDestroy()
    }

    private fun getBundle() {
        Timber.d("getBundle()")

        val url = intent.extras?.getString(Constants.WEB_URL)

        if (null == url) {
            Timber.e("url is null")
        } else {
            mUrl = url
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initWebView() {
        Timber.d("initWebView()")
        binding.webView.webChromeClient = MyWebChromeClient(this)
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.progressBar.visibility = View.VISIBLE
                invalidateOptionsMenu()
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                binding.webView.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if (null != binding)
                    binding.progressBar.visibility = View.GONE
                invalidateOptionsMenu()
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                binding.progressBar.visibility = View.GONE
                invalidateOptionsMenu()
            }
        }
        binding.webView.clearCache(true)
        binding.webView.clearHistory()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.isHorizontalScrollBarEnabled = false
        binding.webView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                if (event.pointerCount > 1) {
                    //Multi touch detected
                    return true
                }
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {

                        // save the x
                        m_downX = event.x
                    }
                    MotionEvent.ACTION_MOVE, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {

                        // set x so that it doesn't move
                        event.setLocation(m_downX, event.y)
                    }
                }
                return false
            }
        })
    }

    private fun bindWebView() {
        Timber.d("bindWebView()")
        if (null == mUrl) {
            Timber.e("url is null")
        } else {
            binding.webView.loadUrl(mUrl!!)
        }
    }
}