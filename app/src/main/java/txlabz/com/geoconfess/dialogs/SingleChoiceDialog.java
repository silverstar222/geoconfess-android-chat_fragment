package txlabz.com.geoconfess.dialogs;

/**
 * Created by Ivan on 26.4.2016..
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;


public class SingleChoiceDialog extends DialogFragment
        implements DialogInterface.OnClickListener {

    private OnSelectedListener callback;
    private int displayItemsId;

    public static SingleChoiceDialog newInstance(int displayItemsId,
                                                 OnSelectedListener callback) {

        SingleChoiceDialog frag = new SingleChoiceDialog();
        Bundle args = new Bundle();
        args.putInt("displayItemsId", displayItemsId);
        frag.setArguments(args);
        frag.callback = callback;

        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        displayItemsId = getArguments().getInt("displayItemsId");

        return new AlertDialog.Builder(getActivity())
                .setTitle("Souhaitez-vous")
                .setItems(displayItemsId, this)
                .create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        String selected = getResources().getStringArray(displayItemsId)[which];
        callback.onSelected(selected);
    }

    public interface OnSelectedListener {
        public void onSelected(String selected);
    }
}