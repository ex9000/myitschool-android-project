package com.sr9000.gdx.x3p1.impl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sr9000.gdx.x3p1.R;
import com.sr9000.gdx.x3p1.business.AppBusiness;
import com.sr9000.gdx.x3p1.business.part.struct.Record;

import java.time.format.DateTimeFormatter;

public class RecordRecyclerViewAdapter extends RecyclerView.Adapter<RecordRecyclerViewAdapter.ViewHolder> {

    private LayoutInflater mInflater;
//    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public RecordRecyclerViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.rv_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        synchronized (AppBusiness.INSTANCE) {
            Record r = AppBusiness.INSTANCE.getSimulation().get_by_position(position);
            holder.textView.setText(format_record(r, position));
        }
    }

    private String format_record(Record r, int rating) {
//        String sdt = r.datetime.format(DateTimeFormatter.ISO_DATE_TIME);
        String sdt = r.datetime.format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS  (E) dd-MM-yyyy"));
        return String.format("%d. By %s: %,d (%d hops) \n\t%s", rating + 1, r.author, r.number, r.hops, sdt);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        synchronized (AppBusiness.INSTANCE) {
            return AppBusiness.INSTANCE.getSimulation().get_size();
        }
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {//implements View.OnClickListener {
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.record_text);
//            itemView.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View view) {
//            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
//        }
    }

//    // convenience method for getting data at click position
//    String getItem(int id) {
//        return mData.get(id);
//    }
//
//    // allows clicks events to be caught
//    void setClickListener(ItemClickListener itemClickListener) {
//        this.mClickListener = itemClickListener;
//    }
//
//    // parent activity will implement this method to respond to click events
//    public interface ItemClickListener {
//        void onItemClick(View view, int position);
//    }
}