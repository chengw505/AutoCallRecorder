package com.chengw.autocallrecorder;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.TimeZone;


public class PlayRecordingFragment extends DialogFragment {
    private Handler mProgressHandler = null;
    private final int PROGRESS_CHANGED = 0;
    private Thread mMoniterProgressThreed;

    String mFullPathName;
    ImageButton mPauseButton;
    MediaPlayer mMediaPlayer = new MediaPlayer();
    private int mTotalTime;
    private int mCurrentTime;
    TextView tvCurrentTime;
    private SeekBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.play_recording, container);
        tvCurrentTime = (TextView)view.findViewById(R.id.player_current_time);
        mProgressBar = (SeekBar) view.findViewById(R.id.player_progress);
        mPauseButton = (ImageButton)view.findViewById(R.id.pause);

        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mPauseButton.setImageResource(R.drawable.play);
                } else {
                    mMediaPlayer.start();   // start or resume
                    mPauseButton.setImageResource(R.drawable.pause);
                }
            }
        });

        try {
            mMediaPlayer.setDataSource(mFullPathName);
            mMediaPlayer.prepare();

            mTotalTime = mMediaPlayer.getDuration();
            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("CST"));
            String strTotalTime = formatter.format(mTotalTime);

            ((TextView)view.findViewById(R.id.player_total_time)).setText(strTotalTime);

            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mTotalTime = mMediaPlayer.getDuration();
                mCurrentTime = mMediaPlayer.getCurrentPosition();

                initialProgressBar();
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                updateCurrentTime(0);
                mMediaPlayer.seekTo(0);
                mMediaPlayer.start();

                Message message = new Message();
                message.what = PROGRESS_CHANGED;
                mProgressHandler.sendMessage(message);
            }
        });

        mProgressHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case PROGRESS_CHANGED:
                        if (mMediaPlayer != null) {
                            mProgressBar.setProgress(mMediaPlayer.getCurrentPosition());
                        }
                        break;
                }
            }
        };

        /*
		 * update progress bar thread
		 */
        if(mMoniterProgressThreed == null) {
            mMoniterProgressThreed = new Thread() {

                public void run() {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            if (mMediaPlayer == null || !mMediaPlayer.isPlaying()) {
                                Thread.sleep(500);
                                continue;
                            }

                            Message message = new Message();
                            message.what = PROGRESS_CHANGED;
                            mProgressHandler.sendMessage(message);

                            if (mMediaPlayer.getCurrentPosition() == mMediaPlayer.getDuration()) {
                                //return;
                            }

                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            };

            mMoniterProgressThreed.start();
        }


        try {
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    public void setRecordingFileName(String fileName) {
        mFullPathName = fileName;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if(mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    private void initialProgressBar() {
        mProgressBar.setMax(mTotalTime);

        mProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {

            }

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {

                int i = mProgressBar.getProgress();

                updateCurrentTime(i);
            }

            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                int i = mProgressBar.getProgress();

                updateCurrentTime(i);
                mMediaPlayer.seekTo(i);
            }

        });
    }

    private boolean updateCurrentTime(int currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("CST"));

        if (tvCurrentTime == null) {
            return false;
        }

        String strCurrentTime = formatter.format(currentTime);
        tvCurrentTime.setText(strCurrentTime);
        return true;
    }
}
