package com.wenhua.wenhua.adpters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;
import com.wenhua.wenhua.R;
import com.wenhua.wenhua.controllers.CameraAllDetailsActivity;
import com.wenhua.wenhua.controllers.CameraDetailsActivity;
import com.wenhua.wenhua.controllers.LogOwnActivity;
import com.wenhua.wenhua.models.beans.TreeViewBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bogdan Melnychuk on 2/12/15.
 */
public class IconTreeItemHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {
    private TextView tvValue;
    private ImageView arrowView;
    /*按钮*/
    private ImageButton tnButton;
    private ImageButton imageButton;
    private Context mcontext;

    public IconTreeItemHolder(Context context) {
        super(context);
        this.mcontext = context;
    }

    @Override
    public View createNodeView(final TreeNode node, final IconTreeItem value) {

        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.fragment_camera_node_item, null, false);

        tvValue = (TextView) view.findViewById(R.id.tree_node_tv);
        tvValue.setText(value.treeViewBean.getText());

        arrowView = (ImageView) view.findViewById(R.id.tree_node_iv);
        if (value.treeViewBean.isLeaf()) {
            arrowView.setVisibility(View.INVISIBLE);
        }
        imageButton = (ImageButton) view.findViewById(R.id.tree_node_btn_image);
        tnButton = (ImageButton) view.findViewById(R.id.tree_node_btn);
        tnButton.setVisibility(View.VISIBLE);
        if (node.getLevel() == 1) {
            tnButton.setVisibility(View.INVISIBLE);
            imageButton.setVisibility(View.INVISIBLE);
        } else if (value.treeViewBean.isLeaf()) {
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strTower = value.treeViewBean.getText();

                    IconTreeItemHolder.IconTreeItem item1 = (IconTreeItemHolder.IconTreeItem) (node.getParent().getValue());
                    String strLine = item1.treeViewBean.getText();
                    Intent intent = new Intent(mcontext, CameraDetailsActivity.class);
                    intent.putExtra("tips", value.treeViewBean.getTips());
                    intent.putExtra("line", strLine);
                    intent.putExtra("tower", strTower);
                    mcontext.startActivity(intent);
                }
            });
            tnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strTower = value.treeViewBean.getText();
                    IconTreeItemHolder.IconTreeItem item1 = (IconTreeItemHolder.IconTreeItem) (node.getParent().getValue());
                    String strLine = item1.treeViewBean.getText();
                    IconTreeItemHolder.IconTreeItem item2 = (IconTreeItemHolder.IconTreeItem)node.getParent().getParent().getValue();
                    String region = item2.treeViewBean.getText();
                    Intent intent = new Intent(mcontext, LogOwnActivity.class);
                    intent.putExtra("region", region);
                    intent.putExtra("line", strLine);
                    intent.putExtra("tower", strTower);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mcontext.startActivity(intent);
                }
            });
        } else {
            //tnButton.setBackgroundColor(Color.parseColor("#FFE4B5"));
            tnButton.setVisibility(View.INVISIBLE);
            imageButton.setBackgroundResource(R.drawable.shexiangtou1);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IconTreeItemHolder.IconTreeItem item1 = (IconTreeItemHolder.IconTreeItem) (node.getValue());
                    String strLine = item1.treeViewBean.getText();
                    ArrayList<String> tower = new ArrayList<String>();
                    ArrayList<String> tips = new ArrayList<String>();
                    List<TreeNode> allChildren = node.getChildren();
                    for (int i = 0; i < allChildren.size(); i++) {
                        IconTreeItem itChildren = (IconTreeItem) (allChildren.get(i).getValue());
                        String tip = itChildren.treeViewBean.getTips();
                        tips.add(tip);
                        String towers = itChildren.treeViewBean.getText();
                        tower.add(towers);
                    }
                    Intent intent = new Intent(mcontext, CameraAllDetailsActivity.class);
                    intent.putExtra("line", strLine);
                    intent.putStringArrayListExtra("tower", tower);
                    intent.putStringArrayListExtra("tips", tips);
                    mcontext.startActivity(intent);
                }
            });
            /*tnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });*/
        }

        return view;
    }

    @Override
    public void toggle(boolean active) {
        arrowView.setImageResource(active ? R.drawable.tree_c : R.drawable.tree_o);
    }

    public static class IconTreeItem {
        public TreeViewBean treeViewBean;

        public IconTreeItem(TreeViewBean treeViewBean) {
            this.treeViewBean = treeViewBean;
        }
    }
}
