package testtensor.gas;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import testtensor.gas.client.App;
import testtensor.gas.client.Model;
import testtensor.gas.client.JsonStubApi;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "myLogs";

    Context context = this;
    DBHelper dbHelper;
    SQLiteDatabase db;
    Button btnUpdate;
    ConstraintLayout constLayout;
    SharedPreferences prefs = null;

    JsonStubApi jsonStubApi = App.getApi();
    Callback<List<Model>> callback = new Callback<List<Model>>() {
        @Override
        public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
            Log.d(TAG, response.body().toString());
            List<Model> models = new ArrayList<>();
            if(response.body() != null) models.addAll(response.body());
            else Log.d(TAG, "Response NULL");

            //Построение списка с сайта и сохранение его в БД
            dbHelper.removeModels(db);
            dbHelper.insertModels(db, models, true, 0);
            constLayout.removeAllViewsInLayout();
            constLayout.addView(showTree(models).getView());
        }

        @Override
        public void onFailure(Call<List<Model>> call, Throwable t) {
            Log.d(TAG, "ERROR Response");
            Toast toast = Toast.makeText(getApplicationContext(), R.string.toast_connect, Toast.LENGTH_SHORT );
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constLayout = (ConstraintLayout) findViewById(R.id.constlayoutTop);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
        dbHelper = new DBHelper(this);
        prefs = getSharedPreferences("testtensor.GaS", MODE_PRIVATE);

        //Построение списка из БД
        db = dbHelper.getWritableDatabase();
        constLayout.addView(showTree(dbHelper.getModels(db,true,0)).getView());
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnUpdate:
                jsonStubApi.getData().enqueue(callback);
            break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Первый запуск приложения
        if (prefs.getBoolean("firstrun", true)) {
            jsonStubApi.getData().enqueue(callback);
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }

    public AndroidTreeView showTree(List<Model> models){
        TreeContainer treeContainer = new TreeContainer(context);
        treeContainer.insertModels(models);
        AndroidTreeView treeView = new AndroidTreeView(context, treeContainer.getTree());
        treeView.setDefaultAnimation(true);
        treeView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
        treeView.setDefaultViewHolder(Holder.class);
        return treeView;
    }
}
