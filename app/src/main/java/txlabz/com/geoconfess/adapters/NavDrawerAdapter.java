package txlabz.com.geoconfess.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import txlabz.com.geoconfess.R;

/**
 * Created by Ivan on 18.5.2016..
 */
public class NavDrawerAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] entities;

    public NavDrawerAdapter(Context context, String[] entities) {
        super(context, 0, entities);
        this.context = context;
        this.entities = entities;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        String entity = entities[position];
        View rowView = inflater.inflate(R.layout.nav_drawer_item, parent, false);
        ViewHolder holder = new ViewHolder(rowView, context);

        updateView(holder, entity);
        return rowView;
    }

    private void updateView(ViewHolder holder, String entity) {
        holder.mItemText.setText(entity);
    }

    public class ViewHolder {

        @BindView(R.id.item_text)
        TextView mItemText;
        @BindView(R.id.nav_item_image)
        ImageView navigationImage;

        public ViewHolder(View view, Context context) {
            ButterKnife.bind(this, view);
        }
    }
}