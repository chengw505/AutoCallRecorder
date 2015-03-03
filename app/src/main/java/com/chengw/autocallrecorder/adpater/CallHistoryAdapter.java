package com.chengw.autocallrecorder.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chengw.autocallrecorder.R;
import com.chengw.autocallrecorder.model.ItemCallHistoryModel;

import java.util.ArrayList;

/**
 * Created by Cheng on 3/3/2015.
 */
public class CallHistoryAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<ItemCallHistoryModel> mCallHistoryList;
    private final LayoutInflater mLayoutInflater;

    public CallHistoryAdapter(Context context, ArrayList<ItemCallHistoryModel> callHistoryModels) {
        mContext = context;
        mCallHistoryList = callHistoryModels;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mCallHistoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCallHistoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CallHistoryItemHolder holder = null;

        if (null == convertView) {
            holder = new CallHistoryItemHolder();
            convertView = mLayoutInflater.inflate(R.layout.call_history_item, null);
            holder.contact_name = (TextView) convertView.findViewById(R.id.contact_name);
            holder.phone_number = (TextView) convertView.findViewById(R.id.phone_number);

            convertView.setTag(holder);
        } else {
            holder = (CallHistoryItemHolder) convertView.getTag();
        }

        holder.phone_number.setText(mCallHistoryList.get(position).getPhone_num());
        holder.contact_name.setText(mCallHistoryList.get(position).getContact_name());

        return convertView;
    }

    public class CallHistoryItemHolder {
        TextView phone_number;
        TextView contact_name;
    }
}
