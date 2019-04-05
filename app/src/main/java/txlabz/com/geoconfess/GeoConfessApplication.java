package txlabz.com.geoconfess;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import retrofit2.Retrofit;
import txlabz.com.geoconfess.network.AppAPI;
import txlabz.com.geoconfess.network.AppApiController;

/**
 * Created by Ivan on 16.5.2016..
 */
public class GeoConfessApplication extends Application {
    private static GeoConfessApplication instance;
    private static AppApiController apiControler;

    private static void initRetrofit() {
        apiControler = new AppApiController();
        apiControler.init();
    }

    public static GeoConfessApplication getAppInstance() {
        return instance;
    }

    public static AppAPI getAppApi() {
        if (apiControler == null) {
            initRetrofit();
        }
        return apiControler.getApiInstance();
    }

    public static Retrofit getRetrofit() {
        if (apiControler == null) {
            initRetrofit();
        }
        return apiControler.getRetrofitInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        instance = this;
        initRetrofit();
    }
}