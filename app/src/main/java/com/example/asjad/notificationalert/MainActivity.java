package com.example.asjad.notificationalert;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionpagerAdapter;
    private TabLayout mTabLayout;
    private FloatingActionButton mGroupFAB;
    private FloatingActionButton mContactFAB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Notification Alert");

        mViewPager = (ViewPager)findViewById(R.id.main_tabPager);

        mGroupFAB = (FloatingActionButton)findViewById(R.id.main_group_fab);
        mContactFAB = (FloatingActionButton)findViewById(R.id.main_contact_fab);
        mContactFAB.hide();

        mSectionpagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionpagerAdapter);
        mViewPager.setCurrentItem(1);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position){
                    case 0:
                        mGroupFAB.hide();
                        mContactFAB.hide();
                        break;
                    case 1:
                        mContactFAB.hide();
                        mGroupFAB.show();
                        break;
                    case 2:
                        mGroupFAB.hide();
                        mContactFAB.show();
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabLayout = (TabLayout)findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);


        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_menu_black_24dp);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_group_black_24dp);
        mTabLayout.getTabAt(2).setIcon(R.drawable.ic_contacts_black_24dp);

        mGroupFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createGroupIntent = new Intent(MainActivity.this, CreategroupActivity.class);
                startActivity(createGroupIntent);
            }
        });

        mContactFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createContactIntent = new Intent(MainActivity.this, NewContactActivity.class);
                startActivity(createContactIntent);
            }
        });



    }


    //-----------Menu------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);

         getMenuInflater().inflate(R.menu.main_menu, menu);

         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.main_settings_btn){
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        return true;
    }



}
