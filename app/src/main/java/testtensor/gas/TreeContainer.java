package testtensor.gas;

import android.content.Context;

import com.unnamed.b.atv.model.TreeNode;

import java.util.ArrayList;
import java.util.List;

import testtensor.gas.client.Model;

/**
 * Created by Администратор on 30.10.2017.
 */

public class TreeContainer {

    private TreeNode root = TreeNode.root();
    Context context;

    public TreeContainer(Context context) {
        this.context = context;
    }


    public TreeNode getTree(){
        return root;
    }

    public void setTree(TreeNode root){
        this.root = root;
    }

    public void insertModels(List<Model> models){
        root.addChildren(getCollection(models));
    }

    private ArrayList<TreeNode> getCollection(List<Model> models){
        ArrayList<TreeNode> list = new ArrayList<>();
        for(Model model: models){
            String title = model.getTitle();
            Holder.IconTreeItem nodeItem = new Holder.IconTreeItem();
            nodeItem.text = title;
            TreeNode parent = new TreeNode(nodeItem).setViewHolder(new Holder(context));
            if(model.getSubs().size()>0){
                parent.addChildren(getCollection(model.getSubs()));
            }
            list.add(parent);
        }
        return list;
    }
}
