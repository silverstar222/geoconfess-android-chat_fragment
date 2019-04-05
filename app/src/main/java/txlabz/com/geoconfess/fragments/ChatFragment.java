package txlabz.com.geoconfess.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.activities.MainActivity;
import txlabz.com.geoconfess.adapters.ConversationAdapter;
import txlabz.com.geoconfess.constants.AppConstants;
import txlabz.com.geoconfess.events.AllChatMessagesEvent;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.events.MarkReadEvent;
import txlabz.com.geoconfess.events.MessageSentEvent;
import txlabz.com.geoconfess.helpers.ListItemClickListener;
import txlabz.com.geoconfess.models.response.ChatMessage;
import txlabz.com.geoconfess.network.requests.ChatMessagesRequest;
import txlabz.com.geoconfess.network.requests.MarkNotificationReadRequest;
import txlabz.com.geoconfess.network.requests.SendMessageRequest;
import txlabz.com.geoconfess.utils.SharedPreferenceUtils;

/**
 * Created by Ivan on 3.6.2016..
 */
public class ChatFragment extends BaseFragment implements ListItemClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.send_msg)
    TextView mSendMsg;
    @BindView(R.id.message_content_text)
    EditText mTextContent;
    @BindView(R.id.txt_name)
    TextView mUserName;
    private List<ChatMessage> itemsData;
    private ConversationAdapter mAdapter;
    private ChatMessage lastMyMsg;
    private long foreignUserId, mNotificationId;
    private boolean messageSent = true;
    private Handler mChatHandler = new Handler();
    private long mTimeinterval = 3000;
    private LinearLayoutManager linearLayoutManager;

    private Runnable mChatCheckingService = new Runnable() {
        @Override
        public void run() {
            fetchOldMessages();
            mChatHandler.postDelayed(this, mTimeinterval);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.chat_fragment_layout, container, false);
        ButterKnife.bind(this, view);
        foreignUserId = getArguments().getLong(AppConstants.CHAT_USER_ID);
        mNotificationId = getArguments().getLong(AppConstants.NOTIFICATION_ID);
        prepareRecyclerView();
        fetchOldMessages();
        //Only call the read status API , if unread status is true
        if (getArguments().getBoolean(AppConstants.UNREAD_STATUS)) {
            MarkReadApi();
        }
        String name = getArguments().getString(AppConstants.REGISTRATION_NAME);
        if (name != null)

            mUserName.setText(name);

        return view;
    }
    //Notification ReadAPi

    private void MarkReadApi() {

        MarkNotificationReadRequest.markread(mNotificationId, SharedPreferenceUtils.getAccessToken(getActivity()));

    }

    private void fetchOldMessages() {
        //((MainActivity) getActivity()).showDialog();
        ChatMessagesRequest.getAll(foreignUserId, SharedPreferenceUtils.getAccessToken(getActivity()));
    }

    private void prepareRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        itemsData = new ArrayList<>();

        mAdapter = new ConversationAdapter(itemsData, this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.send_msg})
    public void onClick() {
        sendMessage();
    }

    private void sendMessage() {
        String msgText = String.valueOf(mTextContent.getText());
        if (!msgText.isEmpty() && messageSent) {
            messageSent = false;
            lastMyMsg = new ChatMessage();
            lastMyMsg.setSenderId(SharedPreferenceUtils.getUserId(getActivity()));
            lastMyMsg.setRecipientId(foreignUserId);
            lastMyMsg.setText(msgText);
            SendMessageRequest.send(foreignUserId, SharedPreferenceUtils.getAccessToken(getActivity()), msgText);
        }
    }

    @Subscribe
    public void onEvent(MessageSentEvent event) {
        itemsData.add(0, lastMyMsg);
        mAdapter.notifyDataSetChanged();
        mTextContent.setText("");
        messageSent = true;

    }

    @Subscribe
    public void onEvent(AllChatMessagesEvent event) {
        itemsData.clear();
        ((MainActivity) getActivity()).hideDialog();
        itemsData.addAll(Arrays.asList(event.getResponse()));
        mAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onEvent(MarkReadEvent event) {

    }

    @Subscribe
    public void onEvent(ErrorResponseEvent event) {
        ((MainActivity) getActivity()).hideDialog();
        messageSent = true;
        Toast.makeText(getActivity(), event.getError().getErrors(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position, long itemId, int button) {

    }

    @Override
    public void onResume() {
        mChatHandler.removeCallbacks(mChatCheckingService);
        mChatHandler.postDelayed(mChatCheckingService, 0);
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (mChatHandler != null && mChatCheckingService != null) {
            mChatHandler.removeCallbacks(mChatCheckingService);

        }
        super.onDestroy();
    }
}
