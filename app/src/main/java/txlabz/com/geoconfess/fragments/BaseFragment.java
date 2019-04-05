package txlabz.com.geoconfess.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

import txlabz.com.geoconfess.events.BusProvider;

/**
 * Created by Ivan on 30.5.2016..
 */
public class BaseFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        BusProvider.getInstance().unregister(this);


    }
}
