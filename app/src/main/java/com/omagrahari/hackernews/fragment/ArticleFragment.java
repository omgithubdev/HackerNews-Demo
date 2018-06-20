package com.omagrahari.hackernews.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.omagrahari.hackernews.R;

public class ArticleFragment extends Fragment {

    private boolean urlExist;
    private String url;
    private FrameLayout noItemFound, networkLayout, progressBar;
    private NestedScrollView mainContent;
    private WebView webView;
    private View rootView;

    /**
     * Constructor
     */
    public ArticleFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_article, container, false);

        urlExist = getArguments().getBoolean("URL_EXIST");
        if (urlExist) {
            url = getArguments().getString("URL");
        }

        setViews();
        return rootView;
    }

    public void setViews() {
        mainContent = (NestedScrollView) rootView.findViewById(R.id.main_content);
        noItemFound = (FrameLayout) rootView.findViewById(R.id.noitemfound);
        networkLayout = (FrameLayout) rootView.findViewById(R.id.network_problem);
        progressBar = (FrameLayout) rootView.findViewById(R.id.progressLayout);
        webView = (WebView) rootView.findViewById(R.id.webview);

        networkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupUiWithData();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupUiWithData();
    }

    public void setupUiWithData() {
        if (urlExist) {
            noItemFound.setVisibility(View.GONE);
            networkLayout.setVisibility(View.GONE);

            //load webpage
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);
            webView.requestFocus();

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    progressBar.setVisibility(View.VISIBLE);
                    mainContent.setVisibility(View.GONE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    progressBar.setVisibility(View.GONE);
                    mainContent.setVisibility(View.VISIBLE);
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    progressBar.setVisibility(View.GONE);
                    networkLayout.setVisibility(View.VISIBLE);
                    mainContent.setVisibility(View.GONE);
                }
            });

        } else {
            mainContent.setVisibility(View.GONE);
            noItemFound.setVisibility(View.VISIBLE);
            networkLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }
}
