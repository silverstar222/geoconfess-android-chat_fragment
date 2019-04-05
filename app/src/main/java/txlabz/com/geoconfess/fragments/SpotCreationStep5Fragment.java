package txlabz.com.geoconfess.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.activities.MainActivity;
import txlabz.com.geoconfess.constants.ApiConstants;
import txlabz.com.geoconfess.constants.AppConstants;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.events.UpdateRecurrencesEvent;
import txlabz.com.geoconfess.network.requests.UpdateRecurrencesRequest;
import txlabz.com.geoconfess.utils.DialogUtility;
import txlabz.com.geoconfess.utils.SharedPreferenceUtils;


/**
 * Created by yagor on 5/5/2016.
 */
public class SpotCreationStep5Fragment extends BaseFragment implements View.OnClickListener {

    private static final int START = 1;
    private static final int STOP = 2;
    ImageView drop1, drop2, drop3, drop4;
    private TextView fromhour, fromminute, tohour, tominute;
    private Dialog dialog;
    private ImageView check1, check2, check3, check4, check5, check6, check7;
    private Boolean Bcheck1 = false, Bcheck2 = false, Bcheck3 = false, Bcheck4 = false, Bcheck5 = false, Bcheck6 = false, Bcheck7 = false;
    private LinearLayout checkspace1, checkspace2, checkspace3, checkspace4, checkspace5, checkspace6, checkspace7;
    private LinearLayout mBottomButton;
    private int spotId;
    private String startHour ="12";
    private String stopHour ="12";
    private String startMin ="30";
    private String stopMin ="30";
    private ArrayList<String> days;
    private String mEdit = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.spotcreation_step5, container, false);

        spotId = getArguments().getInt(ApiConstants.ENTITY_ID);

        if (getArguments().getString("EditPurpose") != null) {
            mEdit = getArguments().getString("EditPurpose");
        }
        drop1 = (ImageView) view.findViewById(R.id.drop1);
        drop2 = (ImageView) view.findViewById(R.id.drop2);
        drop3 = (ImageView) view.findViewById(R.id.drop3);
        drop4 = (ImageView) view.findViewById(R.id.drop4);

        drop1.setOnClickListener(this);
        drop2.setOnClickListener(this);
        drop3.setOnClickListener(this);
        drop4.setOnClickListener(this);


        check1 = (ImageView) view.findViewById(R.id.check1);
        check2 = (ImageView) view.findViewById(R.id.check2);
        check3 = (ImageView) view.findViewById(R.id.check3);
        check4 = (ImageView) view.findViewById(R.id.check4);
        check5 = (ImageView) view.findViewById(R.id.check5);
        check6 = (ImageView) view.findViewById(R.id.check6);
        check7 = (ImageView) view.findViewById(R.id.check7);
        checkspace1 = (LinearLayout) view.findViewById(R.id.checkspace1);
        checkspace2 = (LinearLayout) view.findViewById(R.id.checkspace2);
        checkspace3 = (LinearLayout) view.findViewById(R.id.checkspace3);
        checkspace4 = (LinearLayout) view.findViewById(R.id.checkspace4);
        checkspace5 = (LinearLayout) view.findViewById(R.id.checkspace5);
        checkspace6 = (LinearLayout) view.findViewById(R.id.checkspace6);
        checkspace7 = (LinearLayout) view.findViewById(R.id.checkspace7);

        fromhour = (TextView) view.findViewById(R.id.fromhour);
        fromminute = (TextView) view.findViewById(R.id.fromminute);
        tominute = (TextView) view.findViewById(R.id.tominute);
        tohour = (TextView) view.findViewById(R.id.tohour);
        fromhour.setOnClickListener(this);
        fromminute.setOnClickListener(this);
        tohour.setOnClickListener(this);
        tominute.setOnClickListener(this);
        checkspace1.setOnClickListener(this);
        checkspace2.setOnClickListener(this);
        checkspace3.setOnClickListener(this);
        checkspace4.setOnClickListener(this);
        checkspace5.setOnClickListener(this);
        checkspace6.setOnClickListener(this);
        checkspace7.setOnClickListener(this);
        mBottomButton = (LinearLayout) view.findViewById(R.id.bottombtn);
        mBottomButton.setOnClickListener(this);

        prepareCheckListener();
        return view;
    }

    private void prepareCheckListener() {
        days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            days.add("");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fromhour:
                gethour(fromhour, START);
                break;
            case R.id.drop1:
                gethour(fromhour, START);
                break;

            case R.id.fromminute:
                getminute(fromminute, START);
                break;
            case R.id.drop2:
                getminute(fromminute, START);
                break;


            case R.id.tominute:
                getminute(tominute, STOP);
                break;

            case R.id.drop4:
                getminute(tominute, STOP);
                break;

            case R.id.tohour:
                gethour(tohour, STOP);
                break;
            case R.id.drop3:
                gethour(tohour, STOP);
                break;

            case R.id.checkspace1:
                if (Bcheck1) {
                    check1.setImageResource(R.drawable.checkbox);
                    Bcheck1 = false;
                    days.set(0, "");

                } else {
                    check1.setImageResource(R.drawable.checked);
                    Bcheck1 = true;
                    days.set(0, AppConstants.MONDAY);

                }
                break;
            case R.id.checkspace2:

                if (Bcheck2) {
                    check2.setImageResource(R.drawable.checkbox);
                    Bcheck2 = false;
                    days.set(1, "");

                } else {
                    check2.setImageResource(R.drawable.checked);
                    Bcheck2 = true;
                    days.set(1, AppConstants.TUESDAY);
                }


                break;
            case R.id.checkspace3:
                if (Bcheck3) {
                    check3.setImageResource(R.drawable.checkbox);
                    Bcheck3 = false;
                    days.set(2, "");

                } else {
                    check3.setImageResource(R.drawable.checked);
                    Bcheck3 = true;
                    days.set(2, AppConstants.WEDNESDAY);


                }


                break;
            case R.id.checkspace4:
                if (Bcheck4) {
                    check4.setImageResource(R.drawable.checkbox);
                    Bcheck4 = false;
                    days.set(3, "");

                } else {
                    check4.setImageResource(R.drawable.checked);
                    Bcheck4 = true;
                    days.set(3, AppConstants.THURSDAY);

                }


                break;
            case R.id.checkspace5:

                if (Bcheck5) {
                    check5.setImageResource(R.drawable.checkbox);
                    Bcheck5 = false;
                    days.set(4, "");

                } else {
                    check5.setImageResource(R.drawable.checked);
                    Bcheck5 = true;
                    days.set(4, AppConstants.FRIDAY);

                }


                break;

            case R.id.checkspace6:
                if (Bcheck6) {
                    check6.setImageResource(R.drawable.checkbox);
                    Bcheck6 = false;
                    days.set(5, "");

                } else {
                    check6.setImageResource(R.drawable.checked);
                    Bcheck6 = true;
                    days.set(5, AppConstants.SATURDAY);

                }

                break;

            case R.id.checkspace7:
                if (Bcheck7) {
                    check7.setImageResource(R.drawable.checkbox);
                    Bcheck7 = false;
                    days.set(6, "");

                } else {
                    check7.setImageResource(R.drawable.checked);
                    Bcheck7 = true;
                    days.set(6, AppConstants.SUNDAY);

                }
                break;
            case R.id.bottombtn:
                updateRecurrences();
                break;
        }
    }

    private String[] getSelectedDays() {
        ArrayList<String> selectedDays = new ArrayList<>();
        for (int i = 0; i < days.size(); i++) {
            if (!days.get(i).isEmpty()) {
                selectedDays.add(days.get(i));
            }
        }
        return selectedDays.toArray(new String[selectedDays.size()]);
    }

    public void gethour(final TextView hour, final int type) {
        ListView list = new ListView(getActivity());

        final String[] stringArray = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hour.setText(stringArray[i]);
                switch (type) {
                    case START:
                        startHour = stringArray[i];
                        break;
                    case STOP:
                        stopHour = stringArray[i];
                        break;
                }
                dialog.dismiss();
            }
        });

        dialog = DialogUtility.showHourdialog(getActivity(), list, stringArray);
    }

    private void getminute(final TextView minute, final int type) {

        ListView list2 = new ListView(getActivity());
        final List<String> count = new ArrayList<>();

        for (int x = 0; x < 60; x++) {
            count.add(String.valueOf(x));

        }

        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                minute.setText(count.get(i));
                switch (type) {
                    case START:
                        startMin = count.get(i);
                        break;
                    case STOP:
                        stopMin = count.get(i);
                        break;
                }
                dialog.dismiss();
            }
        });

        dialog = DialogUtility.showMintdialog(getActivity(), list2, count);

    }

    private String prepareTime(String hours, String min) {
        return String.format("%s:%s", hours, min);
    }


    private void updateRecurrences() {
        String[] selected = getSelectedDays();
        if (selected.length > 0) {
            try {
                ((MainActivity) getActivity()).showDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mEdit.equalsIgnoreCase(getString(R.string.edit))) {
                UpdateRecurrencesRequest.updateRecurrance(spotId, prepareTime(startHour, startMin),
                        prepareTime(stopHour, stopMin), SharedPreferenceUtils.getAccessToken(getActivity()), selected);
            } else {
                UpdateRecurrencesRequest.createRecurrance(spotId, prepareTime(startHour, startMin),
                        prepareTime(stopHour, stopMin), SharedPreferenceUtils.getAccessToken(getActivity()), selected);
            }


        } else {
            Toast.makeText(getActivity(), R.string.day_selection_warning, Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void onEvent(UpdateRecurrencesEvent event) {
        ((MainActivity) getActivity()).hideDialog();

        SpotCreationStep4Fragment fragment = new SpotCreationStep4Fragment();
        ((MainActivity) getActivity()).clearBackStack(fragment);

        SpotCreationStep3Fragment fragment1 = new SpotCreationStep3Fragment();
        ((MainActivity) getActivity()).clearBackStack(fragment1);

    }

    @Subscribe
    public void onEvent(ErrorResponseEvent event) {
        ((MainActivity) getActivity()).hideDialog();

        Toast.makeText(getActivity(), event.getError().getErrors(), Toast.LENGTH_SHORT).show();
    }

}
