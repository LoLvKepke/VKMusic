package com.nowandroid.musicvk.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nowandroid.musicvk.R;
import com.nowandroid.musicvk.data.PopularListItem;

import java.util.ArrayList;

/**
 * Created by nowandroid on 23.06.16.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private ArrayList<PopularListItem> mData;
    private OnPopularClickItem         listener;

    public SearchAdapter(ArrayList<PopularListItem> item, OnPopularClickItem listener) {
        super();
        this.mData = item;
        this.listener = listener;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popular_music, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, final int position) {
        final PopularListItem popularListItem = mData.get(position);
        holder.title.setText(popularListItem.getTitle());
        holder.author.setText(popularListItem.getAuthor());
        holder.time.setText(String.valueOf(popularListItem.getTime()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPopularClick(popularListItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnPopularClickItem {
        void onPopularClick(PopularListItem item);
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView title, author, time;

        public SearchViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            author = (TextView) itemView.findViewById(R.id.author);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }
}