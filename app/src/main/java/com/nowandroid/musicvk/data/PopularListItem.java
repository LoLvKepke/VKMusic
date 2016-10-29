package com.nowandroid.musicvk.data;

import java.io.Serializable;

/**
 * Created by nowandroid on 23.06.16.
 */
public class PopularListItem implements Serializable {

    public  String title;
    public  String url;
    public  int    currentPosition;
    private String mAuthor;
    private int    mTime;
    private String mString;

    public PopularListItem() {
    }

    public PopularListItem(String title, String url, String mAuthor, int mTime, int currentPosition) {
        this.title = title;
        this.url = url;
        this.mAuthor = mAuthor;
        this.mTime = mTime;
        this.currentPosition = currentPosition;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getTime() {
//        Long hours   = Long.valueOf(mTime / 3600);
        Long minutes = Long.valueOf(((mTime % 3600) / 60));
        Long seconds = Long.valueOf(mTime % 60);
        if (seconds < 10) {
            mString = String.valueOf("0" + minutes + ":0" + seconds);
        } else {
            mString = String.valueOf("0" + minutes + ":" + seconds);
        }

        return mString;
    }
}
