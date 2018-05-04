package com.chalansoftware.veldopname;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SoundSettings
        extends AppCompatActivity {
    
    public static final String TAGPREFS = "settings";
    public static final String TAG_LOG = "log";
    public static final String KEYBUTTONS = "keybuttonsound";
    public static final String KEYDISTANCE = "keydistancesound";
    private SharedPreferences.Editor mEditor;
    private boolean mIsDistSound;
    private boolean mIsButtonSound;
    
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        // Shared preference file for storing app settings
        SharedPreferences prefs = getSharedPreferences(TAGPREFS, MODE_PRIVATE);
        
        mEditor = prefs.edit();
        
        mIsDistSound = prefs.getBoolean(KEYDISTANCE, true);
        mIsButtonSound = prefs.getBoolean(KEYBUTTONS, true);
        
        // Two switches in the sound activity for setting sounds on or off
        Switch buttonSoundSwitch = findViewById(R.id.switch_buttonsound);
        if (mIsButtonSound) {
            buttonSoundSwitch.setChecked(true);
        } else {
            buttonSoundSwitch.setChecked(false);
        }
        Switch distanceSoundSwitch = findViewById(R.id.switch_distancesound);
        if (mIsDistSound) {
            distanceSoundSwitch.setChecked(true);
        } else {
            distanceSoundSwitch.setChecked(false);
        }
        buttonSoundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(TAGPREFS, "onCheckedChanged(buttonsound): " + mIsButtonSound);
                Log.i(TAGPREFS, "isChecked(buttonsound): " + isChecked);
                mIsButtonSound = !mIsButtonSound;
                mEditor.putBoolean(KEYBUTTONS, mIsButtonSound);
                mEditor.apply();
            }
        });
        distanceSoundSwitch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Log.i(TAGPREFS, "onCheckedChanged(distancesound): " + mIsDistSound);
                        Log.i(TAGPREFS, "isChecked(distancesound): " + isChecked);
                        mIsDistSound = !mIsDistSound;
                        mEditor.putBoolean(KEYDISTANCE, mIsDistSound);
                        mEditor.apply();
                    }
                });
    }
    
    @Override protected void onPause() {
        super.onPause();
        // Save the settings
        mEditor.commit();
    }
}
