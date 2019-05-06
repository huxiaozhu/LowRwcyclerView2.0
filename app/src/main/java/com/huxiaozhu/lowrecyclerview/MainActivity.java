package com.huxiaozhu.lowrecyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.huxiaozhu.recyclerviewlib.adapter.BaseAdapter;
import com.huxiaozhu.recyclerviewlib.adapter.GridViewAdapter;
import com.huxiaozhu.recyclerviewlib.adapter.HGridViewAdapter;
import com.huxiaozhu.recyclerviewlib.adapter.HListViewAdapter;
import com.huxiaozhu.recyclerviewlib.adapter.ListViewAdapter;
import com.huxiaozhu.recyclerviewlib.adapter.VariableAdapter;
import com.huxiaozhu.recyclerviewlib.adapter.viewholder.BaseViewHolder;
import com.huxiaozhu.recyclerviewlib.divider.ListDivider;
import com.huxiaozhu.recyclerviewlib.widget.ExpandRecyclerView;
import com.huxiaozhu.recyclerviewlib.widget.SwipeRefreshLayout;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

public class MainActivity extends AppCompatActivity {
    private ExpandRecyclerView mRecyclerView;
    private ListViewAdapter<String> adapter;
    Handler mHandler = new Handler();
    private List<String> mBodies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mRecyclerView = findViewById(R.id.re);

//        setData();
//        setListener();

        test();
    }

    private void test() {
        final SwipeRefreshLayout mPullRefreshRecyclerView = findViewById(R.id.swipe);
        mPullRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mBodies.add("listview");
        mBodies.add("水平ListView");
        mBodies.add("GridView");
        mBodies.add("水平GridView");
        mBodies.add("瀑布流");
        mBodies.add("通讯录顶吸效果");
        mBodies.add("画廊");
        adapter = new ListViewAdapter<String>(mBodies, this) {

            @Override
            protected int getItemLayoutId() {
                return R.layout.item;
            }

            @Override
            public void setData(BaseViewHolder holder, int position, String item) {
                Button button = holder.getButton(R.id.btn);
                button.setText(item);
            }

        };
        mPullRefreshRecyclerView.setAdapter(adapter);

        mPullRefreshRecyclerView.setOnPullListener(new SwipeRefreshLayout.OnPullListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBodies.add(0, "新数据");
                        mPullRefreshRecyclerView.refreshFinish();
                    }
                }, 3000);
            }

            @Override
            public void onLoadMore() {
                final List<String> mMore = new ArrayList<>();

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 3; i++) {
                            mMore.add("more+++" + i);

                        }


                        mBodies.addAll(mMore);
                        mPullRefreshRecyclerView.loadMroeFinish();
                    }
                }, 1500);
            }


        });
    }

    private void setData() {
        List<String> data = new ArrayList<>();
        data.add("listview");
        data.add("水平ListView");
        data.add("GridView");
        data.add("水平GridView");
        data.add("瀑布流");
        data.add("通讯录顶吸效果");
        data.add("画廊");
        adapter = new ListViewAdapter<String>(data, this) {

            @Override
            protected int getItemLayoutId() {
                return R.layout.item;
            }

            @Override
            public void setData(BaseViewHolder holder, int position, String item) {
                Button button = holder.getButton(R.id.btn);
                button.setText(item);
            }

        };

        mRecyclerView.addHeaderView(View.inflate(this, R.layout.header_view, null));
        mRecyclerView.addFooterView(View.inflate(this, R.layout.header_view, null));
        mRecyclerView.setAdapter(adapter);
    }

    private void setListener() {
        adapter.setOnClickListener(R.id.btn, new BaseAdapter.OnClick() {
            @Override
            public void onClick(int position, BaseViewHolder holder) {
                switch (position) {
                    case 0:
                        //ListView
                        Toast.makeText(getBaseContext(), "ListView", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        //水平ListView
                        Toast.makeText(getBaseContext(), "水平ListView", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        //GridView
                        Toast.makeText(getBaseContext(), "GridView", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        //水平GridView
                        Toast.makeText(getBaseContext(), "水平GridView", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        //瀑布流
                        Toast.makeText(getBaseContext(), "瀑布流", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        //通讯录顶吸效果
                        Toast.makeText(getBaseContext(), "通讯录顶吸效果", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, AddressListActivity.class));
                        break;
                    case 6:
                        Toast.makeText(getBaseContext(), "画廊", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
