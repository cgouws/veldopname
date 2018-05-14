package com.chalansoftware.veldopname;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chalansoftware.dbtocsvhandler.CSVWriter;
import com.chalansoftware.veldopname.database.PointDatabaseHelper;
import com.chalansoftware.veldopname.database.PointDbSchema;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DialogConfirmDelete.OnDeleteDialogListener,
        DialogAdd.OnAddDialogDismissedListener,
        DialogSelectSaveName.OnSelectSaveNameDialogDismissedListener,
        DialogConfirmReset.OnResetDialogListener {
    
    public static final String TAG_PREFS = "settings";
    public static final String KEY_BUTTONS = "keybuttonsound";
    public static final String KEY_DISTANCE = "keydistancesound";
    public static final String TAG_LOG = "log";
    public static final String SHOW_ADD_DIALOG_TAG = "show_dialog";
    public static final String PERCENT_DIALOG_TAG = "percent_dialog";
    public static final String SELECT_FILENAME_DIALOG_TAG = "select_filename";
    public static final int CREATE_REQUEST_CODE = 41;
    public static final int SAVE_REQUEST_CODE = 42;
    public static final int WRITE_REQUEST_CODE = 101;
    private static final String SHOW_CONFIRM_DELETE_DIALOG_TAG = "show_confirm_delete";
    private static final String SHOW_CONFIRM_RESET_DIALOG_TAG = "show_confirm_reset";
    List<Point> mPointsList;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter<PointRecyclerAdapter.PointViewHolder> mAdapter;
    SoundPool mSoundPool;
    int mDistSound = -1;
    int mButtonSound = -1;
    int mIdFX3 = -1;
    int mNowPlaying = -1;
    float mVolume = .1f;
    private boolean mIsDistSound;
    private boolean mIsButtonSound;
    private double mTotals;
    TextView mTotalsTextView;
    
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewItems();
        buildPointsList();
        setRecyclerView();
        setDeleteBySwipe(mRecyclerView);
        initSound();
        updateTotals();
    }
    void resetPointCount() {
        // Reset the point count to zero for each point in mPointsList. Also updates the
        // database, resets the recyclerview and totals.
        for (Point p : mPointsList) {
            p.setPointCount(0d);
        }
        updateDatabase();
        setRecyclerView();
        updateTotals();
    }
    private void updateDatabase() {
        for (Point p : mPointsList) {
            PointLab.getInstance(getApplication()).updatePoint(p);
        }
    }
    @NonNull private void setRecyclerView() {
        // Configures MainActivity's recyclerview.
        mRecyclerView = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        if (mAdapter == null) {
            mAdapter = new PointRecyclerAdapter();
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }
    private void buildPointsList() {
        // Initializing the List of Points
        mPointsList = PointLab.getInstance(this).getPointsList();
    }
    private void updateTotals() {
        // mTotals is a marker for the distance sound. Used in the recyclerview.
        mTotals = 0d;
        for (int i = 0; i < mPointsList.size(); i++) {
            mTotals = mTotals + mPointsList.get(i).getPointCount();
        }
        mTotalsTextView.setText(String.valueOf(mTotals));
    }
    private void initSound() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(
                AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        mSoundPool = new SoundPool.Builder().setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build();
        // Load each sound FX and initialize id's
        try {
            AssetManager assetManager = this.getAssets();
            AssetFileDescriptor descriptor;
            // Load FX into memory
            descriptor = assetManager.openFd("afstandklank.ogg");
            mDistSound = mSoundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("knoppieklank.ogg");
            mButtonSound = mSoundPool.load(descriptor, 0);
            Log.i(TAG_LOG, "initSound: success");
        } catch (IOException ioe) {
            Log.e(TAG_LOG, "initSound: failed to load sound files");
        }
    }
    @Override protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences(TAG_PREFS, MODE_PRIVATE);
        mIsButtonSound = prefs.getBoolean(KEY_BUTTONS, true);
        mIsDistSound = prefs.getBoolean(KEY_DISTANCE, true);
    }
    private void setDeleteBySwipe(RecyclerView recyclerView) {
        // Deletes an item from the recyclerview, pointsList and database by swiping.
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
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
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState,
                        isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    private void showDeleteDialog(int position) {
        // Called by setDeleteBySwipe's ItemTouchHelper.SimpleCallback's onSwiped().
        DialogConfirmDelete dialogConfirmDelete = DialogConfirmDelete.newInstance(position);
        dialogConfirmDelete.setCancelable(false);// dialog cannot be dismissed by tapping outside.
        dialogConfirmDelete.show(getSupportFragmentManager(), SHOW_CONFIRM_DELETE_DIALOG_TAG);
    }
    @Override public void onDeleteDialogPositiveClick(DialogFragment dialogFragment, int position) {
        // Interface methods. Interface declared in DialogConfirmDelete. To let MainActivity know
        // which choice was made in the dialog.
        deletePoint(position);
    }
    @Override public void onDeleteDialogNegativeClick(DialogFragment dialogFragment) {
        // Interface method. Interface declared in DialogConfirmDelete.
        mAdapter.notifyDataSetChanged();
    }
    @Override public void onResetDialogPositiveClick(DialogFragment dialogFragment) {
        // Resets the pointcoint to zero.
        resetPointCount();
    }
    @Override public void onResetDialogNegativeClick(DialogFragment dialogFragment) {
    
    }
    private void deletePoint(int position) {
        // Called when deletion confirmed by user pressing confirm in delete dialog.
        // Called by the interface method onDeleteDialogPositiveClick().
        PointLab.getInstance(getApplication()).removePoint(mPointsList.get(position));
        mAdapter.notifyItemRemoved(position);
        mPointsList.remove(position);
        updateTotals();
    }
    private void showAddDialog() {
        // Shows a new dialog to add new points to mPointsList and database.
        // Called by the menu item R.id.add_point.
        DialogFragment addDialog = DialogAdd.newInstance(mPointsList);
        addDialog.show(getSupportFragmentManager(), SHOW_ADD_DIALOG_TAG);
    }
    private void showSelectFileNameDialog() {
        new DialogSelectSaveName().show(getSupportFragmentManager(), SELECT_FILENAME_DIALOG_TAG);
    }
    private void showPercentage() {
        // Displays a new dialog showing the percentage.
        // Called by menu item R.id.bereken_persentasie.
        calculatePercentage();
        DialogFragment showPercentDialog = DialogShowPercent.newInstance(mPointsList);
        showPercentDialog.show(getSupportFragmentManager(), PERCENT_DIALOG_TAG);
    }
    private void calculatePercentage() {
        // Calculates the percentage for each point object for display in the percentage dialog
        // and for saving in the database.
        // Called when user taps on menu item R.id.bereken_persentasie and save database menu items.
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
            updateDatabase();
        }
    }
    @Override protected void onPause() {
        super.onPause();
        // Writes all changes to mPointsList to the database. Sometimes when running instant run,
        // the activity does not go through onPause, only onCreate, so database and mPointsList
        // may not be synchronised when using instant run.
        updateDatabase();
    }
    private void initializeViewItems() {
        // Called from onCreate.
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
        
        mTotalsTextView = findViewById(R.id.textViewAantalTree);
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
        
        switch (id) {
            case R.id.bereken_persentasie:
                showPercentage();
                return true;
            case R.id.add_point:
                showAddDialog();
                return true;
            case R.id.menu_write_to_csv:
                showSelectFileNameDialog();
                return true;
            case R.id.sound_settings:
                Intent intent = new Intent(this, SoundSettings.class);
                startActivity(intent);
                return true;
            case R.id.sort:
                // Resort the list from the database (the sorting is done in
                // PointLab.getPointsList) and update the recyclerview.
                buildPointsList();
                setRecyclerView();
                return true;
            case R.id.reset:
                new DialogConfirmReset().show(getSupportFragmentManager(),
                        SHOW_CONFIRM_RESET_DIALOG_TAG);
                return true;
        }
        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}
        return super.onOptionsItemSelected(item);
    }
    void saveDbToSd() {
        //  Backs up the database to the removable sd card.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
        Date dateNow = new Date();
        String filename = dateFormat.format(dateNow) + "backupdb.db";
        
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/x-sqlite3");
        intent.putExtra(Intent.EXTRA_TITLE, filename);
        startActivityForResult(intent, CREATE_REQUEST_CODE);
    }
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CREATE_REQUEST_CODE) {
                if (data != null) {
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void backupDatabaseToCsv(String fileSaveName)
            throws FileNotFoundException {
        checkPermissions();
        /* Saves the current database to a .csv file on external storage (not removable storage).
        * Method called from the menu (menu_write_to_csv).*/
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm", Locale.US);
        Date dateNow = new Date();
        String filename = fileSaveName + "_" + dateFormat.format(dateNow) + ".csv";
        PointDatabaseHelper databaseHelper = new PointDatabaseHelper(getApplication());
        
        File exportDir = new File(Environment.getExternalStorageDirectory(),
                "CSV_Database_Backups");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
            Log.i(TAG_LOG, "backupDatabaseToCsv: exportDir.mkDirs()");
        }
        File outToCsvFile = new File(exportDir, filename);
        try {
            outToCsvFile.createNewFile();
            // Uses library components found in the module (in this app) dbtocsvhandler.
            CSVWriter csvWriter = new CSVWriter(new FileWriter(outToCsvFile));
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            // Data from 3 columns saved
            String[] cols = {
                    PointDbSchema.PointTable.Cols.POINTNAME,
                    PointDbSchema.PointTable.Cols.POINTCOUNT,
                    PointDbSchema.PointTable.Cols.POINTPERCENTAGE};
            Cursor cursor = database.query(PointDbSchema.PointTable.TABLE_NAME, cols, null, null,
                    null, null, null);
            csvWriter.writeNext(cursor.getColumnNames());
            while (cursor.moveToNext()) {
                String[] arrStr = {
                        cursor.getString(0),
                        cursor.getString(1), cursor.getString(2)};
                csvWriter.writeNext(arrStr);
            }
            csvWriter.close();
            cursor.close();
            Toast.makeText(this, "Suksesvol gespaar", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void backupDatabaseToExt()
            throws FileNotFoundException {
        checkPermissions();
        /* Saves the current database to a backup file in external (not removable) storage. */
        // Formats the backup database name to take a new date in the name with every save.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
        Date dateNow = new Date();
        String filename = dateFormat.format(dateNow) + "velddb.sqlite";
        
        try {
            // Directory where the backup should be saved.
            File sdDir = new File(Environment.getExternalStorageDirectory(), "DatabaseBackups");
            //noinspection ResultOfMethodCallIgnored
            if (sdDir.mkdirs()) {// mkdirs returns true if directory created.
                Log.i(TAG_LOG, "sdDir.mkDirs()");
            }
            if (sdDir.canWrite()) {
                String currentDbPath = getApplicationContext().getDatabasePath(
                        PointDbSchema.DATABASE_NAME).toString();
                File currentDb = new File(currentDbPath);
                File backupDb = new File(sdDir, filename);
                if (currentDb.exists()) {
                    FileChannel source = new FileInputStream(currentDb).getChannel();
                    FileChannel dest = new FileOutputStream(backupDb).getChannel();
                    dest.transferFrom(source, 0, source.size());
                    source.close();
                    dest.close();
                    Toast.makeText(this, "Suksesvol gespaar", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG_LOG, "currentDb does not exist");
                }
            } else {
                Log.e(TAG_LOG, "sdDir.canWrite == false");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG_LOG, "Exception e: " + e.getMessage());
        }
    }
    
    @Override public void onAddDialogDismissed() {
        // Interface method called in DialogAdd's interface callback to notify the activity of a
        // change in mPointsList to update in the database.
        updateDatabase();
        updateTotals();
    }
    void checkPermissions() {
        // Runtime permission request for Android 6.0+.
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG_LOG, "checkPermissions: Permission to write to External Storage denied.");
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_REQUEST_CODE);
        }
    }
    @Override public void onSelectSaveNameDialogDismissed(String fileSaveName) {
        // Method called from within ShowSelectFileNameDialog(). When the finished button is
        // clicked in above mentioned dialog this method is called. The fileSaveName string is
        // passed from the dialog.
        calculatePercentage();
        try {
            backupDatabaseToCsv(fileSaveName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG_LOG, "onOptionsItemSelected: FileNotFoundException: " + e.getMessage());
        }
    }
    class PointRecyclerAdapter
            extends RecyclerView.Adapter<PointRecyclerAdapter.PointViewHolder> {
        
        @Override public PointViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_list_item, parent, false);
            return new PointViewHolder(view);
        }
        
        @Override public void onBindViewHolder(PointViewHolder holder, int position) {
            
            if (mPointsList.size() > 0) {
                holder.setPointName(mPointsList.get(position).getName());
                holder.setPointCount((mPointsList.get(position).getPointCount()));
            }
        }
        
        @Override public int getItemCount() {
            return mPointsList.size();
        }
        
        class PointViewHolder
                extends RecyclerView.ViewHolder {
            TextView nameTextView;
            TextView countTextView;
            
            PointViewHolder(final View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.recyclerview_item_name_textview);
                countTextView = itemView.findViewById(R.id.recyclerview_item_count_textview);
                
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        incrementValue();
                        //Log.i(TAG_LOG, "mTotals = " + mTotals);// for testing
                        // Play button sound if on
                        if (mIsButtonSound) {
                            mSoundPool.stop(mNowPlaying);
                            mNowPlaying = mSoundPool.play(mButtonSound, mVolume, mVolume, 0, 0, 1);
                        }
                        // For the totals sound
                        if (mIsDistSound) {
                            if (mTotals == 50) {
                                mSoundPool.stop(mNowPlaying);
                                mNowPlaying = mSoundPool.play(mDistSound, mVolume, mVolume, 0, 0,
                                        1);
                            }
                            if (mTotals == 100) {
                                mSoundPool.stop(mNowPlaying);
                                mNowPlaying = mSoundPool.play(mDistSound, mVolume, mVolume, 0, 1,
                                        0);
                            }
                            if (mTotals == 200) {
                                mSoundPool.stop(mNowPlaying);
                                mNowPlaying = mSoundPool.play(mDistSound, mVolume, mVolume, 0, 2,
                                        0);
                            }
                        }
                    }
                });
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override public boolean onLongClick(View v) {
                        decrementValue();
                        return true;
                    }
                });
            }
            
            private void incrementValue() {
                // This method takes the adapter position, inserts it as the id of the relevant
                // Point object in mPointsList, retrieves the value inserted in it's countPoint,
                // increments that value and replaces that value with the new one.
                int position = getAdapterPosition();
                Point point = mPointsList.get(position);
                double countValue = point.getPointCount();
                if (countValue >= 0d) {
                    countValue++;
                } else {
                    countValue = 0d;
                }
                updatePoint(position, countValue, point);
                mTotals = mTotals + 1d;
                mTotalsTextView.setText(String.valueOf(mTotals));
            }
            
            private void updatePoint(int position, double countValue, Point point) {
                point.setPointCount(countValue);
                setPointCount(countValue);//updates the text in the textview with the new value
                notifyItemChanged(position);//notifies the adapter of the change
                PointLab.getInstance(getApplication()).updatePoint(point);
            }
            
            private void decrementValue() {
                // Subtract one from count when long clicked on item
                int position = getAdapterPosition();
                Point point = mPointsList.get(position);
                double countValue = point.getPointCount();
                if (countValue > 0d) { //only decrease if more than 0
                    countValue--;
                    if (mTotals > 0d) {// only decrease if more than 0
                        mTotals = mTotals - 1d;
                    } else {
                        mTotals = 0d;
                    }
                } else {
                    countValue = 0d;
                }
                updatePoint(position, countValue, point);
                mTotalsTextView.setText(String.valueOf(mTotals));
            }
    
            void setPointName(String name) {
                nameTextView.setText(name);//keeping the working code in it's encapsulating class
            }
    
            void setPointCount(double newValue) {
                countTextView.setText(String.valueOf(newValue));
            }
        }
    }
    
}
