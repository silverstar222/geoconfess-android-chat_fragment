package txlabz.com.geoconfess.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.activities.MainActivity;
import txlabz.com.geoconfess.constants.ApiConstants;
import txlabz.com.geoconfess.constants.AppConstants;
import txlabz.com.geoconfess.events.BookingCheckEvent;
import txlabz.com.geoconfess.events.BookingEvent;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.models.response.Recurrences;
import txlabz.com.geoconfess.models.response.SpotResponse;
import txlabz.com.geoconfess.network.requests.CheckRequestStatus;
import txlabz.com.geoconfess.network.requests.SendConfessRequest;
import txlabz.com.geoconfess.utils.LocationUtils;
import txlabz.com.geoconfess.utils.SharedPreferenceUtils;

public class UserChatInitiate1Fragment extends BaseFragment {
    @BindView(R.id.txt_priest_name)
    TextView txtPriestName;
    @BindView(R.id.txt_priest_title)
    TextView txtPriestTitle;

    @BindView(R.id.txt_priest_distance)
    TextView txtPriestDistance;
    @BindView(R.id.btn_user_send_request)
    LinearLayout btnUserSendRequest;
    @BindView(R.id.btn_user_sent_request)
    LinearLayout btnRequestSent;
    @BindView(R.id.btn_user_chat)
    LinearLayout btnChat;
    @BindView(R.id.btn_user_find_itinerary)
    LinearLayout btnFindLocation;

    @BindView(R.id.send_request)
    TextView txtSendRequest;
    @BindView(R.id.request_sent)
    TextView txtrequestSent;
    @BindView(R.id.chat_screen)
    TextView txtChat;
    @BindView(R.id.find_location)
    TextView txtShowLocation;

    private SpotResponse spot;
    private Double latitude;
    private Double longitude;
    private String mPriestTitle = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_chat_initiate1, container, false);
        ButterKnife.bind(this, view);

        spot = (SpotResponse) getArguments().getParcelable(AppConstants.SPOT_PARCELABLE);
        latitude = (Double) getArguments().getDouble(AppConstants.MY_LATITUDE);
        longitude = (Double) getArguments().getDouble(AppConstants.MY_LONGITUDE);

        if (spot.getPriest().getId() != SharedPreferenceUtils.getUserId(getActivity())) {
            checkState();
        } else {
            btnFindLocation.setVisibility(View.VISIBLE);
        }


        txtPriestName.setText(spot.getName());
        if (TextUtils.equals(spot.getActivity_type(), ApiConstants.STATIC_SPOT)) {
            if (spot.getRecurrences() != null && spot.getRecurrences().length > 0) {
                // Recurrences recurrences = spot.getRecurrences()[0];
                mPriestTitle = createWorkingTime(spot.getRecurrences());
                txtPriestTitle.setText(mPriestTitle);
            }


        }

        txtPriestDistance.setText(LocationUtils.getReadableDistance(getActivity(), latitude, longitude, spot.getLatitude(), spot.getLongitude()));
        return view;
    }

    private void checkState() {
        try {
            ((MainActivity) getActivity()).showDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
        CheckRequestStatus.checkStatus(SharedPreferenceUtils.getAccessToken(getActivity()), spot.getPriest().getId());
    }

    private void sendConfessionRequest() {
        if (longitude != null && latitude != null) {
            try {
                ((MainActivity) getActivity()).showDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
            SendConfessRequest.send(SharedPreferenceUtils.getAccessToken(getActivity()), spot.getPriest().getId(), latitude, longitude, ApiConstants.REQUEST_STATUS_PENDING);
        } else {
            Toast.makeText(getActivity(), R.string.location_not_known, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.btn_user_send_request, R.id.btn_user_add_to_favorites, R.id.btn_user_chat, R.id.btn_user_find_itinerary})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_user_send_request:
                sendConfessionRequest();
                break;
            case R.id.btn_user_add_to_favorites:
                Toast.makeText(getActivity(), "Add to favorites", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_user_chat:
                goToChatFragment();
                break;

            case R.id.btn_user_find_itinerary:
                //Opening the Google map app for navigation
                try {
                    String mapLoaction = spot.getLatitude() + ","
                            + spot.getLongitude();
                    Intent intent = new Intent(
                            android.content.Intent.ACTION_VIEW,
                            Uri.parse("google.navigation:q=" + mapLoaction));
                    getActivity().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Please install a maps application",
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void goToChatFragment() {

        Bundle args = new Bundle();


        args.putLong(AppConstants.NOTIFICATION_ID, 0);
        args.putBoolean(AppConstants.UNREAD_STATUS, false);
        long senderId = (Integer) btnChat.getTag();

        args.putLong(AppConstants.CHAT_USER_ID, senderId);
        args.putString(AppConstants.REGISTRATION_NAME, "USER_" + senderId);
        ChatFragment frag = new ChatFragment();
        frag.setArguments(args);
        ((MainActivity) getActivity()).loadFragment(frag, true, false);
    }

    @Subscribe
    public void onEvent(BookingCheckEvent event) {
        ((MainActivity) getActivity()).hideDialog();
        if (event.getResponse().length > 0) {
            switch (event.getResponse()[0].getStatus()) {
                case AppConstants.ACCEPTED:
                    btnChat.setVisibility(View.VISIBLE);
                    btnFindLocation.setVisibility(View.VISIBLE);
                    btnChat.setTag(event.getResponse()[0].getPriest().getId());

                    break;
                case AppConstants.SENT:
                    btnRequestSent.setVisibility(View.VISIBLE);
                    txtrequestSent.setText(getResources().getString(R.string.request_sent));
                    break;
                case AppConstants.PENDING:
                    btnRequestSent.setVisibility(View.VISIBLE);
                    txtrequestSent.setText(getResources().getString(R.string.request_sent));
                    break;
                case AppConstants.REFUSED:
                    btnRequestSent.setVisibility(View.VISIBLE);
                    txtrequestSent.setText(getResources().getString(R.string.request_refused));
                    break;
                default:
                    btnUserSendRequest.setVisibility(View.VISIBLE);
                    break;

            }
        } else {
            btnUserSendRequest.setVisibility(View.VISIBLE);
        }

    }


    @Subscribe
    public void onEvent(BookingEvent event) {
        ((MainActivity) getActivity()).hideDialog();

        btnUserSendRequest.setBackground(getResources().getDrawable(R.drawable.grey_rectangle_shape));
        btnUserSendRequest.setEnabled(false);
        txtSendRequest.setText(R.string.request_sent);
    }

    @Subscribe
    public void onEvent(ErrorResponseEvent event) {
        ((MainActivity) getActivity()).hideDialog();
        Toast.makeText(getActivity(), event.getError().getErrors(), Toast.LENGTH_SHORT).show();
    }

    public String getDateFormat(String date) {
        String mDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
        mDate = formatter.format(testDate);
        return mDate;
    }

    public String createWorkingTime(Recurrences[] recurrences) {
        String time = "";
        if (recurrences != null) {

            for (Recurrences r : recurrences) {
                time = time + r.getStartTime() + "-" + r.getStopTime();
                if (r.getDate() != null) {
                    String mDate = getDateFormat(r.getDate());
                    time = time + ", " + mDate;
                } else if (r.getDays() != null) {
                    for (String s : r.getDays()) {
                        String temp = "";
                        switch (s) {
                            case (AppConstants.MONDAY):
                                temp = "Lu";
                                break;
                            case (AppConstants.TUESDAY):
                                temp = "Ma";
                                break;
                            case (AppConstants.WEDNESDAY):
                                temp = "Me";
                                break;
                            case (AppConstants.THURSDAY):
                                temp = "Je";
                                break;
                            case (AppConstants.FRIDAY):
                                temp = "Ve";
                                break;
                            case (AppConstants.SATURDAY):
                                temp = "Sa";
                                break;
                            case (AppConstants.SUNDAY):
                                temp = "Di";
                                break;
                        }
                        time = time + ", " + temp;
                    }
                }
                time = time + "\n";
            }
        }

        return time;
    }
}

