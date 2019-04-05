package txlabz.com.geoconfess.events;

import com.squareup.otto.Bus;

/**
 * Created by Miroslav on 31.10.2015..
 */
public class BusProvider {
    private static final Bus BUS = new Bus();

    private BusProvider() {
        // No instances.
    }

    public static Bus getInstance() {
        return BUS;
    }
}