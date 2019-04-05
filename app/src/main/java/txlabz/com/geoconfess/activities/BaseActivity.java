package txlabz.com.geoconfess.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.events.BusProvider;
import txlabz.com.geoconfess.views.CustomProgressDialog;

/**
 * Created by Ivan on 17.5.2016..
 */
public class BaseActivity extends AppCompatActivity {

    public static ProgressDialog progressDialog;

    public static void showDialog() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    public static void hideDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.ic_app_background);
        progressDialog = CustomProgressDialog.ctor(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }
}
