package com.wenhua.wenhua.models.beans;

import com.wenhua.wenhua.models.BaseModel;

import java.util.List;

/**
 * Created by castiel on 2016/8/4.
 */

public class TreeViewBean extends BaseModel{
    private static final long serialVersionUID = -5656286799462119215L;

    private String id;
    private String text;
    private String iconCls;
    private boolean leaf;
    private String camadr;
    private String tips;
    private boolean expanded;
    private List<TreeViewBean> children;

    public List<TreeViewBean> getChildren() {
        return children;
    }

    public void setChildren(List<TreeViewBean> children) {
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public String getCamadr() {
        return camadr;
    }

    public void setCamadr(String camadr) {
        this.camadr = camadr;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public String toString() {
        return "TreeViewBean{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", iconCls='" + iconCls + '\'' +
                ", leaf=" + leaf +
                ", camadr='" + camadr + '\'' +
                ", tips='" + tips + '\'' +
                ", expanded=" + expanded +
                ", children=" + children +
                '}';
    }
}
