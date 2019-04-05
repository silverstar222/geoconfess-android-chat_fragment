package txlabz.com.geoconfess.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import txlabz.com.geoconfess.R;

public class UserChatInitiate2Fragment extends Fragment {
    @BindView(R.id.txt_priest_name)
    TextView txtPriestName;
    @BindView(R.id.txt_priest_title)
    TextView txtPriestTitle;
    @BindView(R.id.txt_priest_distance)
    TextView txtPriestDistance;
    @BindView(R.id.btn_user_chat)
    LinearLayout btnUserChat;
    @BindView(R.id.btn_user_find_itinerary)
    LinearLayout btnUserFindItinerary;
    @BindView(R.id.btn_user_add_to_favorites)
    LinearLayout btnUserAddToFavourites;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_chat_initiate2, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick({R.id.btn_user_chat, R.id.btn_user_find_itinerary, R.id.btn_user_add_to_favorites})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_user_chat:
                Toast.makeText(getActivity(), "Chat", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_user_find_itinerary:
                Toast.makeText(getActivity(), "Find Itinerary", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_user_add_to_favorites:
                Toast.makeText(getActivity(), "Add to favorites", Toast.LENGTH_SHORT).show();
                break;

        }


    }


}