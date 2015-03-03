package com.chengw.autocallrecorder;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.chengw.autocallrecorder.adpater.SlidingMenuAdapter;
import com.chengw.autocallrecorder.custom.SlidingMenuListView;
import com.chengw.autocallrecorder.model.ItemSlidingMenuModel;

import java.util.ArrayList;

/**
 * Created by Cheng on 3/2/2015.
 */
public class LeftSlidingMenuFragment extends Fragment {

    private View mView;
    private ListView mSlidingMenu;
    private ArrayList<ItemSlidingMenuModel> mSlidingMenuModels;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(null == mView) {
            // create a new one
            mView = inflater.inflate(R.layout.sliding_menu, container, false);

            initialMenu();
        }

        return mView;
    }

    private void initialMenu() {
        Context context = mView.getContext();
        mSlidingMenu = (ListView)mView.findViewById(R.id.sliding_menu);

        mSlidingMenuModels = new ArrayList<ItemSlidingMenuModel>();

        // get sliding menu
        String[] arraysSlidingMenu = context.getResources().getStringArray(R.array.arrays_slidingmenu);

        for (int i = 0; i < arraysSlidingMenu.length; ++i) {
            mSlidingMenuModels.add(new ItemSlidingMenuModel(R.drawable.settings_blue, arraysSlidingMenu[i]));
        }

        // bind data
        mSlidingMenu.setAdapter(new SlidingMenuAdapter(context, mSlidingMenuModels));
    }


}
