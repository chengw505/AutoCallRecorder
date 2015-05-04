package com.chengw.autocallrecorder;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.chengw.autocallrecorder.adpater.CallHistoryAdapter;
import com.chengw.autocallrecorder.adpater.RecordingsDbAdapter;
import com.chengw.autocallrecorder.model.ItemCallHistoryModel;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;


public class MainActivity extends SlidingFragmentActivity {
    public final  static String TAG = "AutoRecorderTag";
    public final static String recordingDir = "MyCallRecordings";
    private static final int UPLOAD_LOCAL_RECORDING_DONE = 1;

    private LeftSlidingMenuFragment mSlidingMenuFrag;
    private ArrayList<ItemCallHistoryModel> mCallHistoryList;
    private SlidingMenu mSlidingMenu;
    private RecordingsDbAdapter mDB;
    private Handler mUploadLocalRecordingHandler;
    private PlayRecordingFragment mPlayRecordingFrag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDB = new RecordingsDbAdapter(this);
        mDB.open();

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
        mSlidingMenu = getSlidingMenu();

        // initialize sliding sm
        setBehindContentView(R.layout.menu_frame);
        if(null == savedInstanceState) {
            android.support.v4.app.FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            mSlidingMenuFrag = new LeftSlidingMenuFragment();
            mSlidingMenuFrag.setSlidingMenu(mSlidingMenu);
            t.replace(R.id.menu_frame, mSlidingMenuFrag);
            t.commit();
        } else {
            mSlidingMenuFrag = (LeftSlidingMenuFragment) this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
        }

        mSlidingMenu.setShadowDrawable(R.drawable.shadow);
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        mSlidingMenu.setFadeDegree(0.35f);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
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
        // TODO share with others
        Toast.makeText(getApplicationContext(), "TODO share with ...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private void initCallHistory(Bundle savedInstanceState) {

        mCallHistoryList = new ArrayList<>();
        mPlayRecordingFrag = new PlayRecordingFragment();

        ListView callHistoryListView = (ListView) findViewById(R.id.call_history_item);
        callHistoryListView.setAdapter(new CallHistoryAdapter(getApplicationContext(), mCallHistoryList));

        callHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String recordFileName = mCallHistoryList.get(position).getFileName();

                String fullPathName = Environment.getExternalStorageDirectory() + "/" + recordingDir + "/" + recordFileName;
                mPlayRecordingFrag.setRecordingFileName(fullPathName);
                mPlayRecordingFrag.show(getFragmentManager(), "Play Recordings");
            }
        });

        callHistoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

        mUploadLocalRecordingHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPLOAD_LOCAL_RECORDING_DONE:
                        String fileName = mDB.getNextLocalRecording();
                        if (fileName != null) {
                            uploadRecording(fileName);
                        }
                        break;
                }
            }
        };

        new Thread() {
            @Override
            public void run() {
                File[] files = new File(Environment.getExternalStorageDirectory(), recordingDir).listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        return filename.toLowerCase().endsWith(".amr");
                    }
                });

                if(null == files)   return;

                for(int i = 0; i < files.length; ++i) {
                    Log.d(TAG, files[i].getName());

                    String fileName = files[i].getName();

                    // if not exist in database, create a row
                    if(mDB.getRecordingItem(fileName) == null) {
                        mDB.createRecordingItem(fileName, 0, "");
                    }

                    ItemCallHistoryModel record = new ItemCallHistoryModel(fileName);
                    if(record.legalRecord()) {
                        mCallHistoryList.add(record);
                    }
                }

                if(isWiFi()) {
                    String fileName = mDB.getFirstLocalRecording();
                    if(fileName != null) {
                        // upload file into cloud
                        uploadRecording(fileName);
                    }
                } else {
                    // TODO start a monitor thread
                }
            }
        }.start();

     }

    private boolean uploadRecording(String fileName) {

        new Thread() {
            @Override
            public void run() {

                // TODO
                Message message = new Message();
                message.what=UPLOAD_LOCAL_RECORDING_DONE;
                mUploadLocalRecordingHandler.sendMessage(message);
            }

        }.start();

        return true;
    }

    /**
     * @return true if network is available and using WiFi
     */
    public boolean isWiFi() {
        ConnectivityManager connectivity = (ConnectivityManager) getBaseContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void initSystemOverview(Bundle savedInstanceState) {
        // TODO system overview
    }
}
