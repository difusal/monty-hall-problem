package com.difusal.montyhallproblem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
    int goatDoor = 2;

    long nPlays;
    long nSwaps;
    long nKeeps;

    public void loadStatistics() {
        Log.i("MainActivity", "Loading statistics");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        nPlays = sharedPref.getLong(getString(R.string.label_times_played), 0);
        nSwaps = sharedPref.getLong(getString(R.string.label_times_swapped), 0);
        nKeeps = sharedPref.getLong(getString(R.string.label_times_kept), 0);
    }

    public void saveStatistics() {
        Log.i("MainActivity", "Saving statistics");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(getString(R.string.label_times_played), nPlays);
        editor.putLong(getString(R.string.label_times_swapped), nSwaps);
        editor.putLong(getString(R.string.label_times_kept), nKeeps);
        editor.commit();
    }

    public void updateNumPlays() {
        Log.d("MainActivity", "Updating nPlays");

        nPlays = nSwaps + nKeeps;
        saveStatistics();
    }

    public void incSwaps() {
        Log.d("MainActivity", "Incrementing nSwaps");

        nSwaps++;
        updateNumPlays();
    }

    public void incKeeps() {
        Log.d("MainActivity", "Incrementing nKeeps");

        nKeeps++;
        updateNumPlays();
    }

    public void door1Selected(View view) {
        Log.d("MainActivity", "Selected door no. 1");

        processSelectedDoor("1");
    }

    public void door2Selected(View view) {
        Log.d("MainActivity", "Selected door no. 2");

        processSelectedDoor("2");
    }

    public void door3Selected(View view) {
        Log.d("MainActivity", "Selected door no. 3");

        processSelectedDoor("3");
    }

    public void processSelectedDoor(String str) {
        disableDoorButtons();
        updateLabelChosenDoor(str);
        revealGoat();
        enableSwapAndKeepButtons();
        findViewById(R.id.button_restart).setEnabled(true);
    }

    public void setDoorButtonsEnabledParameter(boolean state) {
        findViewById(R.id.door1).setEnabled(state);
        findViewById(R.id.door2).setEnabled(state);
        findViewById(R.id.door3).setEnabled(state);
    }

    public void enableDoorButtons() {
        setDoorButtonsEnabledParameter(true);
    }

    public void disableDoorButtons() {
        setDoorButtonsEnabledParameter(false);
    }

    public void updateLabelChosenDoor(String str) {
        TextView labelChosenDoor = (TextView) findViewById(R.id.label_chosen_door);
        labelChosenDoor.setText(getString(R.string.label_chosen_door) + " " + str + ".");
    }

    public void revealGoat() {
        Log.d("MainActivity", "Goat revealed on door no." + goatDoor);

        TextView labelGoatRevelation = (TextView) findViewById(R.id.label_goat_revelation);
        labelGoatRevelation.setText(getString(R.string.label_goat_revelation) + " " + Integer.toString(goatDoor) + ".");
        labelGoatRevelation.setVisibility(View.VISIBLE);
    }

    public void setSwapAndKeepButtonsEnabledParameter(boolean state) {
        findViewById(R.id.button_swap).setEnabled(state);
        findViewById(R.id.button_keep).setEnabled(state);
    }

    public void enableSwapAndKeepButtons() {
        setSwapAndKeepButtonsEnabledParameter(true);
    }

    public void disableSwapAndKeepButtons() {
        setSwapAndKeepButtonsEnabledParameter(false);
    }

    public void onSwapPress(View view) {
        Log.d("MainActivity", "Pressed swap button");

        incSwaps();
        disableSwapAndKeepButtons();
        showProblemResult(true);
    }

    public void onKeepPress(View view) {
        Log.d("MainActivity", "Pressed keep button");

        incKeeps();
        disableSwapAndKeepButtons();
        showProblemResult(false);
    }

    public void showProblemResult(boolean gotTheCar) {
        TextView label_result = (TextView) findViewById(R.id.label_result);

        if (gotTheCar)
            label_result.setText(getString(R.string.label_car_result));
        else
            label_result.setText(getString(R.string.label_goat_result));

        label_result.setVisibility(View.VISIBLE);
    }

    public void onRestartButtonPress(View view) {
        Log.d("MainActivity", "Pressed restart button");

        enableDoorButtons();
        updateLabelChosenDoor("?");
        findViewById(R.id.label_goat_revelation).setVisibility(View.INVISIBLE);
        disableSwapAndKeepButtons();
        findViewById(R.id.button_restart).setEnabled(false);
        findViewById(R.id.label_result).setVisibility(View.INVISIBLE);
    }

    public void onStatisticsButtonPress(View view) {
        Log.d("MainActivity", "Pressed statistics button");

        Intent intent = new Intent(this, DisplayStatisticsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load data
        loadStatistics();

        // initialize label chosen door
        updateLabelChosenDoor("?");
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveStatistics();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadStatistics();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_help:
                Intent intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
