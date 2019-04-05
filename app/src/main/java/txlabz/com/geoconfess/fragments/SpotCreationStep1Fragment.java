package txlabz.com.geoconfess.fragments;

/**
 * Created by arslan on 5/3/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.activities.MainActivity;
import txlabz.com.geoconfess.utils.DialogUtility;
import txlabz.com.geoconfess.utils.LocationUtils;
import txlabz.com.geoconfess.utils.NetworkUtils;
import txlabz.com.geoconfess.utils.SharedPreferenceUtils;

/**
 * Created by yagor on 27/04/2016.
 */
public class SpotCreationStep1Fragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.btn_spot)
    Button btn_spot;
    @BindView(R.id.btn_livetracking)
    Button btn_livetracking;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.spotcreation_step1, container, false);
        ButterKnife.bind(this, view);

        fetchActivityBottomBar();

        btn_livetracking.setOnClickListener(this);
        btn_spot.setOnClickListener(this);
        return view;
    }

    private void fetchActivityBottomBar() {
        btn_livetracking.setOnClickListener(this);
        btn_spot.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_spot:
                createStaticSpot();
                break;
            case R.id.btn_livetracking:
                trackMyLocation();
                break;
        }
    }

    private void trackMyLocation() {
        if (!SharedPreferenceUtils.isTrackServiceRunning(getActivity())) {
            if (LocationUtils.isGpsOn(getActivity())) {
                if (NetworkUtils.connected()) {
                    try {
                        ((MainActivity) getActivity()).showDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ((MainActivity) getActivity()).tryToStartService();

                } else {
                    DialogUtility.showDialog(getActivity(), "", getActivity().getString(R.string.no_internet_message));
                }
            } else {
                DialogUtility.showDialog(getActivity(), "", getActivity().getString(R.string.no_gps_message));
            }
        }
    }

    private void createStaticSpot() {
        SpotCreationStep2Fragment f = new SpotCreationStep2Fragment();
        ((MainActivity) getActivity()).loadFragment(f, true, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
