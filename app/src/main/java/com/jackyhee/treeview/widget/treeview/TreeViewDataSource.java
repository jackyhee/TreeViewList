package com.jackyhee.treeview.widget.treeview;

import com.jackyhee.treeview.bean.NodeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hexj
 * @createDate 2020/8/12 9:54
 **/
public class TreeViewDataSource {

    private TreeViewDataSource() {
    }

    private List<TreeViewNode> elements = new ArrayList();
    private List<TreeViewNode> nodes;

    public TreeViewDataSource(List<TreeViewNode> nodes) {
        this.nodes = nodes;
        if (nodes == null) {
            nodes = new ArrayList();
        }
        collectElements(nodes);
    }

    private void collectElements(List<TreeViewNode> nodes) {
        for (TreeViewNode node : nodes) {
            elements.add(node);
            if (node.isExpand == true && node.child != null && node.child.size() > 0) {
                collectElements(node.child);
            }
        }
    }

    public void updateNodes() {
        elements.clear();
        collectElements(nodes);
    }

    public List<TreeViewNode> getElements() {
        return elements;
    }

    public List<TreeViewNode> getNodes() {
        return nodes;
    }
}
