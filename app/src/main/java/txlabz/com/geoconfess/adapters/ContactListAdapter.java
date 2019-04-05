package txlabz.com.geoconfess.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.models.request.ContactListEntity;

/**
 * Created by admin on 7/7/2016.
 */

public class ContactListAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context context;
    ArrayList<ContactListEntity> contactList = new ArrayList<ContactListEntity>();
    String Event;
    String userType;

    public ContactListAdapter(Context context, ArrayList<ContactListEntity> contactList1) {
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.contactList = contactList1;

        //       userType = (String) GlobalMethods.getValueFromPreference(context,
        //         GlobalMethods.STRING_PREFERENCE, BairoAppConstants.USERTYPE);
    }


    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.child_contcat_layout, null);

        holder.nameTxt = (TextView) rowView.findViewById(R.id.nameTxt);
        holder.messageBtn = (ImageView) rowView.findViewById(R.id.message_icon);
        holder.root = (LinearLayout) rowView.findViewById(R.id.root);
        holder.nameTxt.setText(contactList.get(position).getName());
        holder.root.setTag(position);
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = (Integer) v.getTag();
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:" + contactList.get(position).getPhone_number()));
                sendIntent.putExtra("sms_body", "Rejoins SOSConfession!");
                context.startActivity(sendIntent);

            }
        });
        return rowView;

    }

    public class Holder {
        TextView nameTxt;
        ImageView messageBtn;
        LinearLayout root;
    }

}


