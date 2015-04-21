package com.chengw.autocallrecorder.adpater;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.chengw.autocallrecorder.QuickContactHelper;
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
        CallHistoryItemHolder holder;

        if (null == convertView) {
            holder = new CallHistoryItemHolder();
            convertView = mLayoutInflater.inflate(R.layout.call_history_item, null);
            holder.contactName = (TextView) convertView.findViewById(R.id.contact_name);
            holder.phoneNumber = (TextView) convertView.findViewById(R.id.phone_number);
            holder.callTime = (TextView) convertView.findViewById(R.id.call_time);
            holder.inCloud = (ImageView) convertView.findViewById(R.id.cloud_img);
            holder.quickContactBadge = (QuickContactBadge) convertView.findViewById(R.id.contact_badge);

            convertView.setTag(holder);
        } else {
            holder = (CallHistoryItemHolder) convertView.getTag();
        }

        String phoneNumber = mCallHistoryList.get(position).getPhoneNumber();
        holder.callTime.setText(mCallHistoryList.get(position).getCallTime());
        holder.phoneNumber.setText(PhoneNumberUtils.formatNumber(phoneNumber));

        QuickContactHelper helper = new QuickContactHelper(mContext, holder.quickContactBadge, phoneNumber);
        String contactName = helper.getContactByNumber();
        helper.addThumbnail();
        if (contactName != null) {
            holder.contactName.setText(contactName);
        } else {
            holder.contactName.setText(phoneNumber);
        }

        return convertView;
    }

    public class CallHistoryItemHolder {
        public TextView phoneNumber;
        public TextView contactName;
        public TextView callTime;
        public ImageView inCloud;
        public QuickContactBadge quickContactBadge;
    }
}
