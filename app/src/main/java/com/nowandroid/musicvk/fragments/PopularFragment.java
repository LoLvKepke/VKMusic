package com.nowandroid.musicvk.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nowandroid.musicvk.R;
import com.nowandroid.musicvk.adapters.PopularAdapter;
import com.nowandroid.musicvk.data.PopularListItem;
import com.nowandroid.musicvk.events.SongEvent;
import com.nowandroid.musicvk.service.PlayerService;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import static android.support.v4.app.ActivityCompat.invalidateOptionsMenu;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopularFragment extends Fragment implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private RecyclerView               rv;
    private PopularAdapter             mAdapter;
    private ArrayList<PopularListItem> mItems;
    private MediaPlayer                mediaPlayer;
    private boolean isPlayingSong = false;

    public PopularFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_popular, container, false);
        setRetainInstance(true);

        rv = (RecyclerView) v.findViewById(R.id.listViewStarMusic);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);

        mItems = new ArrayList<>();
        mediaPlayer = new MediaPlayer();

        VKRequest popular = VKApi.audio().getPopular(VKParameters.from(VKApiConst.COUNT, 15));
        popular.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);

                VKList<VKApiAudio> list = (VKList) response.parsedModel;

                int i = 0;
                for (VKApiAudio audio : list) {
                    mItems.add(new PopularListItem(audio.title, audio.url, audio.artist, audio.duration, i++));
                }

                mAdapter = new PopularAdapter(mItems, new PopularAdapter.OnPopularClickItem() {
                    @Override
                    public void onPopularClick(PopularListItem item) {
                        //Toast.makeText(getActivity(), item.getUrl().substring(0, item.url.lastIndexOf("?")), Toast.LENGTH_SHORT).show();
                        //                        if(mediaPlayer != null){
                        //                            mediaPlayer.release();
                        //                        }
                        //                        mediaPlayer = new MediaPlayer();
                        //                        mediaPlayer =
                        //                                MediaPlayer.create(getActivity(), Uri.parse(item.getUrl().substring(0, item.url.lastIndexOf("?"))));
                        //                        mediaPlayer.start();
                        if (mediaPlayer.isPlaying()) {
                            isPlayingSong = false;
                        } else {
                            isPlayingSong = true;
                        }

                        invalidateOptionsMenu(getActivity());
                        Bundle b = new Bundle();
                        b.putSerializable("list", mItems);
                        Intent i = new Intent(getActivity(), PlayerService.class);
                        i.putExtras(b);
                        i.putExtra("id", item.getCurrentPosition());
                        i.putExtra("url", item.getUrl().substring(0, item.url.lastIndexOf("?")));
                        getActivity().startService(i);

                        EventBus.getDefault().post(new SongEvent(item.getCurrentPosition(), mItems.size(), item.getUrl().substring(0, item.url.lastIndexOf("?"))));
                    }
                });
                rv.setAdapter(mAdapter);

            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCompletion(MediaPlayer player) {
        player.start();
    }

    @Override
    public void onPrepared(MediaPlayer player) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (isPlayingSong) {
            menu.getItem(0).setVisible(true);
        } else {
            menu.getItem(0).setVisible(false);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.action_player == item.getItemId()) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new PlayerFragment()).commit();
        }
        return super.onOptionsItemSelected(item);
    }
}
