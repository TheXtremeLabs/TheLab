package com.riders.thelab.ui.builtin;

import android.content.Context;
import android.webkit.WebChromeClient;

public class MyWebChromeClient extends WebChromeClient {
    Context context;

    public MyWebChromeClient(Context context) {
        super();
        this.context = context;
    }
}
