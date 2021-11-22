package com.example.asjad.notificationalert;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment {

    private ListView mGroupsListView;
    private DatabaseHelper mDatabase;
    private View mMainView;

    private final List<GroupsModel> groups = new ArrayList<>();
    private ListviewGroupsAdapter adapter;


    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_groups, container, false);

        mGroupsListView = (ListView) mMainView.findViewById(R.id.main_groups_list_view);

        mDatabase  = new DatabaseHelper(getActivity());

        Cursor res = mDatabase.getAllGroups();

        groups.clear();

        if(res.getCount() == 0) {
            // show message
        }

        Integer group_id_index = res.getColumnIndex("GROUP_ID");
        Integer group_name_index = res.getColumnIndex("GROUP_NAME");

        while (res.moveToNext()) {

            String groupId = res.getString(group_id_index);
            String groupName = res.getString(group_name_index);
            Integer num = numberOfMembers(groupId);
            String groupMembers = String.valueOf(num);

            groups.add(new GroupsModel(groupId, groupName, groupMembers));

        }

        adapter = new ListviewGroupsAdapter(getActivity(), groups);
        mGroupsListView.setAdapter(adapter);

        registerForContextMenu(mGroupsListView);

        mGroupsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                GroupsModel model = groups.get(i);
                String group_id = model.getId();
                String group_name = model.getName();

                Intent groupActivityIntent = new Intent(getActivity(), GroupActivity.class);
                groupActivityIntent.putExtra("group_name", group_name);
                groupActivityIntent.putExtra("group_id", group_id);
                startActivity(groupActivityIntent);

            }
        });



        return mMainView;

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.main_groups_list_view) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.add(Menu.NONE, 1, 1, "Edit Group");
            menu.add(Menu.NONE, 2, 2, "Delete Group");
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()){
            case 1:

                GroupsModel modelEdit = groups.get(info.position);
                String group_id_edit = modelEdit.getId();
                String group_name_edit = modelEdit.getName();

                Intent goToEditMemberIntent = new Intent(getActivity(), GroupMemberEditActivity.class);
                goToEditMemberIntent.putExtra("group_id", group_id_edit);
                goToEditMemberIntent.putExtra("group_name", group_name_edit);
                startActivity(goToEditMemberIntent);

                break;
            case 2:

                GroupsModel model = groups.get(info.position);
                String group_id = model.getId();
                String group_name = model.getName();

                mDatabase.deleteGroup(group_id);
                groups.remove(info.position);

                adapter.notifyDataSetChanged();

                Toast.makeText(getActivity(),"Group '"+group_name+"' is deleted", Toast.LENGTH_SHORT).show();
                break;
        }



        return super.onContextItemSelected(item);
    }



    //----------function for count number of members in group--------

    public int numberOfMembers(String group_id) {
        int result = 0;
        Cursor cursor = null;
        try {
            cursor = mDatabase.countAllMember(group_id);
            cursor.moveToFirst();
            result = cursor.getInt(0);

            if (result < 0) {
                result = 0;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return result;
    }

}
