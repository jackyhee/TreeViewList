package com.jackyhee.treeview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jackyhee.treeview.bean.NodeBean;
import com.jackyhee.treeview.widget.treeview.BaseListViewAdapter;
import com.jackyhee.treeview.widget.treeview.TreeViewDataSource;
import com.jackyhee.treeview.widget.treeview.TreeViewNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int NODE_MARGIN_LEFT = 12;//px
    private int mCurrExpandIndex;
    private String mCurrExpandOrgId;
    private NodeBean mNodeBean;
    private ListView mListView;
    private ListViewAdapter adapter;
    private TreeViewDataSource dataSource;
    final String[][] CITY = new String[][]{{"南京","苏州","无锡"},{"济南","青岛","日照"},{"杭州","宁波","温州"}};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initData();
        initView();
    }

    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    private void initData() {
        List list = new ArrayList();
        String[] country = getResources().getStringArray(R.array.country);
        String[] province = getResources().getStringArray(R.array.province);
        for(String c:country) {
            NodeBean countryBean = new NodeBean();
            countryBean.name = c;
            TreeViewNode<NodeBean> countryNode = new TreeViewNode();
            countryNode.data = countryBean;
            list.add(countryNode);
        }

        TreeViewNode<NodeBean> firstNode = (TreeViewNode<NodeBean>) list.get(0);
        addNode(firstNode, Arrays.asList(province),false);
        for(int i = 0;i <firstNode.child.size();i++) {
            addNode(firstNode.child.get(i),Arrays.asList(CITY[i]),true);
        }

        dataSource = new TreeViewDataSource(list);
    }

    public void addNode(TreeViewNode<NodeBean> rootNode,List<String> childName,boolean isLeaf) {

        if(rootNode.child != null) {
            rootNode.child.clear();
        } else {
            rootNode.child = new ArrayList<TreeViewNode>();
        }

        for(String s:childName) {
            NodeBean childBean = new NodeBean();
            childBean.name = s;
            TreeViewNode<NodeBean> childNode = new TreeViewNode();
            childNode.data = childBean;
            childNode.isLeaf = isLeaf;
            childNode.maginLeft = dp2px(this,NODE_MARGIN_LEFT) + rootNode.maginLeft;
            rootNode.child.add(childNode);
        }
    }

    private void initView() {
        mListView=findViewById(R.id.organization_listview);
        mListView.setOnItemClickListener(this);
        adapter = new ListViewAdapter();
        mListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TreeViewNode node =  dataSource.getElements().get(position);
        if (node.isLeaf) {
            node.isSelected = !node.isSelected;//节点状态变化
            adapter.notifyItemChanged(mListView, position);
        } else {
            node.isExpand = !node.isExpand;
            dataSource.updateNodes();//节点数量变化
            adapter.notifyDataSetChanged();
        }
    }

    class ListViewAdapter extends BaseListViewAdapter<BaseListViewAdapter.BaseListViewHolder> {

        @Override
        protected BaseListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_org_tree, parent, false);
            TreeViewHolder holder = new TreeViewHolder(view);
            return holder;
        }

        @Override
        protected void onBindViewHolder(BaseListViewHolder holder, final int position) {
            TreeViewNode<NodeBean> node = (TreeViewNode) getItem(position);
            TreeViewHolder treeViewHolder = (TreeViewHolder) holder;
            if(!node.isLeaf) {
                treeViewHolder.ivSelected.setVisibility(View.INVISIBLE);
                treeViewHolder.ivExpand.setVisibility(View.VISIBLE);
                NodeBean nodeBean =  node.data;
                treeViewHolder.textView.setText(nodeBean.name);

                //是否展开
                if (node.isExpand) {
                    treeViewHolder.ivExpand.setImageDrawable(getResources().getDrawable((R.drawable.ic_org_expanded)));
                } else {
                    treeViewHolder.ivExpand.setImageDrawable(getResources().getDrawable((R.drawable.ic_org_unexpand)));
                }
            } else {
                treeViewHolder.ivExpand.setVisibility(View.INVISIBLE);
                treeViewHolder.ivSelected.setVisibility(View.VISIBLE);
                NodeBean nodeBean =  node.data;
                treeViewHolder.textView.setText(nodeBean.name);

                //是否选中开关
                if (node.isSelected) {
                    treeViewHolder.ivSelected.setImageDrawable(getResources().getDrawable((R.drawable.ic_org_tree_item_selected)));
                } else {
                    treeViewHolder.ivSelected.setImageDrawable(getResources().getDrawable((R.drawable.ic_org_tree_item_unselect)));
                }

            }
            treeViewHolder.itemView.setPadding(node.maginLeft, 0, 0, 0);

        }

        @Override
        public int getCount() {
            int count = dataSource.getElements().size();
            return count;
        }

        @Override
        public Object getItem(int position) {
            return dataSource.getElements().get(position);
        }
    }

    class TreeViewHolder extends BaseListViewAdapter.BaseListViewHolder {

        public TextView textView;
        public ImageView ivSelected;
        public ImageView ivExpand;

        public TreeViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv);
            ivSelected = (ImageView) itemView.findViewById(R.id.iv_selected);
            ivExpand = (ImageView) itemView.findViewById(R.id.iv_expand);
        }
    }
}