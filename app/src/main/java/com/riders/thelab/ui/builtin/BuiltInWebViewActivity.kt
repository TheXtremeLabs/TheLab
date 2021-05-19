package com.riders.thelab.ui.builtin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.riders.thelab.core.utils.BrowserUtils
import com.riders.thelab.databinding.ActivityBuiltInWebviewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BuiltInWebViewActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_POST_URL = "postUrl"
        var POST_URL = "http://api.androidhive.info/webview/index.html"
    }

    lateinit var viewBinding: ActivityBuiltInWebviewBinding

    private var m_downX = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityBuiltInWebviewBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        setSupportActionBar(viewBinding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (!TextUtils.isEmpty(intent.getStringExtra(EXTRA_POST_URL))) {
            POST_URL = intent.getStringExtra(EXTRA_POST_URL).toString()
        }

        initWebView()
        initCollapsingToolbar()
        renderPost()
    }

    @SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
    private fun initWebView() {
        viewBinding.contentBuiltInWebview.builtInWebView.webChromeClient = MyWebChromeClient(this)
        viewBinding.contentBuiltInWebview.builtInWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                /**
                 * Check for the url, if the url is from same domain
                 * open the url in the same activity as new intent
                 * else pass the url to browser activity
                 */
                if (BrowserUtils.isSameDomain(POST_URL, url)) {
                    val intent = Intent(
                            this@BuiltInWebViewActivity,
                            BuiltInWebViewActivity::class.java)
                    intent.putExtra("postUrl", url)
                    startActivity(intent)
                } else {
                    // launch in-app browser i.e BrowserActivity
                    openInAppBrowser(url)
                }
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                viewBinding.builtInProgressBar.visibility = View.GONE
            }
        }

        viewBinding.contentBuiltInWebview.builtInWebView.clearCache(true)
        viewBinding.contentBuiltInWebview.builtInWebView.clearHistory()
        viewBinding.contentBuiltInWebview.builtInWebView.settings.javaScriptEnabled = true
        viewBinding.contentBuiltInWebview.builtInWebView.isHorizontalScrollBarEnabled = false
        viewBinding.contentBuiltInWebview.builtInWebView.setOnTouchListener(OnTouchListener { v: View?, event: MotionEvent ->
            if (event.pointerCount > 1) {
                //Multi touch detected
                true
            }
            when (event.action) {
                MotionEvent.ACTION_DOWN -> m_downX = event.x // save the x
                MotionEvent.ACTION_MOVE, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP ->                     // set x so that it doesn't move
                    event.setLocation(m_downX, event.y)
            }
            false
        })
    }

    private fun renderPost() {
        viewBinding.contentBuiltInWebview.builtInWebView.loadUrl(POST_URL)
    }

    private fun openInAppBrowser(url: String) {
        val intent = Intent(
                this@BuiltInWebViewActivity,
                BuiltInBrowserActivity::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar txtPostTitle on scroll
     */
    private fun initCollapsingToolbar() {
        viewBinding.collapsingToolbar.title = " "
        viewBinding.appbar.setExpanded(true)

        // hiding & showing the txtPostTitle when toolbar expanded & collapsed
        viewBinding.appbar.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    viewBinding.collapsingToolbar.title = "Web View"
                    isShow = true
                } else if (isShow) {
                    viewBinding.collapsingToolbar.title = " "
                    isShow = false
                }
            }
        })

        // loading toolbar header image
        Glide.with(applicationContext)
                .load("http://api.androidhive.info/webview/nougat.jpg") /*.thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)*/
                .into(viewBinding.backdrop)
    }
}