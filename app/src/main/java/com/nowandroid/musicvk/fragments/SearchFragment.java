package com.nowandroid.musicvk.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nowandroid.musicvk.R;
import com.nowandroid.musicvk.adapters.SearchAdapter;
import com.nowandroid.musicvk.data.PopularListItem;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private Button                     btnSearch;
    private EditText                   editTextSearch;
    private RecyclerView               rv;
    private SearchAdapter              mAdapter;
    private ArrayList<PopularListItem> mItems;
    private VKList<VKApiAudio>         mList;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        btnSearch = (Button) v.findViewById(R.id.btnSearch);
        editTextSearch = (EditText) v.findViewById(R.id.editTextSearch);
        rv = (RecyclerView) v.findViewById(R.id.searchLVMusic);

        mItems = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = editTextSearch.getText().toString();
                final VKRequest request =
                        VKApi.audio().search(VKParameters.from(VKApiConst.Q, str, VKApiConst.COUNT, 100));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);

                        if(mItems.size() >= 0){
                            mItems.clear();
                        }

                        mList = (VKList) response.parsedModel;
                        int i = 0;
                        for (VKApiAudio audio : mList) {
                            mItems.add(new PopularListItem(audio.title, audio.url, audio.artist, audio.duration, i++));
                        }
                        mAdapter =
                                new SearchAdapter(mItems, new SearchAdapter.OnPopularClickItem() {
                                    @Override
                                    public void onPopularClick(PopularListItem item) {
                                        Toast.makeText(getActivity(), item.getUrl().substring(0, item.url.lastIndexOf("?")), Toast.LENGTH_SHORT).show();
                                    }
                                });
                        rv.setAdapter(mAdapter);

                    }
                });
            }
        });

        return v;
    }

}
