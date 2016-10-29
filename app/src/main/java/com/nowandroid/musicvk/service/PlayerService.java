package com.nowandroid.musicvk.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.nowandroid.musicvk.data.PopularListItem;
import com.nowandroid.musicvk.events.SongEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by nowandroid on 23.06.16.
 */

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    MediaPlayer                mMediaPlayer;
    ArrayList<PopularListItem> mItems;
    private String mUrl;
    private int    mAudioListSize;
    private int    currentTrack;
    private int    mId;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //        if (mMediaPlayer == null)
        //            mMediaPlayer = new MediaPlayer();
        //        return new Binder();
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //        if (mMediaPlayer == null)
        //            mMediaPlayer = new MediaPlayer();
    }

    public void onDestroy() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
    }

    @Subscribe
    public void onEvent(SongEvent event) {
        currentTrack = event.currentTrack;
        //mUrl = event.mUrl;
        mAudioListSize = event.listSize;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        try{
            mMediaPlayer = new MediaPlayer();
            mId = intent.getIntExtra("id", 0);
            mUrl = intent.getStringExtra("url");
            mMediaPlayer =
                    MediaPlayer.create(this, Uri.parse(mUrl));
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.start();
        } catch (Exception e){
            Toast.makeText(this, "ВК нам не дали эту песню :( "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // TODO: 25.06.16 передать ArrayList с песнями (id, url);

        mItems = (ArrayList<PopularListItem>) intent.getSerializableExtra("list");

        for (int i = 0; i < mItems.size(); i++) {
            System.out.println("Num: " + i + "; Song: " + mItems.get(i).getUrl());
        }

        return START_STICKY_COMPATIBILITY;

    }

    @Override
    public void onPrepared(MediaPlayer player) {
        player.start();
    }

    @Override
    public boolean onError(MediaPlayer player, int i, int i1) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer player) {
        if (mId <= mItems.size()) {
            playSong(mId + 1);
            mId = mId + 1;
        }

    }

    private void playSong(int position) {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer =
                MediaPlayer.create(this, Uri.parse(mItems.get(position).getUrl()));
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.start();
    }

}
