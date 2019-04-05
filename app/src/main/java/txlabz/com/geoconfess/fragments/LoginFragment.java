package txlabz.com.geoconfess.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.models.request.AuthRequestModel;
import txlabz.com.geoconfess.network.requests.LoginRequest;
import txlabz.com.geoconfess.utils.SharedPreferenceUtils;

/**
 * Created by yagor on 27/04/2016.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    EditText username;
    EditText password;
    Button loginButton;
    View usernameDivider;
    View passwordDivider;
    TextView forgotPassword;
    TextView signUp;
//    LinearLayout googlePlus;
//    LinearLayout facebook;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        username = (EditText) view.findViewById(R.id.username);
//        googlePlus=(LinearLayout)view.findViewById(R.id.googlePlus);
//        facebook=(LinearLayout)view.findViewById(R.id.facebook);

        password = (EditText) view.findViewById(R.id.password);
        loginButton = (Button) view.findViewById(R.id.loginButton);

        usernameDivider = view.findViewById(R.id.usernameDivider);
        passwordDivider = view.findViewById(R.id.passwordDivider);

        forgotPassword = (TextView) view.findViewById(R.id.forgotPasswordlabel);
        signUp = (TextView) view.findViewById(R.id.signUplabel);

        loginButton.setOnClickListener(this);
        signUp.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);

        setOnTouchListenerForPassword(password);
        setOnTouchListenerForUserName(username);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        handleOnTouchUserName();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                AuthRequestModel request = new AuthRequestModel("password", "", password.getText().toString(),
                        "android", SharedPreferenceUtils.getPushToken(getActivity()));
                LoginRequest.login(request);
//                Call<AuthResponse> oathAPICall = AppApiController.getApiInstance().oathToken("password", username.getText().toString(), password.getText().toString(),
//                        "android", "3kjh123iu42i314g123");
//                ((MainActivity)getActivity()).showDialog();
//                oathAPICall.enqueue(new Callback<AuthResponse>() {
//                    @Override
//                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
//                        AuthResponse responseModel = response.body();
//                        ((MainActivity)getActivity()).hideDialog();
//                        if(response.isSuccessful()) {
//                            //  DialogUtility.showDialog(getActivity(), "Message", "Success.");
//                        //    Log.d("respncve",responseModel);
//
//
//                            Utils.saveDataString("token",responseModel.getAccessToken(),getActivity());
//
//                            Intent intent=new Intent(getActivity(), HomeActivity.class);
//                            startActivity(intent);
//                            getActivity().finish();
//
//
//                        } else {
//                            DialogUtility.showDialog(getActivity(), "Message", "Error.");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<AuthResponse> call, Throwable t) {
//                        ((MainActivity)getActivity()).hideDialog();
//                        DialogUtility.showDialog(getActivity(), "Message", "Error.");
//                    }
//                });
//                GeneralUtility.hideKeyBoard(getActivity());
                break;
            case R.id.forgotPasswordlabel:

                break;
            case R.id.signUplabel:
                break;
//            case R.id.facebook:
//                break;
//            case R.id.googlePlus:
//                break;


        }
    }

    private void setOnTouchListenerForPassword(final EditText edit_Text) {
        edit_Text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
                    handleOnTouchPassword();
                    handleOnTouchUserName();
                }
                return false;
            }
        });
        edit_Text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                handleOnTouchPassword();

            }
        });


    }

    private void setOnTouchListenerForUserName(final EditText edit_Text) {
        edit_Text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
                    handleOnTouchUserName();
                }
                return false;
            }
        });

        edit_Text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b) {
                    handleOnTouchUserName();
                }

            }
        });

    }

    private void handleOnTouchUserName() {

        username.setBackgroundResource(R.color.red_edit_text_background);
        password.setBackgroundResource(R.color.white);

        username.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_identification_on, 0, 0, 0);
        password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_password_off, 0, 0, 0);

        password.setHintTextColor(getResources().getColor(R.color.colorGreyTextHint));
        username.setHintTextColor(getResources().getColor(R.color.white));

        username.setTextColor(getResources().getColor(R.color.white));
        password.setTextColor(getResources().getColor(R.color.colorGreyTextHint));

        passwordDivider.setVisibility(View.VISIBLE);
        usernameDivider.setVisibility(View.GONE);
    }

    private void handleOnTouchPassword() {
        password.setBackgroundResource(R.color.red_edit_text_background);
        username.setBackgroundResource(R.color.white);

        password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_password_on, 0, 0, 0);
        username.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_identification_off, 0, 0, 0);

        password.setHintTextColor(getResources().getColor(R.color.white));
        username.setHintTextColor(getResources().getColor(R.color.colorGreyTextHint));

        username.setTextColor(getResources().getColor(R.color.colorGreyTextHint));
        password.setTextColor(getResources().getColor(R.color.white));

        passwordDivider.setVisibility(View.GONE);
        usernameDivider.setVisibility(View.VISIBLE);
    }

}
