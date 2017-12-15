package com.chalansoftware.veldopname;

import android.graphics.Canvas;
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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import com.chalansoftware.veldopname.database.PointDbSchema;

import java.util.List;

public class MainActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DialogConfirmDelete.DeleteDialogListener {
    
    private static final String SHOW_CONFIRM_DELETE_DIALOG_TAG = "show_confirm_delete";
    List<Point> mPointsList;
    RecyclerView.Adapter<PointRecyclerAdapter.PointViewHolder> mAdapter;
    
    public static final String TAG = "veld";
    public static final String SHOW_ADD_DIALOG_TAG = "show_dialog";
    public static final String PERCENT_DIALOG_TAG = "percent_dialog";
    
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d(TAG, "MainActivity.onCreate");
        initializeViewItems();
        // Initializing the List of Points
        mPointsList = PointLab.getInstance(this).getPointsList();
    
        RecyclerView recyclerView = getRecyclerView();
        setDeleteBySwipe(recyclerView);
        PointLab.getInstance(getApplication()).createTable(PointDbSchema.PointTable.TABLE_NAME);
    }
    @NonNull private RecyclerView getRecyclerView() {
        // Configures MainActivity's recyclerview.
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if (mAdapter == null) {
            mAdapter = new PointRecyclerAdapter(mPointsList);
            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        return recyclerView;
    }
    private void setDeleteBySwipe(RecyclerView recyclerView) {
        // Deletes an item from the recyclerview, pointsList and database by swiping.
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
                                                                                           ItemTouchHelper.RIGHT
                                                                                                   | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }
            @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();// which position was swiped.
                showDeleteDialog(position);
            }
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
/*                final ColorDrawable background = new ColorDrawable(Color.LTGRAY);
                background.setBounds(0,
                                     viewHolder.itemView.getTop(),
                                     (int) (viewHolder.itemView.getLeft() + dX),
                                     viewHolder.itemView.getBottom());
                background.draw(c);*/
                super.onChildDraw(c,
                                  recyclerView,
                                  viewHolder,
                                  dX,
                                  dY,
                                  actionState,
                                  isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    private void showDeleteDialog(int position) {
        DialogConfirmDelete dialogConfirmDelete = DialogConfirmDelete.newInstance(position);
        dialogConfirmDelete.setCancelable(false);// dialog cannot be dismissed by tapping outside.
        dialogConfirmDelete.show(getSupportFragmentManager(), SHOW_CONFIRM_DELETE_DIALOG_TAG);
    }
    @Override public void onDeleteDialogPositiveClick(DialogFragment dialogFragment, int position) {
        deletePoint(position);
    }
    @Override public void onDeleteDialogNegativeClick(DialogFragment dialogFragment) {
        mAdapter.notifyDataSetChanged();
    }
    private void deletePoint(int position) {
        PointLab.getInstance(getApplication()).removePoint(mPointsList.get(position));
        mAdapter.notifyItemRemoved(position);
        mPointsList.remove(position);
    }
    private void showAddDialog() {
        // Shows a new dialog to add new points to mPointsList and database.
        DialogFragment addDialog = DialogAdd.newInstance(mPointsList);
        addDialog.show(getSupportFragmentManager(), SHOW_ADD_DIALOG_TAG);
    }
    private void showPercentage() {
        // Displays a new dialog showing the percentage.
        DialogFragment showPercentDialog = DialogShowPercent.newInstance(mPointsList);
        showPercentDialog.show(getSupportFragmentManager(), PERCENT_DIALOG_TAG);
    }
    private void calculatePercentage() {
        // Calculates the percentage for each point object for display in the percentage dialog
        // and for saving in the database.
        double total = 0;
        for (int i = 0; i < mPointsList.size(); i++) {
            total = total + mPointsList.get(i).getPointCount();
        }
        double countValue;
        double percentage;
        if (total > 0) {//do not divide by zero
            for (int i = 0; i < mPointsList.size(); i++) {
                countValue = mPointsList.get(i).getPointCount();
                percentage = (countValue / total) * 100;
                mPointsList.get(i).setPercentage(percentage);
            }
        }
    }
    @Override protected void onPause() {
        super.onPause();
        // Writes all changes to mPointsList to the database.
        for (Point p : mPointsList) {
            PointLab.getInstance(getApplication()).updatePoint(p);
        }
    }
    private void initializeViewItems() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                                                                 drawer,
                                                                 toolbar,
                                                                 R.string.navigation_drawer_open,
                                                                 R.string.navigation_drawer_close);
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
        //if (id == R.id.action_settings) {
        //    return true;
        //}
        if (id == R.id.bereken_persentasie) {
            calculatePercentage();
            showPercentage();
            return true;
        }
        if (id == R.id.add_point) {
            showAddDialog();
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
}
