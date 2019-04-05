package txlabz.com.geoconfess.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import txlabz.com.geoconfess.activities.LoginActivity;
import txlabz.com.geoconfess.fragments.MessageFragment;

/**
 * Created by yagor on 20/04/2016.
 */
public class DialogUtility {

    static AlertDialog mAlertDialog;

    public static void showDialog(Context context, String title, String message) {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
        mAlertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        mAlertDialog.dismiss();
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void showInternetDialog(final Context context, String title, String message) {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
        mAlertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        mAlertDialog.dismiss();
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    public static Dialog showHourdialog(Context context, ListView modeList, String[] stringArray) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Hour");

        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
        modeList.setAdapter(modeAdapter);

        builder.setView(modeList);
        final Dialog dialog = builder.create();

        dialog.show();


        return dialog;

    }


    public static Dialog showMintdialog(Context context, ListView modeList, List<String> stringArray) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Minute");

        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
        modeList.setAdapter(modeAdapter);

        builder.setView(modeList);
        final Dialog dialog = builder.create();

        dialog.show();


        return dialog;

    }

    public static void showDialogWithCallback(Context context, String title, String message, final DialogMangerCallback mCallback) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        dialog.dismiss();
                        mCallback.onOkClick();

                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void showDialogYesNo(Context context, String title, String message) {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
        mAlertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        MessageFragment.UpdateContact();
                    }
                })
                .setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        mAlertDialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
