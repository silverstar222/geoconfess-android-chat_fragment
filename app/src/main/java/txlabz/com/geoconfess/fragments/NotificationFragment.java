package txlabz.com.geoconfess.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.activities.MainActivity;
import txlabz.com.geoconfess.adapters.NotificationAdapter;
import txlabz.com.geoconfess.constants.AppConstants;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.events.ServerNotificationEvent;
import txlabz.com.geoconfess.helpers.ListItemClickListener;
import txlabz.com.geoconfess.models.response.ServerNotificationModel;
import txlabz.com.geoconfess.network.requests.NotificationsRequest;
import txlabz.com.geoconfess.utils.SharedPreferenceUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends BaseFragment implements ListItemClickListener {

    public TextView noNotifications;
    public long mNotificationId;
    private List<ServerNotificationModel> itemsData;
    private NotificationAdapter mAdapter;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        noNotifications = (TextView) rootView.findViewById(R.id.no_notifications);
        prepareAdapter(rootView);
        fetchNotifications();


        return rootView;
    }

    private void prepareAdapter(View rootView) {
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.notification_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        itemsData = new ArrayList<>();

        mAdapter = new NotificationAdapter(getActivity(), itemsData, this, ((MainActivity) getActivity()).getMyLocation());
        mRecyclerView.setAdapter(mAdapter);
    }

    public void fetchNotifications() {
        try {
            ((MainActivity) getActivity()).showDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
        NotificationsRequest.getNotifications(SharedPreferenceUtils.getAccessToken(getActivity()));
    }

    @Subscribe
    public void OnEvent(ServerNotificationEvent event) {
        ((MainActivity) getActivity()).hideDialog();
        if (itemsData != null) {
            itemsData.clear();
        }
        List<ServerNotificationModel> programs = Arrays.asList(event.getResponse());

        HashMap<String, Integer> map = new HashMap<String, Integer>();

        for (ServerNotificationModel model : programs) {
            if (TextUtils.equals(SharedPreferenceUtils.getLogInRole(getActivity()), AppConstants.PRIEST_ROLE)) {
                if ((model.getModel().equalsIgnoreCase(AppConstants.MEET_REQUEST))) {
                    if (!map.containsKey("Priest:" + model.getMeetRequestModel().getPenitentModel().getId())) {
                        itemsData.add(model);
                        map.put("Priest:" + model.getMeetRequestModel().getPenitentModel().getId(), 0);
                    }
                }
                if ((model.getModel().equalsIgnoreCase(AppConstants.MESSAGE))) {
                    if (!map.containsKey("Message:" + model.getMessage().getSenderId())) {
                        itemsData.add(model);
                        map.put("Message:" + model.getMessage().getSenderId(), 0);
                    }
                }
            } else {

                if ((model.getModel().equalsIgnoreCase(AppConstants.MEET_REQUEST))) {
                    if (!map.containsKey("Priest:" + model.getMeetRequestModel().getPriestModel().getId())) {
                        map.put("Priest:" + model.getMeetRequestModel().getPriestModel().getId(), 0);
                        itemsData.add(model);
                    }
                }
                if ((model.getModel().equalsIgnoreCase(AppConstants.MESSAGE))) {
                    if (!map.containsKey("Message:" + model.getMessage().getSenderId())) {
                        map.put("Message:" + model.getMessage().getSenderId(), 0);
                        itemsData.add(model);
                    }
                }
            }
        }
        ((MainActivity) getActivity()).getNotificationBadgeCount(itemsData);
        if (!itemsData.isEmpty()) {
            noNotifications.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
    }


    @Subscribe
    public void onEvent(ErrorResponseEvent event) {
        ((MainActivity) getActivity()).hideDialog();
        Toast.makeText(getActivity(), event.getError().getErrors(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemClick(int position, long itemId, int button) {
        switch (itemsData.get(position).getModel()) {
            case AppConstants.MEET_REQUEST:
                handleMeetRequestClick(position);
                break;
            case AppConstants.MESSAGE:
                goToChatFragment(position);
                break;
        }
    }

    private void handleMeetRequestClick(int position) {
        switch (String.valueOf(itemsData.get(position).getMeetRequestModel().getStatus())) {
            case (AppConstants.RECEIVED):
                requestReceived(position);
                break;
            case (AppConstants.PENDING):
                requestReceived(position);
                break;
            case (AppConstants.REFUSED):

                if (TextUtils.equals(SharedPreferenceUtils.getLogInRole(getActivity()), AppConstants.USER_ROLE)) {
                    requestReceived(position);
                }
                break;
            case (AppConstants.ACCEPTED):
                goToChatFragment(position);
                break;
        }
    }

    private void requestReceived(int position) {

        Bundle args = new Bundle();
        args.putLong(AppConstants.REQUEST_ID, itemsData.get(position).getMeetRequestModel().getId());
        args.putLong(AppConstants.NOTIFICATION_ID, itemsData.get(position).getId());
        args.putBoolean(AppConstants.UNREAD_STATUS, itemsData.get(position).isUnread());

        //If user role is Priest, then have to take the name from penitent object, if user role is user,
        // then need to get the name from priest object, this name will be displayed on the next screen
        //for who / whom the request sent or recieved
        if (TextUtils.equals(SharedPreferenceUtils.getLogInRole(getActivity()), AppConstants.PRIEST_ROLE)) {
            args.putString(AppConstants.REGISTRATION_NAME, itemsData.get(position).getMeetRequestModel().getPenitentModel().getName());
            PriestResponse2Fragment frag = new PriestResponse2Fragment();
            frag.setArguments(args);
            ((MainActivity) getActivity()).loadFragment(frag, true, true);
        } else {
            args.putString(AppConstants.REGISTRATION_NAME, itemsData.get(position).getMeetRequestModel().getPriestModel().getName());

            String status = "";
            switch (String.valueOf(itemsData.get(position).getMeetRequestModel().getStatus())) {

                case (AppConstants.PENDING):
                    status = getString(R.string.request_sent);
                    break;
                case (AppConstants.REFUSED):
                    status = getString(R.string.request_refused);
                    break;
                case (AppConstants.ACCEPTED):
                    status = getString(R.string.request_accepted);
                    break;
                case (AppConstants.SENT):
                    status = getString(R.string.request_sent);
                    break;
            }
            args.putString(AppConstants.REQUEST_STATUS, status);
            UserResponseFragment frag = new UserResponseFragment();
            frag.setArguments(args);
            ((MainActivity) getActivity()).loadFragment(frag, true, true);
        }

    }

    private void goToChatFragment(int position) {

        Bundle args = new Bundle();


        args.putLong(AppConstants.NOTIFICATION_ID, itemsData.get(position).getId());
        args.putBoolean(AppConstants.UNREAD_STATUS, itemsData.get(position).isUnread());
        long senderId;
        if (TextUtils.equals(SharedPreferenceUtils.getLogInRole(getActivity()), AppConstants.PRIEST_ROLE)) {

            if (itemsData.get(position).getModel().equalsIgnoreCase(AppConstants.MEET_REQUEST)) {

                senderId = itemsData.get(position).getMeetRequestModel().getPenitentModel().getId();
            } else {
                senderId = itemsData.get(position).getMessage().getSenderId();
            }

        } else {


            if (itemsData.get(position).getModel().equalsIgnoreCase(AppConstants.MEET_REQUEST)) {


                senderId = itemsData.get(position).getMeetRequestModel().getPriestModel().getId();
            } else {
                senderId = itemsData.get(position).getMessage().getSenderId();
            }


        }
        args.putLong(AppConstants.CHAT_USER_ID, senderId);
        args.putString(AppConstants.REGISTRATION_NAME, "USER_" + senderId);
        ChatFragment frag = new ChatFragment();
        frag.setArguments(args);
        ((MainActivity) getActivity()).loadFragment(frag, true, false);
    }
}
