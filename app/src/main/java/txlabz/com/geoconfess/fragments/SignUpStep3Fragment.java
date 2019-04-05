package txlabz.com.geoconfess.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.activities.MainActivity;
import txlabz.com.geoconfess.activities.SignUpActivity;
import txlabz.com.geoconfess.constants.AppConstants;
import txlabz.com.geoconfess.events.BusProvider;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.events.LoginEvent;
import txlabz.com.geoconfess.events.RegistrationEvent;
import txlabz.com.geoconfess.helpers.AmazonImageUploader;
import txlabz.com.geoconfess.models.request.AuthRequestModel;
import txlabz.com.geoconfess.models.request.RegistrationRequestModel;
import txlabz.com.geoconfess.network.requests.LoginRequest;
import txlabz.com.geoconfess.network.requests.SignUpRequest;
import txlabz.com.geoconfess.utils.AmazonUploadListener;
import txlabz.com.geoconfess.utils.DialogUtility;
import txlabz.com.geoconfess.utils.NetworkUtils;
import txlabz.com.geoconfess.utils.SharedPreferenceUtils;
import txlabz.com.geoconfess.utils.Utils;

/**
 * Created by yagor on 27/04/2016.
 */
public class SignUpStep3Fragment extends Fragment implements View.OnClickListener {

    public static final String TAG = SignUpStep3Fragment.class.getSimpleName();
    private static final int SELECT_PICTURE = 8;
    private static final int SELECT_PICTURE_Camera = 9;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.et_confirmpassword)
    EditText et_confirmpassword;
    @BindView(R.id.picturebtn)
    LinearLayout picturebtn;
    @BindView(R.id.bottombtn)
    Button bottombtn;
    @BindView(R.id.check)
    ImageView check;
    String role;
    String imageurl = "";
    String pathOfImage1 = "";
    boolean ischeck = false;
    String notification = "0";
    private boolean registrationDone;
    private String mPassword = "", mConfirmpassword = "";
    private boolean isAccessbutton = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setBackgroundDrawableResource(R.drawable.ic_app_background);
        View view = inflater.inflate(R.layout.fragment_signup_step3, container, false);
        ButterKnife.bind(this, view);
        picturebtn.setOnClickListener(this);
        check.setOnClickListener(this);
        bottombtn.setOnClickListener(this);
        checkRole();
        //onTextChangedListener();
        return view;
    }

   /* private void onTextChangedListener() {

        et_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mPassword = s.toString().trim();
                checkButtonVisible();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_confirmpassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mConfirmpassword = s.toString().trim();
                checkButtonVisible();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }*/

    private void checkButtonVisible() {
        if (mPassword.length() < 0 && mConfirmpassword.length() < 0) {
            changeRegisterbutton(false);

        } else {
            if (mPassword.length() > 0) {
                if (mConfirmpassword.length() > 0) {
                    changeRegisterbutton(true);

                }
            }

        }

    }



    private void changeRegisterbutton(boolean isAccess) {
        if (isAccess) {
            isAccessbutton = true;
            bottombtn.setBackgroundResource(R.drawable.red_shape_in_focus);
            bottombtn.setClickable(true);
        } else {
            isAccessbutton = false;
            bottombtn.setBackgroundResource(R.drawable.grey_btn_selector);
            bottombtn.setClickable(false);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    private void checkRole() {
        role = getArguments().getString(AppConstants.REGISTRATION_ROLE);
        if (TextUtils.equals(role, AppConstants.USER_ROLE)) {
            picturebtn.setVisibility(View.GONE);
        } else {
            picturebtn.setVisibility(View.VISIBLE);
        }
    }


    public void imageSelected(File imageFile) {
        pathOfImage1 = imageFile.getAbsolutePath();
        Toast.makeText(getActivity(),R.string.amazon_image_upload, Toast.LENGTH_SHORT).show();
        AmazonUploadListener amazonListener = new AmazonUploadListener() {
            @Override
            public void imageUploaded(String url) {
                try {
                    ((SignUpActivity) getActivity()).hideDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                imageurl = url;
            }
        };
        try {
            ((SignUpActivity) getActivity()).showDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
        AmazonImageUploader amazonUpload = new AmazonImageUploader(pathOfImage1, amazonListener);
        amazonUpload.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.picturebtn:
                ((SignUpActivity) getActivity()).showPrompt();
                break;
            case R.id.check:
                notificationCheck();
                break;
            case R.id.bottombtn:
                if (validate()) {
                    Utils.savePreferencesString(getContext(),AppConstants.PASSWORD,et_password.getText().toString());

                    if (NetworkUtils.connected()) {
                        try {
                            ((SignUpActivity) getActivity()).showDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        registerAction();
                    } else {
                        DialogUtility.showDialog(getActivity(), "", getActivity().getString(R.string.no_internet_message));
                    }
                }
                break;
        }
    }




    private void registerAction() {
        if (!registrationDone) {
            RegistrationRequestModel request = new RegistrationRequestModel(role, getArguments().getString(AppConstants.REGISTRATION_EMAIL),
                    et_password.getText().toString(), getArguments().getString(AppConstants.REGISTRATION_NAME), getArguments().getString(AppConstants.REGISTRATION_SURNAME),
                    getArguments().getString(AppConstants.REGISTRATION_PHONE), imageurl, ischeck, true);
            SignUpRequest.singUp(request);
        } else {
            loginAction();
        }
    }



    private void notificationCheck() {
        if (ischeck) {
            check.setImageResource(R.drawable.checkbox);
            ischeck = false;
            notification = "0";
        } else {
            check.setImageResource(R.drawable.checked);
            ischeck = true;
            notification = "1";
        }
    }



    public boolean validate() {
        boolean valid = true;
        if (et_password.getText().toString().trim().length() == 0 || et_password.getText().toString().trim().length() < 6) {
            DialogUtility.showDialog(getActivity(), getString(R.string.mobnum_notvalid_title), getString(R.string.short_pass));
            et_password.requestFocus();
            valid = false;
        } else if (et_confirmpassword.getText().toString().trim().length() == 0 || et_confirmpassword.getText().toString().trim().length() < 6) {
            DialogUtility.showDialog(getActivity(), getString(R.string.mobnum_notvalid_title), getString(R.string.short_pass));
            et_confirmpassword.requestFocus();
            valid = false;
        } else if (!et_confirmpassword.getText().toString().equals(et_password.getText().toString())) {
            DialogUtility.showDialog(getActivity(), getString(R.string.password_confirm_title), getString(R.string.password_confirm_messg));
            et_confirmpassword.requestFocus();
            valid = false;
        } else if ((imageurl == "") && (TextUtils.equals((getArguments().getString(AppConstants.REGISTRATION_ROLE)), AppConstants.PRIEST_ROLE))) {
            DialogUtility.showDialog(getActivity(), getString(R.string.imageupload_title), getString(R.string.imageupload_notvalid));
            valid = false;
        }


        return valid;
    }



   /* public boolean validate() {
        boolean valid = true;
        if (et_password.length()==0 || et_password.length() < 6 || et_confirmpassword.length() < 6) {
            DialogUtility.showDialog(getActivity(), getString(R.string.mobnum_notvalid_title), getString(R.string.short_pass));
            et_password.requestFocus();
            valid = false;
        } else if (!et_confirmpassword.getText().toString().equals(et_password.getText().toString())) {
            DialogUtility.showDialog(getActivity(), getString(R.string.password_confirm_title), getString(R.string.password_confirm_messg));
            et_confirmpassword.requestFocus();
            valid = false;
        } else if ((imageurl == "") && (TextUtils.equals((getArguments().getString(AppConstants.REGISTRATION_ROLE)), AppConstants.PRIEST_ROLE))) {
//            Snackbar snackbar = Snackbar
//                    .make(getView().findViewById(R.id.sign_up_relative_layout), R.string.imageupload_notvalid, Snackbar.LENGTH_LONG);
//            snackbar.show();
            DialogUtility.showDialog(getActivity(), getString(R.string.imageupload_title), getString(R.string.imageupload_notvalid));

            valid = false;
        }


        return valid;
    }*/

    @Subscribe
    public void onEvent(RegistrationEvent event) {
        registrationDone = true;
        loginAction();
    }



    private void loginAction() {
        if (!SharedPreferenceUtils.getPushToken(getActivity()).isEmpty()) {
            AuthRequestModel request = new AuthRequestModel(AppConstants.GRANT_TYPE,
                    getArguments().getString(AppConstants.REGISTRATION_EMAIL),
                    et_password.getText().toString(), AppConstants.OS, SharedPreferenceUtils.getPushToken(getActivity()));
            LoginRequest.login(request);
        } else {
            ((SignUpActivity) getActivity()).hideDialog();
            Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void onEvent(LoginEvent event) {
        ((SignUpActivity) getActivity()).hideDialog();
        if (event.isResponseValid()) {
            SharedPreferenceUtils.setLogInRole(getActivity(), role);
            SharedPreferenceUtils.setUserNameSurname(getActivity(), getString(R.string.two_resources_format,
                    getArguments().getString(AppConstants.REGISTRATION_NAME),
                    getArguments().getString(AppConstants.REGISTRATION_SURNAME)));
            SharedPreferenceUtils.setUserEmail(getActivity(), getArguments().getString(AppConstants.REGISTRATION_EMAIL));
            SharedPreferenceUtils.setAccessToken(getActivity(), event.getResponse().getAccessToken());
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            ((SignUpActivity) getActivity()).finish();
        }
        Log.i(TAG, "event accesToken" + event.getResponse().getAccessToken());
    }

    @Subscribe
    public void onEvent(ErrorResponseEvent event) {
        ((SignUpActivity) getActivity()).hideDialog();
        if (event.getError().getErrors().equalsIgnoreCase("email-has already been taken. ")) {
            Toast.makeText(getActivity(), getString(R.string.error_msg), Toast.LENGTH_LONG).show();
        } else {
            Log.e("errorrrrr", event.getError().getErrors().toString());
            Toast.makeText(getActivity(), event.getError().getErrors(), Toast.LENGTH_LONG).show();
        }
    }
}
