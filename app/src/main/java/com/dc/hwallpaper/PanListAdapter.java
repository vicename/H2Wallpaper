package com.dc.hwallpaper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

/**
 * Created by Li DaChang on 16/12/25.
 * ..-..---.-.--..---.-...-..-....-.
 */

public class PanListAdapter extends RecyclerView.Adapter<PanListAdapter.ItemViewHolder> {
    private List<Integer> colorList;
    private OnItemClickListener onItemClickListener;


    public PanListAdapter(List<Integer> colorList) {
        this.colorList = colorList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pan_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.viewPan.setBackgroundColor(colorList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(holder.itemView, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return colorList == null ? 0 : colorList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        View viewPan;

        public ItemViewHolder(View itemView) {
            super(itemView);
            viewPan = itemView.findViewById(R.id.view_color_pan);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onClick(View v, int position);
    }
}
