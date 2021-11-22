package com.example.asjad.notificationalert;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Asjad on 6/8/2018.
 */

public class ContactsRecyclerViewAdapter  extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.ViewHolder>  {

    private static final String TAG = "ContactsRecyclerViewAda";

    private Context mContext;

    private ArrayList<String> mContactName = new ArrayList<>();

    public ContactsRecyclerViewAdapter(Context mContext, ArrayList<String> mContactName) {
        this.mContext = mContext;
        this.mContactName = mContactName;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_single_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: called. ");

        holder.contactName.setText(mContactName.get(position));

    }

    @Override
    public int getItemCount() {
        return mContactName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView contactName;
        RelativeLayout contactSingleLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contacts_list_name);
            contactSingleLayout = itemView.findViewById(R.id.contact_single_layout);

        }
    }
}
