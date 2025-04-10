package com.stkj.common.player.exoplayer;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;

import com.stkj.common.R;
import com.stkj.common.ui.activity.ImmerseActivity;
import com.stkj.common.utils.ActivityUtils;
import com.stkj.common.utils.StatusBarUtils;


public class ExoPlayerActivity extends ImmerseActivity {

    public static final String INTENT_VIDEO_URI = "video_uri";
    public static final String INTENT_VIDEO_TITLE = "video_title";

    private TextView tvTitle;
    private ExoPlayerView mExoPlayerView;
    private ExoPlayerControlView mPlayControlView;
    private ExoPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.black));
        StatusBarUtils.setSystemBarMode(this, false);
        setContentView(R.layout.activity_exo_player);
        initViews();
        getDataFromIntent();
    }

    private void initViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        View titleView = findViewById(R.id.ll_title);
        mExoPlayerView = findViewById(R.id.pv_player);
        mPlayControlView = findViewById(R.id.pcv_player);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mPlayControlView.setPlayControlListener(new ExoPlayerControlView.PlayerControlListener() {
            @Override
            public void onVisibilityChange(int visibility, boolean force) {
                if (visibility == VISIBLE) {
                    titleView.setVisibility(VISIBLE);
                } else {
                    if (mPlayControlView.isFullScreen() || force) {
                        titleView.setVisibility(GONE);
                    }
                }
            }

            @Override
            public void onLongClick() {

            }
        });
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra(INTENT_VIDEO_TITLE);
            if (!TextUtils.isEmpty(title)){
                tvTitle.setText(title);
            }
            String mVideoUri = intent.getStringExtra(INTENT_VIDEO_URI);
            initializePlayer(mVideoUri);
        }
    }

    private void initializePlayer(String videoUri) {
        if (mPlayer == null) {
            mPlayer = new ExoPlayer.Builder(this).build();
            mExoPlayerView.setPlayer(mPlayer);
            mPlayControlView.setPlayer(mPlayer);
            MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(videoUri)
                    .build();
            mPlayer.setMediaItem(mediaItem);
            mPlayer.prepare();
            mPlayer.play();
        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!isInPictureInPictureMode()) {
                if (mPlayer != null) {
                    mPlayer.setPlayWhenReady(false);
                }
            }
        } else {
            if (mPlayer != null) {
                mPlayer.setPlayWhenReady(false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onBackPressed() {
        if (mPlayControlView.isFullScreen()) {
            ActivityUtils.setScreenOrientation(this, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, true);
            return;
        }
        super.onBackPressed();
    }

}
