package txlabz.com.geoconfess.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.constants.AppConstants;
import txlabz.com.geoconfess.helpers.ListItemClickListener;
import txlabz.com.geoconfess.models.response.MySpotResponse;
import txlabz.com.geoconfess.models.response.Recurrences;

/**
 * Created by yagor on 1/15/2016.
 */

public class SpotsAdapter extends RecyclerView.Adapter<SpotsAdapter.ViewHolder> {

    private static final String TAG = "MyActivity";
    private int item;
    private static final int TYPE_ODD = 1;
    private static final int TYPE_EVEN = 2;

    private final ListItemClickListener listener;
    List<MySpotResponse> mItems;
    Context context;

    public SpotsAdapter(List<MySpotResponse> items, ListItemClickListener listener) {
        mItems = items;
        this.listener = listener;
        int x=0;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.spot_list_item, viewGroup, false);
        context = viewGroup.getContext();

        return new ViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        prepareDelete(viewHolder, i);
        prepareEdit(viewHolder, i);
        if (i % 2 != 0) {

            viewHolder.spotname.setText(mItems.get(i).getName());
            viewHolder.spottime.setText(createWorkingTime(mItems.get(i).getRecurrences()));
            viewHolder.mainlayout.setBackgroundResource(R.drawable.red_backgroung_list);
            viewHolder.spotname.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.spottime.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.edit.setImageResource(R.drawable.ic_edit_white);
            viewHolder.delet.setImageResource(R.drawable.ic_delete_white);

        } else {
            viewHolder.spotname.setText(mItems.get(i).getName());
            viewHolder.spottime.setText(createWorkingTime(mItems.get(i).getRecurrences()));
            viewHolder.mainlayout.setBackgroundResource(R.drawable.light_gray_shape);
            viewHolder.spotname.setTextColor(context.getResources().getColor(R.color.black));
            viewHolder.spottime.setTextColor(context.getResources().getColor(R.color.black));
            viewHolder.edit.setImageResource((R.drawable.ic_edit));
            viewHolder.delet.setImageResource(R.drawable.ic_delete);


        }

    }

    public String createWorkingTime(Recurrences[] recurrences) {
        String time = "";
        if (recurrences != null) {

            for (Recurrences r : recurrences) {
                time = time + r.getStartTime() + "h" + "-" + r.getStopTime() + "h";
                if (r.getDate() != null) {
                    time = time + ", " + r.getDate();
                } else if (r.getDays() != null) {
                    for (String s : r.getDays()) {
                        String temp = "";
                        switch (s) {
                            case (AppConstants.MONDAY):
                                temp = "Lu";
                                break;
                            case (AppConstants.TUESDAY):
                                temp = "Ma";
                                break;
                            case (AppConstants.WEDNESDAY):
                                temp = "Me";
                                break;
                            case (AppConstants.THURSDAY):
                                temp = "Je";
                                break;
                            case (AppConstants.FRIDAY):
                                temp = "Ve";
                                break;
                            case (AppConstants.SATURDAY):
                                temp = "Sa";
                                break;
                            case (AppConstants.SUNDAY):
                                temp = "Di";
                                break;
                        }
                        time = time + ", " + temp;
                    }
                }
                time = time + "\n";
            }
        }

        return time;
    }

    private void prepareDelete(ViewHolder viewHolder, int i) {
        viewHolder.delet.setTag(i);
        viewHolder.delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                listener.onItemClick(position, mItems.get(position).getId(), v.getId());
            }
        });
    }

    private void prepareEdit(ViewHolder viewHolder, int i) {
        viewHolder.edit.setTag(i);
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                listener.onItemClick(position, mItems.get(position).getId(), v.getId());
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.mainlayout)
        LinearLayout mainlayout;
        @BindView(R.id.edit)
        ImageView edit;
        @BindView(R.id.delet)
        ImageView delet;
        @BindView(R.id.spotname)
        TextView spotname;
        @BindView(R.id.spottime)
        TextView spottime;

        public ViewHolder(View view, Context context) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
