package com.example.lenovo.viewpagerdemo.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.lenovo.viewpagerdemo.HomeShopList;
import com.example.lenovo.viewpagerdemo.R;
import com.example.lenovo.viewpagerdemo.activities.DeliciousFoodActivity;
import com.example.lenovo.viewpagerdemo.activities.EntertainmentActivity;
import com.example.lenovo.viewpagerdemo.activities.HomeActivity;
import com.example.lenovo.viewpagerdemo.adapters.CustomHomeAdapter;
import com.example.lenovo.viewpagerdemo.adapters.MyPagerAdapter;
import com.example.lenovo.viewpagerdemo.entity.ShopDemo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lenovo on 2018/5/23.
 */

public class Home_Fragment extends Fragment implements ViewPager.OnPageChangeListener, View.OnTouchListener {

    public static final int VIEW_PAGER_DELAY = 2000;
    private MyPagerAdapter mAdapter;
    private List<ImageView> mItems;
    private ImageView[] mBottomImages;
    private LinearLayout mBottomLiner;
    private ViewPager mViewPager;

    private int currentViewPagerItem;
    //是否自动播放
    private boolean isAutoPlay;

    private MyHandler mHandler;
    private Thread mThread;
    //-----以上为轮播图所用------------
    private ImageButton imgbtn_food;
    private ImageButton imgbtn_entertain;

    //主线程新建handler获得子线程服务器请求数据更新UI
    private int[] a = {R.drawable.demo01,R.drawable.demo02,R.drawable.demo03,R.drawable.demo04,R.drawable.demo05};
    private ListView listView;
    private OkHttpClient ok;
    private Thread thread;
    private ArrayList<String> s;
    private Bitmap bitmap;
    private ImageView demo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //加载选项卡对应的选项页面
        View view = inflater.inflate(R.layout.activity_home,container,false);
        //获取布局文件中控件对象

        //返回选项卡对应的选项页面
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //获取控件
        imgbtn_food = getActivity().findViewById(R.id.imgbtn_food);
        imgbtn_entertain = getActivity().findViewById(R.id.imgbtn_entertain);
        //给按钮注册事件监听器
        ButtonClickListener listener = new ButtonClickListener();
        imgbtn_food.setOnClickListener(listener);
        imgbtn_entertain.setOnClickListener(listener);

        mHandler = new MyHandler(this);
        //配置轮播图ViewPager
        mViewPager = ((ViewPager) getActivity().findViewById(R.id.live_view_pager));
        mItems = new ArrayList<>();
        mAdapter = new MyPagerAdapter(mItems, getActivity());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnTouchListener(this);
        mViewPager.addOnPageChangeListener(this);
        isAutoPlay = true;

        //TODO: 添加ImageView
        addImageView();
        mAdapter.notifyDataSetChanged();
        //设置底部4个小点
        setBottomIndicator();
        //添加listview
        demo = getActivity().findViewById(R.id.demo);
        ok = new OkHttpClient();
        thread = new Thread(new MyThread());
        listView = getActivity().findViewById(R.id.lv_shops);
        thread.start();



    }

    private void addImageView(){
        ImageView view0 = new ImageView(getContext());
        view0.setImageResource(R.mipmap.p1);
        ImageView view1 = new ImageView(getContext());
        view1.setImageResource(R.mipmap.p2);
        ImageView view2 = new ImageView(getContext());
        view2.setImageResource(R.mipmap.p3);
        ImageView view3 = new ImageView(getContext());
        view3.setImageResource(R.mipmap.p4);

        view0.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view3.setScaleType(ImageView.ScaleType.CENTER_CROP);

        mItems.add(view0);
        mItems.add(view1);
        mItems.add(view2);
        mItems.add(view3);
    }

    private void setBottomIndicator() {
        //获取指示器(下面三个小点)
        mBottomLiner = (LinearLayout) getActivity().findViewById(R.id.live_indicator);
        //右下方小圆点
        mBottomImages = new ImageView[mItems.size()];
        for (int i = 0; i < mBottomImages.length; i++) {
            ImageView imageView = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.setMargins(5, 0, 5, 0);
            imageView.setLayoutParams(params);
            //如果当前是第一个 设置为选中状态
            if (i == 0) {
                imageView.setImageResource(R.drawable.indicator_select);
            } else {
                imageView.setImageResource(R.drawable.indicator_no_select);
            }
            mBottomImages[i] = imageView;
            //添加到父容器
            mBottomLiner.addView(imageView);
        }

        //让其在最大值的中间开始滑动, 一定要在 mBottomImages初始化之前完成
        int mid = MyPagerAdapter.MAX_SCROLL_VALUE / 2;
        mViewPager.setCurrentItem(mid);
        currentViewPagerItem = mid;

        //定时发送消息
        mThread = new Thread(){
            @Override
            public void run() {
                super.run();
                while (true) {
                    mHandler.sendEmptyMessage(0);
                    try {
                        Thread.sleep(HomeActivity.VIEW_PAGER_DELAY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        mThread.start();

//        thread = new Thread(){
//            @Override
//        public void run(){
//            String str = "首页list请求";
//            MediaType type = MediaType.parse("text/plain;charset=UTF-8");
//            RequestBody body = RequestBody.create(type,str);
//            Request.Builder builder = new Request.Builder();
//
//            builder.url("http://172.16.23.47:8080/demo001/ShopDemo/homelist.action");
//            builder.post(body);
//            Request request = builder.build();
//            Call call = ok.newCall(request);
//
//            try {
//                Response response = call.execute();
//                Log.i("demo",response.body().string());
//                String jshoplist = response.body().string();
//                Gson gson = new Gson();
//                List<ShopDemo> shoplist = gson.fromJson(jshoplist,new TypeToken<List<ShopDemo>>(){}.getType());
//                Message msg = Message.obtain();
//                Bundle b = new Bundle();
//                for(int i = 0;i<shoplist.size();i++){
//                    String n = String.valueOf(i);
//                    b.putString(n,shoplist.get(i).getShopdName());
//
//                }
//                msg.setData(b);
//                msg.what = 1;
//                mHandler.sendMessage(msg);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            mHandler.removeCallbacks(thread);
//        }
//        };
//        thread.start();
    }






    ///////////////////////////////////////////////////////////////////////////
    // ViewPager的监听事件
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

        currentViewPagerItem = position;
        if (mItems != null) {
            position %= mBottomImages.length;
            int total = mBottomImages.length;

            for (int i = 0; i < total; i++) {
                if (i == position) {
                    mBottomImages[i].setImageResource(R.drawable.indicator_select);
                } else {
                    mBottomImages[i].setImageResource(R.drawable.indicator_no_select);
                }
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isAutoPlay = false;
                break;
            case MotionEvent.ACTION_UP:
                isAutoPlay = true;
                break;
        }
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 为防止内存泄漏, 声明自己的Handler并弱引用Activity
    ///////////////////////////////////////////////////////////////////////////
    private static class MyHandler extends Handler {
        private WeakReference<Home_Fragment> mWeakReference;

        public MyHandler(Home_Fragment activity) {
            mWeakReference = new WeakReference<Home_Fragment>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Home_Fragment activity = mWeakReference.get();
                    if (activity.isAutoPlay) {

                        activity.mViewPager.setCurrentItem(++activity.currentViewPagerItem);
                    }

                    break;
                case 1:
                    final Home_Fragment activity1 = mWeakReference.get();
                    Log.i("ceshi","success");
                    activity1.s = new ArrayList<>();
                    Bundle b = msg.getData();
                    Set<String> keySet = b.keySet();
                    Gson gson = new Gson();
                    for(String key:keySet){
                        ShopDemo shopDemo = gson.fromJson((String)b.get(key),ShopDemo.class);
                        activity1.s.add(shopDemo.getShopdName());
                        activity1.s.add(shopDemo.getShopimg());
                    }
                    Log.i("ceshi",activity1.s.toString());
                    activity1.listView = activity1.getActivity().findViewById(R.id.lv_shops);
                    CustomHomeAdapter customHomeAdapter = new CustomHomeAdapter(activity1.getContext(),R.layout.main_list_item,activity1.prepaerDate(activity1.s));

                    activity1.listView.setAdapter(customHomeAdapter);
                    ViewGroup.LayoutParams params = activity1.listView.getLayoutParams();
                    ListAdapter listAdapter = activity1.listView.getAdapter();
                    View listitem = listAdapter.getView(0,null,activity1.listView);
                    listitem.measure(0,0);

                    params.height = listAdapter.getCount() * listitem.getMeasuredHeight();
                    activity1.listView.setLayoutParams(params);
                    break;
            }

        }
    }
    class ButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()){
                case R.id.imgbtn_food:
                    //页面跳转
                    //1. 创建Intent对象
                    //2. 指定跳转路线
                    intent.setClass(getContext(),
                            DeliciousFoodActivity.class);
                    //3. 进行跳转
                    startActivity(intent);
                    break;
                case R.id.imgbtn_entertain:
                    //页面跳转
                    //1. 创建Intent对象
                    //2. 指定跳转路线
                    intent.setClass(getContext(),
                            EntertainmentActivity.class);
                    //3. 进行跳转
                    startActivity(intent);
                    break;

            }
        }
    }

    public List<HomeShopList> prepaerDate(ArrayList<String> list){
        List<HomeShopList> shopLists = new ArrayList<>();
        File dir = new File(getActivity().getFilesDir().getAbsolutePath()+"/img");
        if(!dir.exists()){
            dir.mkdir();
        }
        for(int i =0;i < list.size();i++){
            Log.i("ceshi",i+getActivity().getFilesDir().getAbsolutePath()+list.get(i+1));
            HomeShopList shopList = new HomeShopList();
            shopList.setShopname(list.get(i));
            shopList.setShopimg(list.get(i+1));
            shopLists.add(shopList);
            i=i+1;
        }
        return shopLists;
    }

    //请求图片list子线程
    class MyThread extends Thread {
        @Override
        public void run(){
            String str = "首页list请求";
            MediaType type = MediaType.parse("text/plain;charset=UTF-8");
            RequestBody body = RequestBody.create(type,str);
            Request.Builder builder = new Request.Builder();
            builder.url("http://10.7.85.138:8080/demo001/ShopDemo/homelist.action");
            //builder.url("http://172.16.12.228:8080/demo001/ShopDemo/homelist.action");
            builder.post(body);
            Request request = builder.build();
            Call call = ok.newCall(request);

            try {
                Response response = call.execute();

                String jshoplist = response.body().string();
                Gson gson = new Gson();
                List<ShopDemo> shoplist = gson.fromJson(jshoplist,new TypeToken<List<ShopDemo>>(){}.getType());
                Message msg = Message.obtain();
                Bundle b = new Bundle();
                for(int i = 0;i<shoplist.size();i++){
                    String n = String.valueOf(i);

                    b.putString(n,gson.toJson(shoplist.get(i)));

                }
                msg.setData(b);
                msg.what = 1;
                mHandler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mHandler.removeCallbacks(thread);
        }
    }




}
