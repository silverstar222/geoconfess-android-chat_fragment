package txlabz.com.geoconfess.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.constants.AppConstants;

/**
 * Created by Dell on 7/7/2016.
 */
public class QuestConfessionFragment extends BaseFragment {


    private TextView mTitletxt;
    private WebView mWebview;
    private String mType = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_confession, container, false);
        mWebview = (WebView) v.findViewById(R.id.web_view1);
        mTitletxt = (TextView) v.findViewById(R.id.title_txt);


        // Enable Javascript
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);


        mWebview.setWebViewClient(new WebViewClient());
        mType = getArguments().getString(AppConstants.FOR_QUESTCONFESSION);
        switch (mType) {
            case AppConstants.FOR_QUESTONE:
                mWebview.loadUrl("file:///android_asset/Questconfession.html");
                break;
            case AppConstants.FOR_QUESTTWO:
                mWebview.loadUrl("file:///android_asset/pourquoiseconfesser.html");
                break;
            case AppConstants.FOR_QUESTTHREE:
                mWebview.loadUrl("file:///android_asset/commentseconfesser.html");
                break;
        }


        return v;
    }

    private class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);

        }
    }

}
