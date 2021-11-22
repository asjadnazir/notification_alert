package com.example.asjad.notificationalert;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Asjad on 4/21/2018.
 */

public class ListviewAdapter extends BaseAdapter {
    Activity activity;
    List<ContactsModel> contacts;
    ArrayList<ContactsModel> arrayList;
    LayoutInflater inflater;

    public ListviewAdapter(Activity activity){
        this.activity = activity;
    }

    public ListviewAdapter(Activity activity, List<ContactsModel> contacts){
        this.activity = activity;
        this.contacts = contacts;

        inflater = activity.getLayoutInflater();

        this.arrayList = new ArrayList<ContactsModel>();
        this.arrayList.addAll(contacts);

    }

    @Override
    public int getCount() {
        return contacts.size();
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

            view = inflater.inflate(R.layout.list_view_contacts_item, viewGroup, false);

            holder = new ViewHolder();

            holder.contact_name = (TextView)view.findViewById(R.id.list_view_contact_name);
            holder.contact_number = (TextView)view.findViewById(R.id.list_view_contact_number);
            holder.contact_check_box = (ImageView) view.findViewById(R.id.list_view_check_box);

            view.setTag(holder);
        }else
            holder = (ViewHolder)view.getTag();

        ContactsModel model = contacts.get(i);

        holder.contact_name.setText(model.getName());
        holder.contact_number.setText(model.getNumber());

        if (model.isSelected())
            holder.contact_check_box.setBackgroundResource(R.drawable.checkbox_checked);

        else
            holder.contact_check_box.setBackgroundResource(R.drawable.checkbox_unchecked);

        return view;
    }

    public void fiter (String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        contacts.clear();
        if (charText.length() == 0){
            contacts.addAll(arrayList);
        }else{
            for (ContactsModel model : arrayList){
                if (model.getName().toLowerCase(Locale.getDefault()).contains(charText)){
                    contacts.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void updateRecords(List<ContactsModel> contacts){
        this.contacts = contacts;

        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView contact_id;
        TextView contact_name;
        TextView contact_number;
        ImageView contact_check_box;
    }

}
