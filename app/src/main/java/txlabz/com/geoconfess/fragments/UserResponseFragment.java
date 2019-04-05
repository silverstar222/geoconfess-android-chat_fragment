package txlabz.com.geoconfess.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.constants.AppConstants;
import txlabz.com.geoconfess.network.requests.MarkNotificationReadRequest;
import txlabz.com.geoconfess.utils.SharedPreferenceUtils;

public class UserResponseFragment extends BaseFragment {
    @BindView(R.id.txt_priest_name)
    TextView txtPriestName;
    @BindView(R.id.request_status)
    TextView txtRequestStatus;
    private long meetRequestId, mNotificationId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_response, container, false);
        ButterKnife.bind(this, view);

        meetRequestId = getArguments().getLong(AppConstants.REQUEST_ID);

        String name = getArguments().getString(AppConstants.REGISTRATION_NAME);
        if (name != null)
            txtPriestName.setText(name);
        mNotificationId = getArguments().getLong(AppConstants.NOTIFICATION_ID);

        String status = getArguments().getString(AppConstants.REQUEST_STATUS);
        txtRequestStatus.setText(status);
        //Only call the read status API , if unread status is true
        if (getArguments().getBoolean(AppConstants.UNREAD_STATUS)) {
            MarkReadApi();
        }

        return view;
    }


    //Notification ReadAPi

    private void MarkReadApi() {

        MarkNotificationReadRequest.markread(mNotificationId, SharedPreferenceUtils.getAccessToken(getActivity()));

    }


}