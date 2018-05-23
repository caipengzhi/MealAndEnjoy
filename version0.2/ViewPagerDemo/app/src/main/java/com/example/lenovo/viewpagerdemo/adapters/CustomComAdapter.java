package com.example.lenovo.viewpagerdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.viewpagerdemo.CommentList;
import com.example.lenovo.viewpagerdemo.R;

import java.util.List;

/**
 * Created by lenovo on 2018/5/18.
 */

public class CustomComAdapter extends BaseAdapter{

    private Context context ;
    private int mLayout;
    private List<CommentList> comData;

    public CustomComAdapter(Context context, int mLayout, List<CommentList> comData){
        this.context = context;
        this.mLayout = mLayout;
        this.comData = comData;
    }
    /**
     *
     * @return 返回数据源的数量
     */
    @Override
    public int getCount(){
        return comData.size();
    }
    /**
     *
     * @param position 当前选择的item的id
     * @return 返回选择的item项的数据
     */
    @Override
    public Object getItem(int position){
        return comData.get(position);
    }

    /**
     *
     * @param position
     * @return 当前选择了第几个item项
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return 返回item项的布局视图组件
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(null == convertView){
            //1. 获取子项目item的布局文件
            //用于加载布局的LayoutInflater对象
            LayoutInflater mInflater = LayoutInflater.from(context);
            //使用inflate方法加载布局文件（参数1：布局文件的ID，参数2：ViewGroup）
            //2. 布局文件赋值给convertView参数
            convertView = mInflater.inflate(mLayout, null);
            //3. 获取布局文件中的控件对象
            TextView com_title = convertView.findViewById(R.id.comment_item_title);
            ImageView com_img = convertView.findViewById(R.id.comment_item_img);

            CommentList commentList = comData.get(position);
            com_title.setText(commentList.getItem_title());
            com_img.setImageResource(commentList.getImg_id());
        }
        return convertView;
    }
}
