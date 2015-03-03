package com.chengw.autocallrecorder;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.chengw.autocallrecorder.adpater.CallHistoryAdapter;
import com.chengw.autocallrecorder.model.ItemCallHistoryModel;
import com.chengw.autocallrecorder.model.ItemSlidingMenuModel;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.util.ArrayList;


public class MainActivity extends SlidingFragmentActivity {

    private LeftSlidingMenuFragment mSlidingMenuFrag;
    private ArrayList<ItemCallHistoryModel> mCallHistoryModels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize sliding menu
        initSlidingMenu(savedInstanceState);

        // initialize system overview
        initSystemOverview(savedInstanceState);

        // initialize call history
        initCallHistory(savedInstanceState);

        // set flipper listener
        ImageButton flipper = (ImageButton) findViewById(R.id.main_flipper);
        flipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });

        // set share listener
        ImageButton share = (ImageButton)findViewById(R.id.main_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShareWith();
            }
        });
    }

    private void initSlidingMenu(Bundle savedInstanceState) {
        // initialize sliding sm
        setBehindContentView(R.layout.menu_frame);
        if(null == savedInstanceState) {
            android.support.v4.app.FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            mSlidingMenuFrag = new LeftSlidingMenuFragment();
            t.replace(R.id.menu_frame, mSlidingMenuFrag);
            t.commit();
        } else {
            mSlidingMenuFrag = (LeftSlidingMenuFragment) this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
        }

        SlidingMenu sm = getSlidingMenu();
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setMode(SlidingMenu.LEFT);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.main_flipper) {
            toggle();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.main_share) {
            onShareWith();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onShareWith() {
        // TODO
        Toast.makeText(getApplicationContext(), "TODO share with ...", Toast.LENGTH_SHORT).show();
    }

    private void initCallHistory(Bundle savedInstanceState) {
        // TODO

        mCallHistoryModels = new ArrayList<ItemCallHistoryModel>();

        String name = "Name_";
        String phone_num = "(505)123-4567";
        for(int i = 1; i < 20; ++i) {
            mCallHistoryModels.add(new ItemCallHistoryModel(phone_num, name + 1, i % 2 == 0, false));
        }

        ListView lvCallHistory = (ListView) findViewById(R.id.call_history_item);
        lvCallHistory.setAdapter(new CallHistoryAdapter(getApplicationContext(), mCallHistoryModels));
    }

    private void initSystemOverview(Bundle savedInstanceState) {
        // TODO
    }
}
