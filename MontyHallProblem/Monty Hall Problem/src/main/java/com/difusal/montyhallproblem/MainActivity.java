package com.difusal.montyhallproblem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
    int goatDoor = 2;

    public void door1Selected(View view) {
        Log.d("MainActivity", "Selected door no. 1" + goatDoor);

        processSelectedDoor("1");
    }

    public void door2Selected(View view) {
        Log.d("MainActivity", "Selected door no. 2" + goatDoor);

        processSelectedDoor("2");
    }

    public void door3Selected(View view) {
        Log.d("MainActivity", "Selected door no. 3" + goatDoor);

        processSelectedDoor("3");
    }

    public void processSelectedDoor(String str) {
        disableDoorButtons();
        updateLabelChosenDoor(str);
        revealGoat();
        enableSwapAndKeepButtons();
    }

    public void updateLabelChosenDoor(String str) {
        TextView labelChosenDoor = (TextView) findViewById(R.id.label_chosen_door);
        labelChosenDoor.setText(getString(R.string.label_chosen_door) + " " + str);
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

    public void revealGoat() {
        Log.d("MainActivity", "Goat revealed on door no." + goatDoor);

        TextView labelGoatRevelation = (TextView) findViewById(R.id.label_goat_revelation);
        labelGoatRevelation.setText(getString(R.string.label_goat_revelation) + " " + Integer.toString(goatDoor) + ".");
        labelGoatRevelation.setVisibility(View.VISIBLE);
    }

    public void displayStatistics(View view) {
        Intent intent = new Intent(this, DisplayStatisticsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize label chosen door
        updateLabelChosenDoor("?");
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
