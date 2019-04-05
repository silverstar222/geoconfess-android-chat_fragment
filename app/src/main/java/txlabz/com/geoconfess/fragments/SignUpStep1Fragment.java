package txlabz.com.geoconfess.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.activities.SignUpActivity;
import txlabz.com.geoconfess.constants.AppConstants;

/**
 * Created by yagor on 27/04/2016.
 */
public class SignUpStep1Fragment extends Fragment implements View.OnClickListener {

    Button btnUser;
    Button btnPriest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup_step1, container, false);
        btnUser = (Button) view.findViewById(R.id.btn_user);
        btnPriest = (Button) view.findViewById(R.id.btn_priest);

        btnUser.setOnClickListener(this);
        btnPriest.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_user:
                setRole(AppConstants.USER_ROLE);

                break;
            case R.id.btn_priest:
                setRole(AppConstants.PRIEST_ROLE);
                break;


        }
    }

    private void setRole(String userRole) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.REGISTRATION_ROLE, userRole);
        SignUpStep2Fragment f = new SignUpStep2Fragment();
        f.setArguments(bundle);
        ((SignUpActivity) getActivity()).loadFragment(f);
    }


}
