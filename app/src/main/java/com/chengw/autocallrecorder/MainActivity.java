package com.chengw.autocallrecorder;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


public class MainActivity extends SlidingFragmentActivity {

    private LeftSlidingMenuFragment mSlidingMenuFrag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSlidingMenu(savedInstanceState);

        // set flipper listener
        ImageButton flipper = (ImageButton) findViewById(R.id.main_flipper);
        flipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
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
        // Inflate the menu; this adds items to the action bar if it is present.
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
