package com.nowandroid.musicvk.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nowandroid.musicvk.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayListOnlineFragment extends Fragment {

    public PlayListOnlineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play_list_online, container, false);
    }

}
