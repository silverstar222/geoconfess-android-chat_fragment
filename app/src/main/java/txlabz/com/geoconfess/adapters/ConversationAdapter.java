package txlabz.com.geoconfess.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.helpers.ListItemClickListener;
import txlabz.com.geoconfess.models.response.ChatMessage;
import txlabz.com.geoconfess.utils.SharedPreferenceUtils;

/**
 * Created by yagor on 1/15/2016.
 */

public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MY_MSG_TYPE = 1;
    private static final int FOREIGN_MSG_TYPE = 2;

    private final ListItemClickListener listener;
    List<ChatMessage> mItems;
    Context context;


    public ConversationAdapter(List<ChatMessage> items, ListItemClickListener listener) {
        mItems = items;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int type) {
        View view;
        RecyclerView.ViewHolder viewHolder = null;
        context = viewGroup.getContext();
        switch (type) {
            case MY_MSG_TYPE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_msg_layout, viewGroup, false);
                viewHolder = new MyMsgViewHolder(view, context);
                break;
            case FOREIGN_MSG_TYPE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.foreign_msg_layout, viewGroup, false);
                viewHolder = new ForeignMsgViewHolder(view, context);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case MY_MSG_TYPE:
                ((MyMsgViewHolder) holder).mMsg.setText(mItems.get(position).getText());
                break;
            case FOREIGN_MSG_TYPE:
                ((ForeignMsgViewHolder) holder).mMsg.setText(mItems.get(position).getText());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isMyMsg(mItems.get(position).getSenderId())) {
            return MY_MSG_TYPE;
        } else {
            return FOREIGN_MSG_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private boolean isMyMsg(long id) {
        if (SharedPreferenceUtils.getUserId(context) == id) {
            return true;
        } else {
            return false;
        }
    }

    public class MyMsgViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.my_msg_text)
        TextView mMsg;

        public MyMsgViewHolder(View view, Context context) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class ForeignMsgViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.foreign_msg_text)
        TextView mMsg;

        public ForeignMsgViewHolder(View view, Context context) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
