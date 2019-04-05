package txlabz.com.geoconfess.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.utils.SharedPreferenceUtils;

public class NotesFragment extends Fragment {

    private static final String TAG = "MyActivity";

    @BindView(R.id.note)
    EditText note;

    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        ButterKnife.bind(this, view);
        note.setText(SharedPreferenceUtils.getNote(getActivity()));
        return view;
    }

    @Override
    public void onDestroy() {
        SharedPreferenceUtils.setNote(getActivity(), String.valueOf(note.getText()));
        super.onDestroy();
    }
}
