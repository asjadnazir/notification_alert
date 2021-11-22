package com.example.asjad.notificationalert;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Asjad on 5/4/2018.
 */

public class ListviewGroupsAdapter extends BaseAdapter {
    Activity activity;
    List<GroupsModel> groups;
    LayoutInflater inflater;

    public ListviewGroupsAdapter(Activity activity){
        this.activity = activity;
    }

    public ListviewGroupsAdapter(Activity activity, List<GroupsModel> groups){
        this.activity = activity;
        this.groups = groups;

        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return  groups.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if (view == null){

            view = inflater.inflate(R.layout.group_single_layout, viewGroup, false);

            holder = new ViewHolder();

            holder.group_name = (TextView)view.findViewById(R.id.group_single_name);
            holder.group_members = (TextView)view.findViewById(R.id.group_single_totel_members);

            view.setTag(holder);
        }else
            holder = (ViewHolder)view.getTag();

        GroupsModel model = groups.get(i);

        holder.group_name.setText(model.getName());
        holder.group_members.setText(model.getMembers()+" Members");

        return view;
    }

    public void updateRecords(List<GroupsModel> groups){
        this.groups = groups;

        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView group_name;
        TextView group_members;
    }

}
