package com.chengw.autocallrecorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.chengw.autocallrecorder.adpater.SlidingMenuAdapter;
import com.chengw.autocallrecorder.model.ItemSlidingMenuModel;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

/**
 * Created by Cheng on 3/2/2015.
 */
public class LeftSlidingMenuFragment extends Fragment {
    private View mView;
    private ListView mSlidingMenuListView;
    private ArrayList<ItemSlidingMenuModel> mSlidingMenuModels;
    private Context context;
    private SlidingMenu slidingMenu;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(null == mView) {
            // create a new one
            mView = inflater.inflate(R.layout.sliding_menu, container, false);
            context = mView.getContext();

            initialMenu();
        }

        return mView;
    }

    private void initialMenu() {
        mSlidingMenuListView = (ListView)mView.findViewById(R.id.sliding_menu_item);

        mSlidingMenuModels = new ArrayList<ItemSlidingMenuModel>();

        // get sliding menu
        String[] arraysSlidingMenu = context.getResources().getStringArray(R.array.arrays_slidingmenu);

        for (int i = 0; i < arraysSlidingMenu.length; ++i) {
            mSlidingMenuModels.add(new ItemSlidingMenuModel(R.drawable.settings_blue, arraysSlidingMenu[i]));
        }

        // bind data
        mSlidingMenuListView.setAdapter(new SlidingMenuAdapter(context, mSlidingMenuModels));

        mSlidingMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        getSlidingMenu().toggle();
                        startActivity(new Intent(context, SettingsActivity.class));
                        break;

                    default:
                        getSlidingMenu().toggle();
                        Toast.makeText(view.getContext(), "TODO", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


    public void setSlidingMenu(SlidingMenu slidingMenu) {
        this.slidingMenu = slidingMenu;
    }

    public SlidingMenu getSlidingMenu() {
        return slidingMenu;
    }
}
