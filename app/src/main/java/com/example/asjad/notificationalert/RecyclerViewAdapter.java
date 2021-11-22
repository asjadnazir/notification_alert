package com.example.asjad.notificationalert;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Asjad on 6/1/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private Context mContext;
    private DatabaseHelper mDatabase;
    private ArrayList<String> mMessageId = new ArrayList<>();
    private ArrayList<String> mMessageText = new ArrayList<>();
    private ArrayList<String> mMessageTime = new ArrayList<>();
    private ArrayList<String> mMessageStatus = new ArrayList<>();

    public RecyclerViewAdapter(Context mContext,ArrayList<String> mMessageId, ArrayList<String> mMessageText, ArrayList<String> mMessageTime, ArrayList<String> mMessageStatus ) {
        this.mContext = mContext;
        this.mMessageId = mMessageId;
        this.mMessageText = mMessageText;
        this.mMessageTime = mMessageTime;
        this.mMessageStatus = mMessageStatus;

        mDatabase = new DatabaseHelper(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Log.d(TAG, "onBindViewHolder: called");

        holder.messageText.setText(mMessageText.get(position));
        holder.messageTime.setText(mMessageTime.get(position));
        if ((mMessageStatus.get(position)).equals("true")){
            holder.statusImage.setImageResource(R.drawable.sent);
        }else {
            holder.statusImage.setImageResource(R.drawable.alert);
        }

        holder.messageSingleLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                //creating a popup menu
                PopupMenu popup = new PopupMenu(mContext, holder.messageSingleLayout);
                //inflating menu from xml resource
                popup.inflate(R.menu.message_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.message_delete_item:
                                //handle menu1 click


                                mDatabase.deleteMessage(mMessageId.get(position));

                                mMessageId.remove(position);
                                mMessageText.remove(position);
                                mMessageTime.remove(position);
                                mMessageStatus.remove(position);

                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,mMessageId.size());


                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

                return false;
            }
        });
        
    }

    @Override
    public int getItemCount() {
        return mMessageText.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView messageTime;
        ImageView statusImage;
        RelativeLayout messageSingleLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.message_text_layout);
            messageTime = itemView.findViewById(R.id.message_time_layout);
            statusImage = itemView.findViewById(R.id.message_sent_layout);
            messageSingleLayout = itemView.findViewById(R.id.message_single_layout);

        }
    }


}
