package testtensor.gas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;

/**
 * Created by Drumbl on 01.11.2017.
 */

public class Holder extends TreeNode.BaseNodeViewHolder<Holder.IconTreeItem> {


    public Holder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_node, null, false);
        TextView tvValue = (TextView) view.findViewById(R.id.node_value);
        tvValue.setText(value.text);

        return view;
    }

    public static class IconTreeItem {
        public int icon;
        public String text;
    }
}
