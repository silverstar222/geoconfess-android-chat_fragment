package txlabz.com.geoconfess.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.activities.SignUpActivity;
import txlabz.com.geoconfess.constants.AppConstants;
import txlabz.com.geoconfess.utils.DialogUtility;

/**
 * Created by yagor on 27/04/2016.
 */
public class SignUpStep2Fragment extends Fragment implements View.OnClickListener {

    @BindViews({R.id.et_surname, R.id.et_name, R.id.et_email, R.id.et_phon})
    List<EditText> allEditTextFields;
    @BindView(R.id.et_surname)
    EditText et_surname;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_phon)
    EditText et_phon;

    @BindView(R.id.parentview)
    RelativeLayout activityRootView;
    @BindView(R.id.bottombtn)
    Button bottombtn;

    private String mFirstname = "", mLastname = "", mEmail = "";
    private boolean isAccessbutton = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup_step2, container, false);

        ButterKnife.bind(this, view);

        bottombtn.setOnClickListener(this);
//        onTextChangedListener();
        return view;
    }


    private void onTextChangedListener() {
        et_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mFirstname = s.toString().trim();
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
        et_surname.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mLastname = s.toString().trim();
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
        et_email.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mEmail = s.toString().trim();
                if (mEmail.length() <= 0) {
                    DialogUtility.showDialog(getActivity(), getString(R.string.email_valid_titile), getString(R.string.invalid_email_add));
                }
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
    }


    private boolean checkButtonVisible() {
        bottombtn.setClickable(true);
        if (et_name.getText().toString().trim().length() == 0) {
            et_name.requestFocus();
            DialogUtility.showDialog(getActivity(), getString(R.string.surname), getString(R.string.require_field));
        } else if (et_surname.getText().toString().trim().length() == 0) {
            et_surname.requestFocus();
            DialogUtility.showDialog(getActivity(), getString(R.string.first_name_title), getString(R.string.require_field));
            return false;
        } else if (et_email.getText().toString().trim().length() == 0) {
            et_email.requestFocus();
            DialogUtility.showDialog(getActivity(), getString(R.string.email_valid_titile), getString(R.string.require_field));
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString().trim()).matches()) {
            et_email.requestFocus();
            DialogUtility.showDialog(getActivity(), getString(R.string.email_valid_titile), getString(R.string.email_valid_message));
            return false;
        }
       /* else if ((et_phon.getText().length() ==0)) {
            et_phon.requestFocus();
            DialogUtility.showDialog(getActivity(), getString(R.string.phone), getString(R.string.require_field));
            return false;
        }
        else if ((et_phon.getText().length() < 10)) {
            et_phon.requestFocus();
            DialogUtility.showDialog(getActivity(), getString(R.string.phone), getString(R.string.mobnum_notvalid));
            return false;
        }*/
        else {
            return true;
        }
        return false;
    }


/*
//        boolean valid = true;
//        if (mFirstname.length() < 0 && mLastname.length() < 0 && mEmail.length() < 0) {
//            changeRegisterbutton(false);
//            valid = false;
//
//        }
//        else {
//            if (mFirstname.length() > 0) {
//                valid = false;
//                if (mLastname.length() > 0) {
//                    valid = false;
//                    if (mEmail.length() > 0) {
//                        changeRegisterbutton(true);
//                        valid = true;
//
//                    }
//
//                }
//            }
//        }
//
//        return valid;

        //}*/

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
        setEditTexts();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                break;
            case R.id.forgotPasswordlabel:
                break;
            case R.id.bottombtn:
                if (checkButtonVisible()) {
                    Bundle bundle = new Bundle();
                    bundle.putString(AppConstants.REGISTRATION_NAME, et_name.getText().toString());
                    bundle.putString(AppConstants.REGISTRATION_SURNAME, et_surname.getText().toString());
                    bundle.putString(AppConstants.REGISTRATION_EMAIL, et_email.getText().toString());
                    bundle.putString(AppConstants.REGISTRATION_PHONE, et_phon.getText().toString());
                    bundle.putString(AppConstants.REGISTRATION_ROLE, getArguments().getString(AppConstants.REGISTRATION_ROLE));
                    SignUpStep3Fragment fragment = new SignUpStep3Fragment();
                    fragment.setArguments(bundle);
                    ((SignUpActivity) getActivity()).loadFragment(fragment);
//                    if(et_email.getText().toString().trim().isEmpty()){
//                        DialogUtility.showDialog(getActivity(), getString(R.string.email_valid_titile), getString(R.string.invalid_email_add));
//
//                    }
//                    else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
//                        DialogUtility.showDialog(getActivity(), getString(R.string.email_valid_titile), getString(R.string.email_valid_message));
//                    } else {
//                        //      Phone is not necessary but if provided then should be 10 or 11 digits long
//                        if ((et_phon.getText().length() > 0) && ((et_phon.getText().length() < 10) || (et_phon.getText().length() > 11))) {
//                            DialogUtility.showDialog(getActivity(), getString(R.string.mobnum_notvalid_title), getString(R.string.mobnum_notvalid));
//                        } else {
//                            Bundle bundle = new Bundle();
//                            bundle.putString(AppConstants.REGISTRATION_NAME, et_name.getText().toString());
//                            bundle.putString(AppConstants.REGISTRATION_SURNAME, et_surname.getText().toString());
//                            bundle.putString(AppConstants.REGISTRATION_EMAIL, et_email.getText().toString());
//                            bundle.putString(AppConstants.REGISTRATION_PHONE, et_phon.getText().toString());
//                            bundle.putString(AppConstants.REGISTRATION_ROLE, getArguments().getString(AppConstants.REGISTRATION_ROLE));
//                            SignUpStep3Fragment fragment = new SignUpStep3Fragment();
//                            fragment.setArguments(bundle);
//                            ((SignUpActivity) getActivity()).loadFragment(fragment);
//                        }
//
//                    }

                }
                break;
            case R.id.signUplabel:
                ((SignUpActivity) getActivity()).loadFragment(new SignUpStep1Fragment());
                break;
        }
    }

    private void setEditTexts() {
        for (EditText editTextField : allEditTextFields) {
            if (editTextField.getText().length() > 0) {
                editTextField.setHintTextColor(getResources().getColor(R.color.colorGreyTextHint));
                editTextField.setTextColor(getResources().getColor(R.color.colorGreyTextHint));
            }
            editTextField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText field = (EditText) v;
                    if (hasFocus) {
                        field.setHintTextColor(getResources().getColor(R.color.white));
                        field.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        field.setHintTextColor(getResources().getColor(R.color.colorGreyTextHint));
                        field.setTextColor(getResources().getColor(R.color.colorGreyTextHint));
                    }
                }
            });
        }
    }

    private boolean validation() {
        boolean valid = true;
        if (et_name.getText().toString().equalsIgnoreCase("")) {
            et_name.setError(getString(R.string.empty_field));
            valid = false;
        }

        if (et_surname.getText().toString().equalsIgnoreCase("")) {
            et_surname.setError(getString(R.string.empty_field));
            valid = false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(String.valueOf(et_email.getText())).matches()) {
            et_email.setError(getString(R.string.invalid_email));
            valid = false;
        }
        if (et_email.getText().toString().equalsIgnoreCase("")) {
            et_email.setError(getString(R.string.no_email));
            valid = false;
        }
//      Phone is not necessary but if provided then should be 10 or 11 digits long
        if ((et_phon.getText().toString().length() > 0) && ((et_phon.getText().toString().length() < 10) || (et_phon.getText().toString().length() > 11))) {
            et_phon.setError(getString(R.string.wrong_phone));
            valid = false;
        }
        return valid;
    }
}
