package txlabz.com.geoconfess.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.activities.MainActivity;
import txlabz.com.geoconfess.adapters.ContactListAdapter;
import txlabz.com.geoconfess.models.request.ContactListEntity;

/**
 * Created by admin on 7/7/2016.
 */

public class MessageFragment extends BaseFragment {

    public static ArrayList<ContactListEntity> contactList = new ArrayList<ContactListEntity>();
    public static Context context;
    //public enum PICK_CONTACT=1;
    public static int PICK_CONTACT = 1;
    public static ContactAsyncSync calTask1;
    public static ListView phonumberListview;
    private TextView mTitletxt;
    private WebView mWebview;


    public static void getContact_list() {
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            ContactListEntity ContactListEntity1 = new ContactListEntity();
            ContactListEntity1.setName(name);
            ContactListEntity1.setPhone_number(phoneNumber);
            contactList.add(ContactListEntity1);
            Log.d("name and number :", name + "," + phoneNumber);
        }
        phones.close();
    }


    public static void UpdateContact() {

        calTask1.cancel(true);
        calTask1 = new ContactAsyncSync(context);
        calTask1.execute("");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.message_fragement, container, false);
//        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//        startActivityForResult(intent, 1);

        phonumberListview = (ListView) rootView.findViewById(R.id.contact_list);
        context = getContext();
        //getContact_list();

        calTask1 = new ContactAsyncSync(getContext());
        UpdateContact();
        //  DialogUtility.showDialog(this, "", getString(R.string.check_your_email));
        //   DialogUtility. showDialogYesNo(context,"",getString(R.string.access_contact));
        return rootView;

    }



    public static class ContactAsyncSync extends AsyncTask<String, Void, Void> {
        private Context mContext;

        public ContactAsyncSync(Context context) {
            mContext = context;
        }

        protected void onPreExecute() {
            try {
                MainActivity.showDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected Void doInBackground(String... urls) {

            try {
                getContact_list();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void unused) {

            ContactListAdapter customContactAdapter = new ContactListAdapter(context, contactList);
            phonumberListview.setAdapter(customContactAdapter);
            MainActivity.hideDialog();
        }

    }
}



