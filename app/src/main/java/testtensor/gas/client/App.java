package testtensor.gas.client;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Drumbl on 19.10.2017.
 */

public class App extends Application {

    private static JsonStubApi jsonStubApi;
    private Retrofit retrofit;

    @Override
    public void onCreate(){
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://jsonstub.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonStubApi = retrofit.create(JsonStubApi.class);
    }
    public static JsonStubApi getApi(){
        return jsonStubApi;
    }
}
