package com.example.techhub;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private static final int CHAT_END = 1;
    private static final int CHAT_START = 2;

    private List<Message> mDataSet;
    private String mId;
    private String userName ;

    /**
     * Called when a view has been clicked.
     *
     * @param dataSet Message list
     * @param id      Device id
     */
    ChatAdapter(List<Message> dataSet, String id) {
        mDataSet = dataSet;
        mId = id;
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == CHAT_END) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat_end, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat_start, parent, false);
        }

        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
          if(mDataSet.get(position).getId().equals(mId)){
                return CHAT_END;
        }
        else
        return CHAT_START;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = mDataSet.get(position);
        holder.mTextView.setText(message.getText());
        holder.userName.setText(message.getUname());
        holder.time1.setText(message.getTime());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /**
     * Inner Class for a recycler view
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView , userName, time1;

        ViewHolder(View v) {
            super(v);
            mTextView = (TextView) itemView.findViewById(R.id.tvMessage);
            userName = (TextView) itemView.findViewById(R.id.unm);
            time1 = (TextView) itemView.findViewById(R.id.time);

        }
    }
}

