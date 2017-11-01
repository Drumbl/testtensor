package testtensor.gas.client;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by Администратор on 12.10.2017.
 */

public interface JsonStubApi {

    @Headers({"Content-Type: application/json","JsonStub-User-Key: cbd2482f-5e46-4483-b9f9-ad667183a86b", "JsonStub-Project-Key: 334e7727-f014-493e-87c2-3db789aa802a"})
    @GET("test_api")
    Call<List<Model>>getData();

}
