package com.example.asjad.notificationalert;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Asjad on 6/7/2018.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter{

    Drawable mDrawble;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;
            case 1:
                GroupsFragment groupsFragment = new GroupsFragment();
                return groupsFragment;
            case 2:
                ContactsFragment contactsFragment = new ContactsFragment();
                return contactsFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    /**
    public CharSequence getPageTitle(int position){

        switch (position){
            case 0:
                return "SETTINGS";
            case 1:
                return "GROUPS";
            case 2:
                return "CONTACTS";
            default:
                return null;
        }

    } **/
}
