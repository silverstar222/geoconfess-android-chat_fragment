package txlabz.com.geoconfess.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.activities.MainActivity;
import txlabz.com.geoconfess.constants.AppConstants;
import txlabz.com.geoconfess.events.AccessConfessionEvent;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.events.MarkReadEvent;
import txlabz.com.geoconfess.events.RefuseConfessionEvent;
import txlabz.com.geoconfess.network.requests.AcceptConfessionRequest;
import txlabz.com.geoconfess.network.requests.MarkNotificationReadRequest;
import txlabz.com.geoconfess.network.requests.RefuseConfessionRequest;
import txlabz.com.geoconfess.utils.SharedPreferenceUtils;

public class PriestResponse2Fragment extends BaseFragment {
    @BindView(R.id.txt_priest_name)
    TextView txtPriestName;
    @BindView(R.id.txt_demand_confession)
    TextView txtDemandConfession;
    @BindView(R.id.btn_available)
    LinearLayout btnAvailable;
    @BindView(R.id.btn_not_available)
    LinearLayout btnNotAvailable;
    private long meetRequestId, mNotificationId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_priest_response2, container, false);
        ButterKnife.bind(this, view);

        meetRequestId = getArguments().getLong(AppConstants.REQUEST_ID);

        String name = getArguments().getString(AppConstants.REGISTRATION_NAME);
        if (name != null)
            txtPriestName.setText(name);
        mNotificationId = getArguments().getLong(AppConstants.NOTIFICATION_ID);

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

    @OnClick({R.id.btn_available, R.id.btn_not_available})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_available:
                try {
                    ((MainActivity) getActivity()).showDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AcceptConfessionRequest.access(meetRequestId, SharedPreferenceUtils.getAccessToken(getActivity()));
                break;
            case R.id.btn_not_available:
                try {
                    ((MainActivity) getActivity()).showDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RefuseConfessionRequest.refuse(meetRequestId, SharedPreferenceUtils.getAccessToken(getActivity()));
                break;
        }
    }

    @Subscribe
    public void onEvent(AccessConfessionEvent event) {
        ((MainActivity) getActivity()).hideDialog();
        Toast.makeText(getActivity(), R.string.confession_accepted, Toast.LENGTH_SHORT).show();
        ((MainActivity) getActivity()).closeCurrentFragment();

    }

    @Subscribe
    public void onEvent(MarkReadEvent event) {


    }

    @Subscribe
    public void onEvent(RefuseConfessionEvent event) {
        ((MainActivity) getActivity()).hideDialog();
        Toast.makeText(getActivity(), R.string.confesion_denied, Toast.LENGTH_SHORT).show();
        ((MainActivity) getActivity()).closeCurrentFragment();

    }

    @Subscribe
    public void onEvent(ErrorResponseEvent event) {
        ((MainActivity) getActivity()).hideDialog();
        Toast.makeText(getActivity(), event.getError().getErrors(), Toast.LENGTH_SHORT).show();
    }

}