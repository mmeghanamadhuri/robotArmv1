package com.example.roboticarm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.List;

public class FrameAdapter extends RecyclerView.Adapter<FrameAdapter.FrameViewHolder> {
    private List<Frame> frameList;
    private OnFrameActionListener listener;
    private int selectedPosition = -1;

    public interface OnFrameActionListener {
        void onFrameSelected(int position);
    }

    public FrameAdapter(List<Frame> frames, OnFrameActionListener listener) {
        this.frameList = frames;
        this.listener = listener;
    }

    public static class FrameViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public FrameViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }

    @NonNull
    @Override
    public FrameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_frame, parent, false);
        return new FrameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FrameViewHolder holder, int position) {
        Frame frame = frameList.get(position);
        holder.textView.setText("Item " + position);

        if (position == selectedPosition) {
            holder.textView.setTextColor(holder.textView.getContext().getResources().getColor(android.R.color.holo_blue_dark));
        } else {
            holder.textView.setTextColor(holder.textView.getContext().getResources().getColor(android.R.color.black));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                int previousSelected = selectedPosition;
                selectedPosition = position;
                notifyItemChanged(previousSelected);
                notifyItemChanged(selectedPosition);
                listener.onFrameSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return frameList.size();
    }

    public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(frameList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(frameList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        notifyDataSetChanged();
    }

    public List<Frame> getFrameList() {
        return frameList;
    }

    public void setSelectedPosition(int position) {
        int previous = selectedPosition;
        selectedPosition = position;
        if (previous != -1) notifyItemChanged(previous);
        if (position != -1) notifyItemChanged(position);
    }
} 