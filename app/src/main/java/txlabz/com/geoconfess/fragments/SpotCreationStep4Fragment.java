package txlabz.com.geoconfess.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.activities.MainActivity;
import txlabz.com.geoconfess.constants.ApiConstants;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.events.UpdateRecurrencesEvent;
import txlabz.com.geoconfess.network.requests.UpdateRecurrencesRequest;
import txlabz.com.geoconfess.utils.DialogUtility;
import txlabz.com.geoconfess.utils.SharedPreferenceUtils;

/**
 * Created by yagor on 5/5/2016.
 */
public class SpotCreationStep4Fragment extends BaseFragment implements View.OnClickListener {


    private static final int START = 1;
    private static final int STOP = 2;
    static TextView date;
    TextView fromhour, fromminute, tohour, tominute;
    Dialog dialog;
    Button btn_tostep5;
    ImageView drop1, drop2, drop3, drop4;
    private int spotId;
    private String startHour = "12";
    private String stopHour = "12";
    private String startMin = "30";
    private String stopMin = "30";
    private String mEdit = "";
    private LinearLayout mBottomButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.spotcreation_step4, container, false);


        spotId = (int) getArguments().getInt(ApiConstants.ENTITY_ID);
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

        fromhour = (TextView) view.findViewById(R.id.fromhour);
        fromminute = (TextView) view.findViewById(R.id.fromminute);
        tominute = (TextView) view.findViewById(R.id.tominute);
        tohour = (TextView) view.findViewById(R.id.tohour);
        date = (TextView) view.findViewById(R.id.date);
        date.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        date.setOnClickListener(this);
        btn_tostep5 = (Button) view.findViewById(R.id.btn_tostep5);
        fromhour.setOnClickListener(this);
        fromminute.setOnClickListener(this);
        tohour.setOnClickListener(this);
        tominute.setOnClickListener(this);
        btn_tostep5.setOnClickListener(this);
        mBottomButton = (LinearLayout) view.findViewById(R.id.bottombtn);
        mBottomButton.setOnClickListener(this);

        return view;
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
            case R.id.date:
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
                break;
            case R.id.btn_tostep5:
                SpotCreationStep5Fragment fragment = new SpotCreationStep5Fragment();
                Bundle b = new Bundle();
                b.putInt(ApiConstants.ENTITY_ID, spotId);
                if (mEdit.equalsIgnoreCase(getString(R.string.edit))) {
                    b.putString("EditPurpose", getString(R.string.edit));
                }
                fragment.setArguments(b);
                ((MainActivity) getActivity()).loadFragment(fragment, true, true);
                break;
            case R.id.bottombtn:
                try {
                    ((MainActivity) getActivity()).showDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateRecurrences();
                break;


        }
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
        if (!String.valueOf(date.getText()).isEmpty()) {

            if (mEdit.equalsIgnoreCase(getString(R.string.edit))) {
                UpdateRecurrencesRequest.updateNotRecurrences(spotId, String.valueOf(date.getText()), prepareTime(startHour, startMin),
                        prepareTime(stopHour, stopMin), SharedPreferenceUtils.getAccessToken(getActivity()));

            } else {
                UpdateRecurrencesRequest.createDayRecurrence(spotId, String.valueOf(date.getText()), prepareTime(startHour, startMin),
                        prepareTime(stopHour, stopMin), SharedPreferenceUtils.getAccessToken(getActivity()));

            }
        }
    }

    @Subscribe
    public void onEvent(UpdateRecurrencesEvent event) {
        ((MainActivity) getActivity()).hideDialog();
        if (mEdit.equalsIgnoreCase(getString(R.string.edit))) {
            ((MainActivity) getActivity()).closeCurrentFragment();
        }
        SpotCreationStep3Fragment fragment = new SpotCreationStep3Fragment();
        ((MainActivity) getActivity()).clearBackStack(fragment);

    }

    @Subscribe
    public void onEvent(ErrorResponseEvent event) {
        ((MainActivity) getActivity()).hideDialog();

        Toast.makeText(getActivity(), event.getError().getErrors(), Toast.LENGTH_SHORT).show();
    }

    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }

        public void populateSetDate(int year, int month, int day) {
            date.setText(String.format("%s-%s-%s", year, month, day));
        }
    }
}
