package com.example.lenovo.viewpagerdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenovo.viewpagerdemo.R;

/**
 * Created by lenovo on 2018/5/23.
 */

public class Mine_Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //加载选项卡对应的选项页面
        View view = inflater.inflate(R.layout.activity_mine,container,false);
        //获取布局文件中控件对象

        //返回选项卡对应的选项页面
        return view;
    }
}
