package testtensor.gas.client;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import testtensor.gas.client.RetroFitApi;

/**
 * Created by Drumbl on 19.10.2017.
 */

public class InitRetrofit extends Application {

    private static RetroFitApi retroFitApi;
    private Retrofit retrofit;

    @Override
    public void onCreate(){
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://jsonstub.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retroFitApi = retrofit.create(RetroFitApi.class);
    }
    public static RetroFitApi getApi(){
        return retroFitApi;
    }
}
