package com.liuxiaozhu.lowrecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.liuxiaozhu.recyclerviewlib.adapter.ListViewAdapter;
import com.liuxiaozhu.recyclerviewlib.adapter.viewholder.BaseViewHoloder;
import com.liuxiaozhu.recyclerviewlib.utils.LowRecyclerViewUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.re);
        List<String> data = new ArrayList<>();
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        ListViewAdapter<String> adapter = new ListViewAdapter<String>(data, this, R.layout.item) {
            @Override
            protected void setData(BaseViewHoloder holder, int position, String item) {
                holder.getTextView(R.id.textView).setText(item);
            }
        };
        new LowRecyclerViewUtils<>(mRecyclerView, 0, adapter);
        mRecyclerView.setAdapter(adapter);
    }
}
