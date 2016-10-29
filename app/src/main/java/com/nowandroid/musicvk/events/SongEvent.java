package com.nowandroid.musicvk.events;

/**
 * Created by nowandroid on 25.06.16.
 */

public class SongEvent {
    public int    currentTrack;
    public int    listSize;
    public String mUrl;

    public SongEvent(int currentTrack, int listSize, String mUrl) {
        this.currentTrack = currentTrack;
        this.listSize = listSize;
        this.mUrl = mUrl;
    }
}
