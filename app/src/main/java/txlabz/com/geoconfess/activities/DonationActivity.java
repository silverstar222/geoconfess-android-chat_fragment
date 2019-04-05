package txlabz.com.geoconfess.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import txlabz.com.geoconfess.R;

public class DonationActivity extends AppCompatActivity {
    @BindView(R.id.web_view)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
        ButterKnife.bind(this);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://donner.ktotv.com/a/mon-don");
    }
}
