package txlabz.com.geoconfess.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yagor on 20/04/2016.
 */
public class AppApiController {

    private static AppAPI apiInstance;
    private static Retrofit retrofit;


    public void init() {
        try {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://geoconfess.herokuapp.com/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiInstance = retrofit.create(AppAPI.class);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public AppAPI getApiInstance() {
        if (apiInstance == null) {
            init();
        }
        return apiInstance;
    }

    public Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            init();
        }
        return retrofit;
    }
}
