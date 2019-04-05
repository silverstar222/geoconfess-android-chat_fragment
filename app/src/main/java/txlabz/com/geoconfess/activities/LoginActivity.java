package txlabz.com.geoconfess.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.internal.Util;
import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.constants.AppConstants;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.events.LoginEvent;
import txlabz.com.geoconfess.models.request.AuthRequestModel;
import txlabz.com.geoconfess.network.requests.LoginRequest;
import txlabz.com.geoconfess.utils.DialogUtility;
import txlabz.com.geoconfess.utils.NetworkUtils;
import txlabz.com.geoconfess.utils.SharedPreferenceUtils;
import txlabz.com.geoconfess.utils.Utils;

public class LoginActivity extends ImageChooseActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();
    private static final int REQUEST_LOCATION = 2;
    static public Double longitude = null;
    static public Double latitude = null;
    @BindViews({R.id.username, R.id.password})
    List<EditText> emailAndPass;
    @BindView(R.id.username)
    EditText mUserNameField;
    @BindView(R.id.password)
    EditText mPasswordField;
    @BindView(R.id.loginButton)
    Button loginButton;
    @BindView(R.id.forgotPasswordlabel)
    TextView forgotPassword;
    @BindView(R.id.signUplabel)
    TextView signUp;
    private String mEmail = "";
    private String mPassword = "";
    private boolean permissionsGranted;
    private LocationManager locationManager;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawableResource(R.drawable.ic_app_background);
        Utils.hideKeyboard2(LoginActivity.this);
        updateView();
    }

    private void updateView() {
        //set email from preference
        if (SharedPreferenceUtils.getUserEmail(this) != null) {
            mUserNameField.setText(SharedPreferenceUtils.getUserEmail(this));
        }

        setEdiTextListener();
    }

    private void setEdiTextListener() {
        mUserNameField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText field = (EditText) v;
                if (hasFocus) {
                    field.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_identification_on, 0, 0, 0);
                    field.setHintTextColor(getResources().getColor(R.color.white));
                    field.setTextColor(getResources().getColor(R.color.white));
                } else {
                    field.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_identification_off, 0, 0, 0);
                    field.setHintTextColor(getResources().getColor(R.color.colorGreyTextHint));
                    field.setTextColor(getResources().getColor(R.color.colorGreyTextHint));
                }
            }
        });
        mPasswordField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText field = (EditText) v;
                if (hasFocus) {
                    field.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_password_on, 0, 0, 0);
                    field.setHintTextColor(getResources().getColor(R.color.white));
                    field.setTextColor(getResources().getColor(R.color.white));
                } else {
                    field.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_password_off, 0, 0, 0);
                    field.setHintTextColor(getResources().getColor(R.color.colorGreyTextHint));
                    field.setTextColor(getResources().getColor(R.color.colorGreyTextHint));
                }
            }
        });
    }

    @OnClick({R.id.loginButton, R.id.forgotPasswordlabel, R.id.signUplabel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                if (checkIfValid()) {
                    if (NetworkUtils.connected()) {
                        loginAction();
                    } else {
                        DialogUtility.showDialog(LoginActivity.this, "", LoginActivity.this.getString(R.string.no_internet_message));
                    }
                }
                break;
            case R.id.forgotPasswordlabel:
                Intent forgot = new Intent(this, ForgotPasswordActivity.class);
                startActivity(forgot);
                break;
            case R.id.signUplabel:
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }


    private void loginAction() {
        if (permissionsGranted && SharedPreferenceUtils.getPushToken(this) != null) {
            try {
                showDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Utils.savePreferencesString(LoginActivity.this,AppConstants.PASSWORD,mPasswordField.getText().toString());
            AuthRequestModel request = new AuthRequestModel(AppConstants.GRANT_TYPE, mEmail, mPassword, AppConstants.OS, SharedPreferenceUtils.getPushToken(this));
            LoginRequest.login(request);
        } else if (!permissionsGranted) {
            checkPermissions();
        } else {
        }
    }

    private boolean checkIfValid() {
        boolean valid = true;
//        for (EditText emailAndPas : emailAndPass) {
//            if (emailAndPas.getId() == R.id.username) {
//                mEmail = String.valueOf(emailAndPas.getText());
//                if (mEmail.isEmpty()) {
//                    DialogUtility.showDialog(this, "", getString(R.string.empty_field));
//                    valid = false;
//                }
//            } else if (emailAndPas.getId() == R.id.password) {
//                mPassword = String.valueOf(emailAndPas.getText());
//                if (mPassword.isEmpty()) {
//                    DialogUtility.showDialog(this, "", getString(R.string.password_empty));
//                    valid = false;
//                }
//            }
//        }
        mEmail = mUserNameField.getText().toString();
        mPassword = mPasswordField.getText().toString();
        if (mUserNameField.getText().toString().isEmpty()) {
            DialogUtility.showDialog(this, "", getString(R.string.empty_field));
            valid = false;
        } else if (mPasswordField.getText().toString().isEmpty()) {
            valid = false;
            DialogUtility.showDialog(this, "", getString(R.string.password_empty));
        }
        return valid;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermissions()) {
            permissionsGranted = true;
        }
    }

    @Override
    public void onMediaChosen(File imageFile) {

    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 2
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionsGranted = true;
            } else {
                permissionsGranted = false;
            }
        }
    }

    @Subscribe
    public void onEvent(LoginEvent event) {
        hideDialog();
        if (event.isResponseValid()) {
            SharedPreferenceUtils.setUserEmail(this, mEmail);
            SharedPreferenceUtils.setAccessToken(this, event.getResponse().getAccessToken());
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        Log.i(TAG, "event accesToken" + event.getResponse().getAccessToken());
    }

    @Subscribe
    public void onEvent(ErrorResponseEvent event) {
        hideDialog();
        DialogUtility.showDialog(this, "", getString(R.string.incorrect_user));

    }
}
