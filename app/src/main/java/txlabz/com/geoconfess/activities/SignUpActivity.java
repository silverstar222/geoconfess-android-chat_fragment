package txlabz.com.geoconfess.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;

import java.io.File;

import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.fragments.SignUpStep1Fragment;
import txlabz.com.geoconfess.fragments.SignUpStep3Fragment;
import txlabz.com.geoconfess.utils.GeneralUtility;

public class SignUpActivity extends ImageChooseActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initSignUpFragment();
    }

    private void initSignUpFragment() {
        SignUpStep1Fragment myf = new SignUpStep1Fragment();
        loadFragment(myf);
    }

    public void loadFragment(final Fragment fragment) {
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.sign_up_frame, fragment, fragment.getClass().getSimpleName());
        Log.i(TAG, "open fragment: " + fragment.getClass().getSimpleName());
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return true;

    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() == 1) {
            finish();
        }
        GeneralUtility.hideKeyBoard(this);
        super.onBackPressed();

    }

    @Override
    public void onMediaChosen(File imageFile) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(SignUpStep3Fragment.TAG);
        if (fragment != null) {
            ((SignUpStep3Fragment) fragment).imageSelected(imageFile);
        }
    }
}
