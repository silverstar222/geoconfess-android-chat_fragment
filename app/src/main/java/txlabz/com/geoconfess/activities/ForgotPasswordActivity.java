package txlabz.com.geoconfess.activities;

import android.os.Bundle;
import android.widget.EditText;

import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.events.ResetPassEvent;
import txlabz.com.geoconfess.network.requests.ResetPassRequest;
import txlabz.com.geoconfess.utils.DialogMangerCallback;
import txlabz.com.geoconfess.utils.DialogUtility;
import txlabz.com.geoconfess.utils.NetworkUtils;

public class ForgotPasswordActivity extends BaseActivity implements DialogMangerCallback {

    @BindView(R.id.username)
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        email.setHintTextColor(getResources().getColor(R.color.white));
    }

    @OnClick(R.id.confirmButton)
    public void onConfirmClick() {
        if (checkIfValid()) {
            if (NetworkUtils.connected()) {
                try {
                    showDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ResetPassRequest.reset(String.valueOf(email.getText()));
            } else {
                DialogUtility.showDialog(ForgotPasswordActivity.this, "", ForgotPasswordActivity.this.getString(R.string.no_internet_message));
            }
        }
    }

    private boolean checkIfValid() {
        boolean valid = true;
        if (String.valueOf(email.getText()).isEmpty()) {
            DialogUtility.showDialog(this, "", getString(R.string.check_your_email));
            valid = false;
        }
        return valid;
    }

    @Subscribe
    public void onEvent(ResetPassEvent event) {
        hideDialog();
        DialogUtility.showDialogWithCallback(this, "", getString(R.string.forgotpasswd_messg), this);

    }

    @Subscribe
    public void onEvent(ErrorResponseEvent event) {
        hideDialog();
        DialogUtility.showDialog(this, "", getString(R.string.check_your_email));

    }

    @Override
    public void onOkClick() {
        onBackPressed();
    }
}
