package com.example.lenovo.viewpagerdemo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.lenovo.viewpagerdemo.CommentList;
import com.example.lenovo.viewpagerdemo.adapters.CustomComAdapter;
import com.example.lenovo.viewpagerdemo.R;

import java.util.ArrayList;
import java.util.List;

public class ShopDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);
        ListView listView = findViewById(R.id.comment_list);

        final CustomComAdapter adapter = new CustomComAdapter(this,R.layout.commen_item,prepaerDate());

        listView.setAdapter(adapter);
    }
    private List<CommentList> prepaerDate(){
        List<CommentList> comList = new ArrayList<>();

        CommentList meituan = new CommentList();
        meituan.setItem_title("查看“美团”评论");
        meituan.setImg_id(R.drawable.arrow);
        comList.add(meituan);

        CommentList dazhong = new CommentList();
        dazhong.setItem_title("查看“大众点评”评论");
        dazhong.setImg_id(R.drawable.arrow);
        comList.add(dazhong);

        return comList;
    }
}
