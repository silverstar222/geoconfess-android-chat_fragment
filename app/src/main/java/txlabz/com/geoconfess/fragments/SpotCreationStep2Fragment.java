package txlabz.com.geoconfess.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.activities.MainActivity;
import txlabz.com.geoconfess.adapters.SpotsAdapter;
import txlabz.com.geoconfess.constants.ApiConstants;
import txlabz.com.geoconfess.constants.AppConstants;
import txlabz.com.geoconfess.events.DeletedStaticSpotEvent;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.events.MySpotsEvent;
import txlabz.com.geoconfess.helpers.ListItemClickListener;
import txlabz.com.geoconfess.models.response.MySpotResponse;
import txlabz.com.geoconfess.models.response.Recurrences;
import txlabz.com.geoconfess.network.requests.DeleteSpotRequest;
import txlabz.com.geoconfess.network.requests.MySpotsRequest;
import txlabz.com.geoconfess.utils.SharedPreferenceUtils;
import txlabz.com.geoconfess.utils.Utils;

/**
 * Created by yagor on 5/4/2016.
 */
public class SpotCreationStep2Fragment extends BaseFragment implements View.OnClickListener, ListItemClickListener {

    private static final String TAG = SpotCreationStep2Fragment.class.getSimpleName();
    private static final int POSITION_INVALID = -1;
    List<MySpotResponse> list = new ArrayList<>();
    @BindView(R.id.bottombtn)
    LinearLayout bottombtn;
    private RecyclerView mrecyclerView;
    private Utils utils;
    private SpotsAdapter adapter;
    private int deletedPosition = POSITION_INVALID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.spotcreationlayout, container, false);
        ButterKnife.bind(this, view);
        prepareAdapter(view);
        fetchSpots();
        updateVIew();
        return view;
    }

    private void fetchSpots() {
        MySpotsRequest.getMyPosts(SharedPreferenceUtils.getAccessToken(getActivity()));
        try {
            ((MainActivity) getActivity()).showDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateVIew() {
        bottombtn.setOnClickListener(this);
    }

    private void prepareAdapter(View view) {
        mrecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mrecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new SpotsAdapter(list, this);
        mrecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottombtn:
                //((MainActivity)getActivity()).closeCurrentFragment();
                SpotCreationStep3Fragment f = new SpotCreationStep3Fragment();
                ((MainActivity) getActivity()).loadFragment(f, true, true);
                break;
        }
    }

    @Subscribe
    public void onEvent(MySpotsEvent event) {
        ((MainActivity) getActivity()).hideDialog();
        list.clear();

        List<MySpotResponse> tempList = new ArrayList<>();
        tempList.addAll(Arrays.asList(event.getResponse()));

        List<MySpotResponse> tempListStore = new ArrayList<>();
        for (int i = 0; i < tempList.size(); i++) {
            MySpotResponse temp = tempList.get(i);

            if (temp.getRecurrences() != null && temp.getRecurrences().length > 0) {
                for (int j = 0; j < temp.getRecurrences().length; j++) {
                    MySpotResponse spotTemp = new MySpotResponse();
                    spotTemp.setId(temp.getId());
                    spotTemp.setName(temp.getName());
                    spotTemp.setActivity_type(temp.getActivity_type());
                    spotTemp.setLatitude(temp.getLatitude());
                    spotTemp.setLongitude(temp.getLongitude());
                    spotTemp.setState(temp.getState());
                    spotTemp.setPostCode(temp.getPostCode());
                    spotTemp.setCity(temp.getCity());
                    spotTemp.setState(temp.getState());
                    spotTemp.setCountry(temp.getCountry());
                    Recurrences[] recTemp = new Recurrences[1];
                    recTemp[0] = temp.getRecurrences()[j];
                    spotTemp.setRecurrences(recTemp);

                    tempListStore.add(spotTemp);
                }
            }

        }
        list.addAll(tempListStore);
        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onEvent(ErrorResponseEvent event) {
        ((MainActivity) getActivity()).hideDialog();
        Toast.makeText(getContext(), event.getError().getErrors(), Toast.LENGTH_SHORT).show();
        deletedPosition = POSITION_INVALID;
    }

    @Override
    public void onItemClick(int position, long itemId, int button) {
        switch (button) {
            case R.id.edit:
                editSpot(position);
                break;
            case R.id.delet:
                deleteSpot(position);
                break;
        }
    }

    private void deleteSpot(final int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.delete_spot_dialog_title))
                .setMessage(getString(R.string.delete_spot_dialog_question))
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletedPosition = position;
                        try {
                            ((MainActivity) getActivity()).showDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        DeleteSpotRequest.deleteSpot(list.get(position).getId(), SharedPreferenceUtils.getAccessToken(getActivity()), ApiConstants.STATIC_SPOT);
                    }
                })
                .create()
                .show();
    }

    @Subscribe
    public void OnEvent(DeletedStaticSpotEvent event) {
        ((MainActivity) getActivity()).hideDialog();
        list.remove(deletedPosition);
        adapter.notifyDataSetChanged();
        deletedPosition = POSITION_INVALID;
    }

    private void editSpot(int position) {
        ((MainActivity) getActivity()).hideDialog();
        SpotCreationStep4Fragment f = new SpotCreationStep4Fragment();
        Bundle b = new Bundle();
        b.putString(AppConstants.SPOTNAME, list.get(position).getName());
        b.putInt(ApiConstants.ENTITY_ID, (int) list.get(position).getRecurrences()[0].getId());
        b.putString("EditPurpose", getString(R.string.edit));
        f.setArguments(b);
        ((MainActivity) getActivity()).loadFragment(f, true, true);
    }
}
