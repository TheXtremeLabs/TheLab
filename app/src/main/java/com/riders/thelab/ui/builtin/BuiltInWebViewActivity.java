package com.riders.thelab.ui.builtin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.riders.thelab.R;
import com.riders.thelab.core.utils.BrowserUtils;
import com.riders.thelab.ui.base.SimpleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class BuiltInWebViewActivity extends SimpleActivity {

    public static final String EXTRA_POST_URL = "postUrl";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.built_in_webView)
    WebView mBuiltInWebView;
    @BindView(R.id.built_in_progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.backdrop)
    ImageView imgHeader;

    private String postUrl = "http://api.androidhive.info/webview/index.html";
    private float m_downX;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_built_in_webview);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (!TextUtils.isEmpty(getIntent().getStringExtra(EXTRA_POST_URL))) {
            postUrl = getIntent().getStringExtra(EXTRA_POST_URL);
        }

        initWebView();
        initCollapsingToolbar();
        renderPost();
    }

    @SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled"})
    private void initWebView() {
        mBuiltInWebView.setWebChromeClient(new MyWebChromeClient(this));
        mBuiltInWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                /**
                 * Check for the url, if the url is from same domain
                 * open the url in the same activity as new intent
                 * else pass the url to browser activity
                 * */
                if (BrowserUtils.isSameDomain(postUrl, url)) {
                    Intent intent = new Intent(
                            BuiltInWebViewActivity.this,
                            BuiltInWebViewActivity.class);
                    intent.putExtra("postUrl", url);
                    startActivity(intent);
                } else {
                    // launch in-app browser i.e BrowserActivity
                    openInAppBrowser(url);
                }

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
            }
        });
        mBuiltInWebView.clearCache(true);
        mBuiltInWebView.clearHistory();
        mBuiltInWebView.getSettings().setJavaScriptEnabled(true);
        mBuiltInWebView.setHorizontalScrollBarEnabled(false);
        mBuiltInWebView.setOnTouchListener((v, event) -> {

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

    private void renderPost() {
        mBuiltInWebView.loadUrl(postUrl);
    }

    private void openInAppBrowser(String url) {
        Intent intent = new Intent(
                BuiltInWebViewActivity.this,
                BuiltInBrowserActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar txtPostTitle on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the txtPostTitle when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Web View");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });

        // loading toolbar header image
        Glide.with(getApplicationContext())
                .load("http://api.androidhive.info/webview/nougat.jpg")
                /*.thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)*/
                .into(imgHeader);
    }
}
