package com.chengw.autocallrecorder.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chengw.autocallrecorder.R;
import com.chengw.autocallrecorder.model.ItemSlidingMenuModel;

import java.util.ArrayList;

/**
 * Created by Cheng on 3/2/2015.
 */
public class SlidingMenuAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<ItemSlidingMenuModel> mSlidingMenuList;
    private final LayoutInflater mLayoutInflater;

    public SlidingMenuAdapter(Context context, ArrayList<ItemSlidingMenuModel> mSlidingMenuModels) {
        mContext = context;
        mSlidingMenuList = mSlidingMenuModels;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mSlidingMenuList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSlidingMenuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SlidingMenuItemHolder holder = null;

        if(null == convertView) {
            holder = new SlidingMenuItemHolder();
            convertView = mLayoutInflater.inflate(R.layout.sliding_menu_item, null);
            holder.img = (ImageView)convertView.findViewById(R.id.sliding_menu_image);
            holder.text = (TextView)convertView.findViewById(R.id.sliding_menu_text);

            convertView.setTag(holder);
        } else {
            holder = (SlidingMenuItemHolder)convertView.getTag();
        }

        holder.img.setImageResource(mSlidingMenuList.get(position).getId());
        holder.text.setText(mSlidingMenuList.get(position).getName());

        return convertView;
    }

    public class SlidingMenuItemHolder {
        TextView text;
        ImageView img;
    }
}
