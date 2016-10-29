package com.nowandroid.musicvk.adapters;

import android.graphics.Color;
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
public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.PopularViewHolder> {

    private ArrayList<PopularListItem> mData;
    private OnPopularClickItem         listener;
    private int selected_position = -1;

    public PopularAdapter(ArrayList<PopularListItem> item, OnPopularClickItem listener) {
        super();
        this.mData = item;
        this.listener = listener;
    }

    @Override
    public PopularViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PopularViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popular_music, parent, false));
    }

    @Override
    public void onBindViewHolder(final PopularViewHolder holder, final int position) {
        final PopularListItem popGetPosItem = mData.get(position);
        if(selected_position == position){
            holder.itemView.setBackgroundColor(Color.parseColor("#3F51B5"));
            holder.title.setTextColor(Color.WHITE);
            holder.author.setTextColor(Color.WHITE);
            holder.time.setTextColor(Color.WHITE);
            holder.txtTime.setTextColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            holder.title.setTextColor(Color.parseColor("#394ba4"));
            holder.author.setTextColor(Color.parseColor("#c8394ba4"));
            holder.time.setTextColor(Color.parseColor("#FF818181"));
            holder.txtTime.setTextColor(Color.parseColor("#FF858585"));
        }
        holder.title.setText(popGetPosItem.getTitle());
        holder.author.setText(popGetPosItem.getAuthor());
        holder.time.setText(String.valueOf(popGetPosItem.getTime()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyItemChanged(selected_position);
                selected_position = position;
                notifyItemChanged(selected_position);
                listener.onPopularClick(popGetPosItem);
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

    public class PopularViewHolder extends RecyclerView.ViewHolder {
        TextView title, author, time, txtTime;

        public PopularViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            author = (TextView) itemView.findViewById(R.id.author);
            time = (TextView) itemView.findViewById(R.id.time);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
        }
    }
}
