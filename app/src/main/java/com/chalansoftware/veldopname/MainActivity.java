package com.chalansoftware.veldopname;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    
    ArrayList<Point> mPointsList = new ArrayList<>();
    
    public static final String TAG = "veld";
    
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d(TAG, "MainActivity.onCreate");
        initViews();
        
        if (savedInstanceState != null) {
            mPointsList = savedInstanceState.getParcelableArrayList("Points");
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter<PointRecyclerAdapter.PointViewHolder> adapter;
        
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PointRecyclerAdapter(mPointsList);
        recyclerView.setAdapter(adapter);
        
        //Point point = ViewModelProviders.of(this).get(Point.class);
    }
    private void showDialog() {
        // Shows a new dialog to add new points to mPointsList
        DialogFragment addDialog = DialogAdd.newInstance(mPointsList);
        addDialog.show(getSupportFragmentManager(), "dialog");
    }
    private void showPercentage(){
        DialogFragment showPercentDialog = DialogShowPercent.newInstance(mPointsList);
        showPercentDialog.show(getSupportFragmentManager(), "percent_dialog");
    }
    private void calculatePercentage(){
        double total = 0;
        for (int i = 0; i < mPointsList.size(); i++) {
            total = total + mPointsList.get(i).getPointCount();
        }
        double countValue;
        double percentage;
        if (total > 0){//do not divide by zero
            for (int i = 0; i < mPointsList.size(); i++) {
                countValue = mPointsList.get(i).getPointCount();
                percentage = (countValue / total) * 100;
                mPointsList.get(i).setPercentage(percentage);
            }
        }
    }
    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("Points", mPointsList);
    }
    
    private void initViews() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);// TODO: 2017/11/13 Verander
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);// TODO: 2017/11/13 Verander
        
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    
    @Override public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.bereken_persentasie) {
            calculatePercentage();
            showPercentage();
            return true;
        }
        if (id == R.id.add_point) {
            showDialog();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody") @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
        
        } else if (id == R.id.nav_slideshow) {
        
        } else if (id == R.id.nav_manage) {
        
        } else if (id == R.id.nav_share) {
        
        } else if (id == R.id.nav_send) {
        
        }
        
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override protected void onPause() {
        super.onPause();
        //Log.d(TAG, "MainActivity.onPause");
    }
    @Override protected void onDestroy() {
        super.onDestroy();
        //Log.d(TAG, "MainActivity.onDestroy");
    }
    @Override protected void onStop() {
        super.onStop();
        //Log.d(TAG, "MainActivity.onStop");
    }
    @Override protected void onStart() {
        super.onStart();
        //Log.d(TAG, "MainActivity.onStart");
    }
    @Override protected void onResume() {
        super.onResume();
        //Log.d(TAG, "MainActivity.onResume");
    }
    @Override protected void onRestart() {
        super.onRestart();
        //Log.d(TAG, "MainActivity.onRestart");
    }
}
