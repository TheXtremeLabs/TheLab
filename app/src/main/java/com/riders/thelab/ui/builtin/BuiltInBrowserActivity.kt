package com.riders.thelab.ui.builtin


import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.riders.thelab.R
import com.riders.thelab.core.ui.data.SnackBarType
import com.riders.thelab.core.ui.utils.UIManager
import com.riders.thelab.core.utils.BrowserUtils
import com.riders.thelab.databinding.ActivityBuiltInBrowserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BuiltInBrowserActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityBuiltInBrowserBinding

    private var url: String? = null

    private var mDownX = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityBuiltInBrowserBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setSupportActionBar(viewBinding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = getString(R.string.activity_title_built_in_web_view)
        url = intent.getStringExtra("url")
        if (TextUtils.isEmpty(url)) {
            finish()
        }
        initWebView()
        viewBinding.contentBuiltInBrowser.browserWebView.loadUrl(url!!)
    }

    @SuppressLint("ClickableViewAccessibility", "SetJavaScriptEnabled")
    private fun initWebView() {
        viewBinding.contentBuiltInBrowser.browserWebView.webChromeClient = MyWebChromeClient(this)
        viewBinding.contentBuiltInBrowser.browserWebView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                super.onPageStarted(view, url, favicon)
                viewBinding.builtInBrowserProgressBar.visibility = View.VISIBLE
                invalidateOptionsMenu()
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                viewBinding.contentBuiltInBrowser.browserWebView.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                viewBinding.builtInBrowserProgressBar.visibility = View.GONE
                invalidateOptionsMenu()
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                viewBinding.builtInBrowserProgressBar.visibility = View.GONE
                invalidateOptionsMenu()
            }
        }
        viewBinding.contentBuiltInBrowser.browserWebView.clearCache(true)
        viewBinding.contentBuiltInBrowser.browserWebView.clearHistory()
        viewBinding.contentBuiltInBrowser.browserWebView.settings.javaScriptEnabled = true
        viewBinding.contentBuiltInBrowser.browserWebView.isHorizontalScrollBarEnabled = false
        viewBinding.contentBuiltInBrowser.browserWebView.setOnTouchListener { _: View?, event: MotionEvent ->
            if (event.pointerCount > 1) {
                //Multi touch detected
                return@setOnTouchListener true
            }
            when (event.action) {
                MotionEvent.ACTION_DOWN -> mDownX = event.x // save the x
                MotionEvent.ACTION_MOVE, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> event.setLocation(
                    mDownX,
                    event.y
                ) // set x so that it doesn't move
            }
            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.browser, menu)
        if (BrowserUtils.isBookmarked(this, viewBinding.contentBuiltInBrowser.browserWebView.url)) {
            // change icon color
            BrowserUtils.tintMenuIcon(applicationContext, menu.getItem(0), R.color.teal_200)
        } else {
            BrowserUtils.tintMenuIcon(applicationContext, menu.getItem(0), R.color.white)
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (!viewBinding.contentBuiltInBrowser.browserWebView.canGoBack()) {
            menu.getItem(0).isEnabled = false
            menu.getItem(0).icon?.alpha = 130
        } else {
            menu.getItem(0).isEnabled = true
            menu.getItem(0).icon?.alpha = 255
        }
        if (!viewBinding.contentBuiltInBrowser.browserWebView.canGoForward()) {
            menu.getItem(1).isEnabled = false
            menu.getItem(1).icon?.alpha = 130
        } else {
            menu.getItem(1).isEnabled = true
            menu.getItem(1).icon?.alpha = 255
        }
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.itemId == android.R.id.home) {
            finish()
        }
        if (item.itemId == R.id.action_bookmark) {
            // bookmark / unbookmark the url
            BrowserUtils.bookmarkUrl(this, viewBinding.contentBuiltInBrowser.browserWebView.url)
            val msg =
                if (BrowserUtils.isBookmarked(
                        this,
                        viewBinding.contentBuiltInBrowser.browserWebView.url
                    )
                )
                    viewBinding.contentBuiltInBrowser.browserWebView.title + "is Bookmarked!"
                else viewBinding.contentBuiltInBrowser.browserWebView.title + " removed!"

            UIManager.showActionInSnackBar(
                this,
                msg,
                SnackBarType.NORMAL,
                "",
                null
            )

            // refresh the toolbar icons, so that bookmark icon color changes
            // depending on bookmark status
            invalidateOptionsMenu()
        }
        if (item.itemId == R.id.action_back) {
            back()
        }
        if (item.itemId == R.id.action_forward) {
            forward()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun back() {
        if (viewBinding.contentBuiltInBrowser.browserWebView.canGoBack()) {
            viewBinding.contentBuiltInBrowser.browserWebView.goBack()
        }
    }

    private fun forward() {
        if (viewBinding.contentBuiltInBrowser.browserWebView.canGoForward()) {
            viewBinding.contentBuiltInBrowser.browserWebView.goForward()
        }
    }
}