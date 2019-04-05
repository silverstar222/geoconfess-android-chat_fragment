package txlabz.com.geoconfess.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.constants.AppConstants;
import txlabz.com.geoconfess.events.UserEvent;
import txlabz.com.geoconfess.models.UpdateUserRequestModel;
import txlabz.com.geoconfess.network.requests.UpdateUserRequest;
import txlabz.com.geoconfess.network.requests.UserDataRequest;
import txlabz.com.geoconfess.utils.DialogUtility;
import txlabz.com.geoconfess.utils.NetworkUtils;
import txlabz.com.geoconfess.utils.SharedPreferenceUtils;
import txlabz.com.geoconfess.utils.Utils;

/**
 * Created by Dell on 7/7/2016.
 */
public class ProfileFragment extends BaseFragment {

    @BindView(R.id.name_edt)
    EditText mNameedt;
    @BindView(R.id.surname_edt)
    EditText mSurnameedt;
    @BindView(R.id.email_edt)
    EditText mEmailedt;
    @BindView(R.id.phone_edt)
    EditText mPhoneedt;
    @BindView(R.id.bottombtn)
    LinearLayout mBottombtn;
    @BindView(R.id.button_status)
    TextView mTxtbutton;
    private boolean mEditable = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        Notallowededit();
        if (NetworkUtils.connected()) {
            getUserProfile();
        } else {
            DialogUtility.showDialog(getActivity(),"", getActivity().getString(R.string.no_internet_message));
        }

        return view;
    }

    private void getUserProfile() {
        UserDataRequest.getUserData(SharedPreferenceUtils.getAccessToken(getActivity()));
    }

    public void Notallowededit() {

        mNameedt.setFocusable(false);
        mSurnameedt.setFocusable(false);
        mEmailedt.setFocusable(false);
        mPhoneedt.setFocusable(false);

        mNameedt.setFocusableInTouchMode(false);
        mSurnameedt.setFocusableInTouchMode(false);
        mEmailedt.setFocusableInTouchMode(false);
        mPhoneedt.setFocusableInTouchMode(false);

        mNameedt.setEnabled(false);
        mSurnameedt.setEnabled(false);
        mEmailedt.setEnabled(false);
        mPhoneedt.setEnabled(false);

    }

    public void allowedit() {
        mNameedt.setFocusable(true);
        mSurnameedt.setFocusable(true);
        mEmailedt.setFocusable(true);
        mPhoneedt.setFocusable(true);

        mNameedt.setFocusableInTouchMode(true);
        mSurnameedt.setFocusableInTouchMode(true);
        mEmailedt.setFocusableInTouchMode(true);
        mPhoneedt.setFocusableInTouchMode(true);

        mNameedt.setEnabled(true);
        mSurnameedt.setEnabled(true);
        mEmailedt.setEnabled(true);
        mPhoneedt.setEnabled(true);
    }

    @Subscribe
    public void onEvent(UserEvent event) {
        mNameedt.setText(event.getUser().getName());
        mSurnameedt.setText(event.getUser().getSurname());
        mEmailedt.setText(event.getUser().getEmail());
        mPhoneedt.setText(event.getUser().getPhone());
        Utils.savePreferencesBoolean(getActivity(), String.valueOf(AppConstants.NOTIFICATION), event.getUser().isNotification());
        Utils.savePreferencesBoolean(getActivity(), String.valueOf(AppConstants.NEWSLETTER), event.getUser().isNewsletter());
    }

    @SuppressLint("SetTextI18n")
    @OnClick({R.id.bottombtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottombtn:
                if (!mEditable) {
                    mTxtbutton.setText("Valider");
                    mBottombtn.setBackgroundResource(R.drawable.red_backgroung_list);
                    allowedit();
                    mEditable = true;
                }
                else {
                    Utils.hideKeyboard2(getContext());
                    if(validate()){
                        mBottombtn.setBackgroundResource(R.drawable.linear_btn_selector);
                        Notallowededit();
                        mTxtbutton.setText("Modifier");
                        mEditable = false;
                        if (NetworkUtils.connected()) {
                            UserEditApi();
                        } else {
                            DialogUtility.showDialog(getActivity(), "", getActivity().getString(R.string.no_internet_message));
                        }
                    }
                }

                break;
        }

    }
    private boolean validate() {
        if (mSurnameedt.getText().toString().trim().length() == 0) {
            mSurnameedt.requestFocus();
            DialogUtility.showDialog(getActivity(), getString(R.string.error), getString(R.string.profile_error));
        } else if (mNameedt.getText().toString().trim().length() == 0) {
            mNameedt.requestFocus();
            DialogUtility.showDialog(getActivity(), getString(R.string.error), getString(R.string.profile_error));
            return false;
        } else if (mEmailedt.getText().toString().trim().length() == 0) {
            mEmailedt.requestFocus();
            DialogUtility.showDialog(getActivity(), getString(R.string.error), getString(R.string.profile_error));
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmailedt.getText().toString().trim()).matches()) {
            mEmailedt.requestFocus();
            DialogUtility.showDialog(getActivity(), getString(R.string.error), getString(R.string.profile_error));
            return false;
        }
        else if (mPhoneedt.getText().toString().trim().length() == 0) {
            DialogUtility.showDialog(getActivity(), getString(R.string.error), getString(R.string.profile_error));
            mPhoneedt.requestFocus();
            return false;
        }
        else {
            return true;
        }
        return false;
    }

    private void UserEditApi() {
        UpdateUserRequestModel request = new UpdateUserRequestModel(SharedPreferenceUtils.getAccessToken(getActivity()), mEmailedt.getText().toString(), Utils.getPreferencesString(getContext(),AppConstants.PASSWORD), mNameedt.getText().toString(), mSurnameedt.getText().toString(), mPhoneedt.getText().toString(), Utils.getPreferencesBoolean(getActivity(), String.valueOf(AppConstants.NOTIFICATION)), Utils.getPreferencesBoolean(getActivity(), String.valueOf(AppConstants.NEWSLETTER)));
        UpdateUserRequest.update(request);
        DialogUtility.showDialog(getActivity(), "Profil", getString(R.string.profile_success));
    }
}
