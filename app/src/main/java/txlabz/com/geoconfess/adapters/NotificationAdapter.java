package txlabz.com.geoconfess.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.constants.AppConstants;
import txlabz.com.geoconfess.helpers.ListItemClickListener;
import txlabz.com.geoconfess.models.response.ServerNotificationModel;
import txlabz.com.geoconfess.utils.LocationUtils;
import txlabz.com.geoconfess.utils.SharedPreferenceUtils;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private final Context context;
    private final LatLng myLocation;
    private final ListItemClickListener listener;
    private List<ServerNotificationModel> itemsData;

    public NotificationAdapter(Context context, List<ServerNotificationModel> itemsData, ListItemClickListener listener, LatLng myLocation) {
        this.itemsData = itemsData;
        this.context = context;
        this.listener = listener;
        this.myLocation = myLocation;

    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.priest_requests_list_item, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        ServerNotificationModel model = itemsData.get(position);

        switch (model.getModel()) {
            case AppConstants.MEET_REQUEST:
                prepareMeetRequestItem(viewHolder, model);
                break;
            case AppConstants.MESSAGE:
                prepareMessageItem(viewHolder, model);
                break;
        }
        viewHolder.mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position, 0, viewHolder.mainlayout.getId());
            }
        });

    }

    private void prepareMessageItem(ViewHolder viewHolder, ServerNotificationModel model) {
        viewHolder.txtSubject.setText(String.valueOf(model.getModel()));

        viewHolder.txtName.setText("USER_" + model.getMessage().getSenderId());
        if (model.isUnread()) {
            viewHolder.txtName.setTextColor(context.getResources().getColor(R.color.colorBlackText));
        } else {
            viewHolder.txtName.setTextColor(context.getResources().getColor(R.color.colorGreyText));
        }
        updateAction(viewHolder, context.getString(R.string.chat));
    }

    private void prepareMeetRequestItem(ViewHolder viewHolder, ServerNotificationModel model) {

        if (model.isUnread()) {
            viewHolder.txtName.setTextColor(context.getResources().getColor(R.color.colorBlackText));
        } else {
            viewHolder.txtName.setTextColor(context.getResources().getColor(R.color.colorGreyText));
        }
        if (TextUtils.equals(SharedPreferenceUtils.getLogInRole(context), AppConstants.PRIEST_ROLE)) {
            setValueForPriest(viewHolder, model);
        } else {
            setValueForUser(viewHolder, model);
        }


    }

    private void setValueForPriest(ViewHolder viewHolder, ServerNotificationModel model) {
        viewHolder.txtName.setText(String.valueOf(model.getMeetRequestModel().getPenitentModel().getName()));
        String status = "";
        switch (String.valueOf(model.getMeetRequestModel().getStatus())) {
            case (AppConstants.RECEIVED):
                status = context.getString(R.string.request_recieved);
                updateAction(viewHolder, context.getString(R.string.confessor_response));
                break;
            case (AppConstants.PENDING):
                status = context.getString(R.string.request_recieved);
                updateAction(viewHolder, context.getString(R.string.confessor_response));
                break;
            case (AppConstants.REFUSED):
                status = context.getString(R.string.request_refused);


                viewHolder.priestRequestListItemAction.setVisibility(View.GONE);
                viewHolder.details.setVisibility(View.GONE);


                break;
            case (AppConstants.ACCEPTED):
                status = context.getString(R.string.request_accepted);
                updateAction(viewHolder, context.getString(R.string.chat));
                break;

        }
        viewHolder.txtSubject.setText(status);
        updateDistance(viewHolder, model);
    }

    private void setValueForUser(ViewHolder viewHolder, ServerNotificationModel model) {
        viewHolder.txtName.setText(String.valueOf(model.getMeetRequestModel().getPriestModel().getName()));

        String status = "";
        switch (String.valueOf(model.getMeetRequestModel().getStatus())) {

            case (AppConstants.PENDING):

                status = context.getString(R.string.request_sent);
                updateAction(viewHolder, context.getString(R.string.see_details));

                break;
            case (AppConstants.REFUSED):
                status = context.getString(R.string.request_refused);
                updateAction(viewHolder, context.getString(R.string.see_details));

                break;
            case (AppConstants.ACCEPTED):
                status = context.getString(R.string.request_accepted);
                updateAction(viewHolder, context.getString(R.string.chat));
                break;
            case (AppConstants.SENT):

                status = context.getString(R.string.request_sent);
                updateAction(viewHolder, context.getString(R.string.see_details));
                break;
        }


        viewHolder.txtSubject.setText(status);
        updateDistance(viewHolder, model);
    }

    private void updateDistance(ViewHolder viewHolder, ServerNotificationModel model) {
        if (myLocation != null
                && model.getMeetRequestModel().getPenitentModel().getLatitude() != null
                && model.getMeetRequestModel().getPenitentModel().getLongitude() != null) {

            viewHolder.txtDistance.setText(LocationUtils.getReadableDistance(context
                    , myLocation.latitude
                    , myLocation.longitude
                    , Double.valueOf(model.getMeetRequestModel().getPenitentModel().getLatitude())
                    , Double.valueOf(model.getMeetRequestModel().getPenitentModel().getLongitude())));
        }
    }

    private void updateAction(ViewHolder viewHolder, String action) {
        viewHolder.priestRequestListItemAction.setText(action);
    }

    @Override
    public int getItemCount() {
        return itemsData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.txt_subject)
        TextView txtSubject;
        @BindView(R.id.txt_distance)
        TextView txtDistance;
        @BindView(R.id.priest_request_list_item_action)
        TextView priestRequestListItemAction;
        @BindView(R.id.mainlayout)
        LinearLayout mainlayout;
        @BindView(R.id.details)
        ImageView details;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            ButterKnife.bind(this, itemLayoutView);
        }
    }
}
