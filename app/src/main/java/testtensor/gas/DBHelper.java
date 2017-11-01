package testtensor.gas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import testtensor.gas.client.Model;

/**
 * Created by Drumbl on 12.10.2017.
 */

public class DBHelper extends SQLiteOpenHelper{

    DBHelper(Context context) {
        super(context, "modelsDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Таблица моделей
        db.execSQL("CREATE TABLE models ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "title TEXT,"
                + "img TEXT,"
                + "id INTEGER,"
                + "is_root INTEGER"
                + ");");
        //Таблица связей
        db.execSQL("CREATE TABLE treePath ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "parent INTEGER,"
                + "child INTEGER"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Добавление моделей в БД
     * @param db БД
     * @param models Коллекция моделей из JSON
     * @param is_root Флаг корневого элемента
     * @param id_parent ID родителя, при is_root == true; id_parent = 0;
     */
    public void insertModels(SQLiteDatabase db, List<Model> models, boolean is_root, long id_parent){
        ContentValues cvModel = new ContentValues();
        ContentValues cvTreePath = new ContentValues();
        for(Model model: models){
            long id = 0;
            cvModel.clear();
            cvModel.put("title",model.getTitle());
            cvModel.put("img",model.getImg());
            cvModel.put("id", model.getId());
            cvModel.put("is_root", is_root? 1 : 0);
            id = db.insert("models",null,cvModel);

            if(id_parent > 0){
                cvTreePath.clear();
                cvTreePath.put("parent", id_parent);
                cvTreePath.put("child", id);
                db.insert("treePath", null, cvTreePath);
            }
            if(model.getSubs().size() > 0){
                insertModels(db, model.getSubs(), false, id);
            }
        }
    }

    /**
     *
     * @param db БД
     * @param is_root Флаг корневого элемента
     * @param id_parent ID родителя, при is_root == true; id_parent = 0;
     * @return Возвращает коллекцию моделей
     */
    public List<Model> getModels(SQLiteDatabase db, boolean is_root, long id_parent){
        List<Model> models = new ArrayList<Model>();

        if(is_root){
            Cursor cursor = db.query("models", null, "is_root > ?", new String[]{"0"}, null, null, null);
            if(cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        models.add(setDB(db, cursor));
                    }while (cursor.moveToNext());
                }
            }
            cursor.close();
        }
        else {
            ArrayList<Long> list = getListChildren(db, id_parent);
            for(Long l: list){
                Cursor cursor = db.query("models", null, "_id = ?", new String[]{l.toString()}, null, null, null);
                if(cursor != null) {
                    if(cursor.moveToFirst()) {
                        do{
                            models.add(setDB(db, cursor));
                        }while (cursor.moveToNext());
                    }
                }
                cursor.close();
            }
        }
        return models;
    }

    /**
     *
     * @param db БД
     * @param id_parent ID родителя потомков которого необходимо найти
     * @return Возвращает список потомков
     */
    private ArrayList<Long> getListChildren(SQLiteDatabase db, long id_parent){
        Cursor cursor = db.query("treePath", null, "parent = ?", new String[]{String.valueOf(id_parent)}, null, null, null);
        ArrayList<Long> children = new ArrayList<>();
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    children.add((long)cursor.getInt(cursor.getColumnIndex("child")));
                }
                while (cursor.moveToNext());
            }
        }
        cursor.close();
        return children;
    }

    private Model setDB(SQLiteDatabase db, Cursor cursor){
        Model model = new Model();
        model.setTitle(cursor.getString(cursor.getColumnIndex("title")));
        model.setImg(cursor.getString(cursor.getColumnIndex("img")));
        model.setId(cursor.getInt(cursor.getColumnIndex("id")));
        if(getListChildren(db, cursor.getInt(cursor.getColumnIndex("_id"))).size()>0){
            model.setSubs(getModels(db, false, cursor.getInt(cursor.getColumnIndex("_id"))));
        }
        return model;
    }

    public void removeModels(SQLiteDatabase db){
        db.delete("models", null, null);
        db.delete("treePath", null, null);
        db.delete("sqlite_sequence", null, null);
    }
}
