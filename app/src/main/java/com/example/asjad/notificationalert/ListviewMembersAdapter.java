package com.example.asjad.notificationalert;



import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Asjad on 5/23/2018.
 */

public class ListviewMembersAdapter extends BaseAdapter {

    Activity activity;
    List<MemberModel> members;
    LayoutInflater inflater;

    public ListviewMembersAdapter(Activity activity, List<MemberModel> members) {
        this.activity = activity;
        this.members = members;
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int i) {
        return members.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if (view == null){

            view = inflater.inflate(R.layout.list_view_group_members_item, viewGroup, false);

            holder = new ViewHolder();

            holder.member_name = (TextView)view.findViewById(R.id.list_view_group_member_name);

            view.setTag(holder);
        }else
            holder = (ViewHolder)view.getTag();

        MemberModel model = members.get(i);

        holder.member_name.setText(model.getName());
        return view;
    }

    class ViewHolder{
        TextView member_name;
        TextView member_number;
    }

}
