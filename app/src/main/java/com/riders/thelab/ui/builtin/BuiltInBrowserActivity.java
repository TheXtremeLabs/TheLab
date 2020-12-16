package com.riders.thelab.ui.builtin;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;
import com.riders.thelab.R;
import com.riders.thelab.core.utils.BrowserUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class BuiltInBrowserActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.browserWebView)
    WebView mBrowserWebView;
    @BindView(R.id.built_in_browser_progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.main_content)
    CoordinatorLayout coordinatorLayout;

    private String url;

    private float m_downX;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_built_in_browser);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.activity_title_built_in_web_view));

        url = getIntent().getStringExtra("url");

        if (TextUtils.isEmpty(url)) {
            finish();
        }

        initWebView();

        mBrowserWebView.loadUrl(url);
    }

    @SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled"})
    private void initWebView() {
        mBrowserWebView.setWebChromeClient(new MyWebChromeClient(this));
        mBrowserWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mBrowserWebView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
                invalidateOptionsMenu();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                mProgressBar.setVisibility(View.GONE);
                invalidateOptionsMenu();
            }
        });
        mBrowserWebView.clearCache(true);
        mBrowserWebView.clearHistory();
        mBrowserWebView.getSettings().setJavaScriptEnabled(true);
        mBrowserWebView.setHorizontalScrollBarEnabled(false);
        mBrowserWebView.setOnTouchListener((v, event) -> {

            if (event.getPointerCount() > 1) {
                //Multi touch detected
                return true;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // save the x
                    m_downX = event.getX();
                    break;

                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    // set x so that it doesn't move
                    event.setLocation(m_downX, event.getY());
                    break;

                default:
                    break;
            }

            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.browser, menu);

        if (BrowserUtils.isBookmarked(this, mBrowserWebView.getUrl())) {
            // change icon color
            BrowserUtils.tintMenuIcon(getApplicationContext(), menu.getItem(0), R.color.teal_200);
        } else {
            BrowserUtils.tintMenuIcon(getApplicationContext(), menu.getItem(0), android.R.color.white);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (!mBrowserWebView.canGoBack()) {
            menu.getItem(0).setEnabled(false);
            menu.getItem(0).getIcon().setAlpha(130);
        } else {
            menu.getItem(0).setEnabled(true);
            menu.getItem(0).getIcon().setAlpha(255);
        }

        if (!mBrowserWebView.canGoForward()) {
            menu.getItem(1).setEnabled(false);
            menu.getItem(1).getIcon().setAlpha(130);
        } else {
            menu.getItem(1).setEnabled(true);
            menu.getItem(1).getIcon().setAlpha(255);
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        if (item.getItemId() == R.id.action_bookmark) {
            // bookmark / unbookmark the url
            BrowserUtils.bookmarkUrl(this, mBrowserWebView.getUrl());

            String msg =
                    BrowserUtils.isBookmarked(
                            this,
                            mBrowserWebView.getUrl())
                            ? mBrowserWebView.getTitle() + "is Bookmarked!"
                            : mBrowserWebView.getTitle() + " removed!";
            Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
            snackbar.show();

            // refresh the toolbar icons, so that bookmark icon color changes
            // depending on bookmark status
            invalidateOptionsMenu();
        }

        if (item.getItemId() == R.id.action_back) {
            back();
        }

        if (item.getItemId() == R.id.action_forward) {
            forward();
        }

        return super.onOptionsItemSelected(item);
    }

    private void back() {
        if (mBrowserWebView.canGoBack()) {
            mBrowserWebView.goBack();
        }
    }

    private void forward() {
        if (mBrowserWebView.canGoForward()) {
            mBrowserWebView.goForward();
        }
    }
}
