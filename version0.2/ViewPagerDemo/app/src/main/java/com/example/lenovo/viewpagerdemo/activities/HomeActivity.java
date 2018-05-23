package com.example.lenovo.viewpagerdemo.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.lenovo.viewpagerdemo.adapters.MyPagerAdapter;
import com.example.lenovo.viewpagerdemo.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnTouchListener{
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //获取控件
        imgbtn_food = findViewById(R.id.imgbtn_food);
        imgbtn_entertain = findViewById(R.id.imgbtn_entertain);
        //给按钮注册事件监听器
        ButtonClickListener listener = new ButtonClickListener();
        imgbtn_food.setOnClickListener(listener);
        imgbtn_entertain.setOnClickListener(listener);

        mHandler = new MyHandler(this);
        //配置轮播图ViewPager
        mViewPager = ((ViewPager) findViewById(R.id.live_view_pager));
        mItems = new ArrayList<>();
        mAdapter = new MyPagerAdapter(mItems, this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnTouchListener(this);
        mViewPager.addOnPageChangeListener(this);
        isAutoPlay = true;

        //TODO: 添加ImageView
        addImageView();
        mAdapter.notifyDataSetChanged();
        //设置底部4个小点
        setBottomIndicator();


    }
    private void addImageView(){
        ImageView view0 = new ImageView(this);
        view0.setImageResource(R.mipmap.p1);
        ImageView view1 = new ImageView(this);
        view1.setImageResource(R.mipmap.p2);
        ImageView view2 = new ImageView(this);
        view2.setImageResource(R.mipmap.p3);
        ImageView view3 = new ImageView(this);
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
        mBottomLiner = (LinearLayout) findViewById(R.id.live_indicator);
        //右下方小圆点
        mBottomImages = new ImageView[mItems.size()];
        for (int i = 0; i < mBottomImages.length; i++) {
            ImageView imageView = new ImageView(this);
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
        private WeakReference<HomeActivity> mWeakReference;

        public MyHandler(HomeActivity activity) {
            mWeakReference = new WeakReference<HomeActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    HomeActivity activity = mWeakReference.get();
                    if (activity.isAutoPlay) {

                        activity.mViewPager.setCurrentItem(++activity.currentViewPagerItem);
                    }

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
                    intent.setClass(HomeActivity.this,
                            DeliciousFoodActivity.class);
                    //3. 进行跳转
                    startActivity(intent);
                    break;
                case R.id.imgbtn_entertain:
                    //页面跳转
                    //1. 创建Intent对象
                    //2. 指定跳转路线
                    intent.setClass(HomeActivity.this,
                            EntertainmentActivity.class);
                    //3. 进行跳转
                    startActivity(intent);
                    break;

            }
        }
    }
}
