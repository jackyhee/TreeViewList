package com.jackyhee.treeview.widget.treeview;

import java.util.List;

/**
 * @author hexj
 * @createDate 2020/8/12 9:54
 **/
public class TreeViewNode<T> {
    public T data;
    public boolean isLeaf;//是否为叶子节点（不再包含子节点）
    public boolean isExpand;
    public boolean isSelected;//是否选中
    public int maginLeft;//距左边距离
    public List<TreeViewNode> child;
}
