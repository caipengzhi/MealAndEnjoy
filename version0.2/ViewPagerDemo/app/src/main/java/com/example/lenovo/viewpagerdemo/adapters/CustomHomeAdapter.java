package com.example.lenovo.viewpagerdemo.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.viewpagerdemo.CommentList;
import com.example.lenovo.viewpagerdemo.HomeShopList;
import com.example.lenovo.viewpagerdemo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by lenovo on 2018/5/16.
 */

public class CustomHomeAdapter extends BaseAdapter {
    public static final String PIC_URL = "http://10.7.85.138:8080/demo001";
    public String path;
    //上下文环境
    private Context mContext;
    //子项目的布局id
    private int mLayout;
    private List<HomeShopList> shopData;

    public CustomHomeAdapter(Context context, int mLayout, List<HomeShopList> shopData){
        this.mContext = context;
        this.mLayout = mLayout;
        this.shopData = shopData;
        this.path = context.getFilesDir().getAbsolutePath();
    }
    /**
     *
     * @return 返回数据源的数量
     */
    @Override
    public int getCount(){
        return shopData.size();
    }
    /**
     *
     * @param mposition 当前选择的item的id
     * @return 返回选择的item项的数据
     */
    @Override
    public Object getItem(int mposition){
        return shopData.get(mposition);
    }

    /**
     *
     * @param mposition
     * @return 当前选择了第几个item项
     */
    @Override
    public long getItemId(int mposition) {
        return mposition;
    }
    private class ViewHolder{
        private TextView textView;
        private ImageView imageView;
    }
    /**
     *
     * @param mposition
     * @param convertView
     * @param parent
     * @return 返回item项的布局视图组件
     */
    @Override
    public View getView(int mposition, View convertView, ViewGroup parent){
        Log.i("ceshi",path);
        Log.i("ceshi",shopData.get(mposition).getShopimg());
        ViewHolder viewHolder = null;
        if(null == convertView){
            //1. 获取子项目item的布局文件
            //用于加载布局的LayoutInflater对象
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            //使用inflate方法加载布局文件（参数1：布局文件的ID，参数2：ViewGroup）
            //2. 布局文件赋值给convertView参数
            convertView = mInflater.inflate(mLayout, null);
            viewHolder = new ViewHolder();
            //3. 获取布局文件中的控件对象
            viewHolder.textView = convertView.findViewById(R.id.tv_shop_name);
            viewHolder.imageView = convertView.findViewById(R.id.iv_shop);


            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        File file = new File(path,shopData.get(mposition).getShopimg());
        if(!file.exists()){
            downloadpic(shopData.get(mposition).getShopimg(),PIC_URL+shopData.get(mposition).getShopimg());
        }else{
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            viewHolder.imageView.setImageBitmap(bitmap);
        }
        HomeShopList shopList = shopData.get(mposition);
        viewHolder.textView.setText(shopList.getShopname());

        return convertView;
    }

    public void downloadpic(final String name,final String picurl){
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream fout = null;
                InputStream in = null;
                File file = new File(path,name);
                try {
                    fout = new FileOutputStream(file);
                    URL url = new URL(picurl);
                    in = url.openStream();

                    int len = -1;
                    byte[] b = new byte[1024];
                    while ((len = in.read(b)) != -1){
                        fout.write(b,0,len);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if(fout != null){
                            fout.close();
                        }
                        if (in != null){
                            in.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }


}
