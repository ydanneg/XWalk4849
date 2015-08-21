package com.example.xwalk4849;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private XWalkView webView;
    private ProgressBar progress;
    private View error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        error = findViewById(R.id.activity_main_error);
        error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRetry();
            }
        });

        progress = (ProgressBar) findViewById(R.id.activity_main_progress);
        webView = (XWalkView) findViewById(R.id.activity_main_web);
        webView.setUIClient(new XWalkUIClient(webView) {
            @Override
            public void onPageLoadStarted(XWalkView view, String url) {
                Log.d(TAG, "onPageLoadStarted: " + url);
                super.onPageLoadStarted(view, url);
                // show progress above the web view
                showProgress();
            }

            @Override
            public void onPageLoadStopped(XWalkView view, String url, LoadStatus status) {
                Log.d(TAG, "onPageLoadStopped: " + status);
                super.onPageLoadStopped(view, url, status);
                // hide progress
                hideProgress();
            }
        });

        webView.setResourceClient(new XWalkResourceClient(webView) {
            @Override
            public void onReceivedLoadError(XWalkView view, int errorCode, String description, String failingUrl) {
                Log.d(TAG, "onReceivedLoadError: " + errorCode + ", msg: " + description);
                // show error
                showError();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.load("http://localhost:2334", null);
    }

    private void onRetry() {
        hideError();
        webView.reload(XWalkView.RELOAD_NORMAL);
    }

    private void hideError() {
        // show web view to hide an error view
        webView.setVisibility(View.VISIBLE);
    }

    private void showError() {
        // just remove web view to view an error view :)
        webView.setVisibility(View.GONE);

    }

    private void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progress.setVisibility(View.GONE);
    }
}
