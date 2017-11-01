package testtensor.gas.client;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Drumbl on 19.10.2017.
 */

public class Model {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("subs")
    @Expose
    private List<Model> subs = new ArrayList<Model>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Model withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Model withImg(String img) {
        this.img = img;
        return this;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Model withId(int id) {
        this.id = id;
        return this;
    }

    public List<Model> getSubs() {
        return subs;
    }

    public void setSubs(List<Model> subs) {
        this.subs = subs;
    }

    public Model withSubs(List<Model> subs) {
        this.subs = subs;
        return this;
    }

}