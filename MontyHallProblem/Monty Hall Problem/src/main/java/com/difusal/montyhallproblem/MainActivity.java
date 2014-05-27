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
    public static Logic logic = new Logic();

    public void door1Selected(View view) {
        processSelectedDoor("1");
    }

    public void door2Selected(View view) {
        processSelectedDoor("2");
    }

    public void door3Selected(View view) {
        processSelectedDoor("3");
    }

    public void processSelectedDoor(String str) {
        logic.selectedDoor = Integer.parseInt(str);

        Log.d("MainActivity", "Selected door no. " + logic.selectedDoor);

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
        logic.revealGoatDoor();

        TextView labelGoatRevelation = (TextView) findViewById(R.id.label_goat_revelation);
        labelGoatRevelation.setText(getString(R.string.label_goat_revelation) + " " + Integer.toString(logic.revealedGoatDoor) + ".");
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

        logic.incSwaps(this);
        disableSwapAndKeepButtons();

        logic.swapSelectedDoor();

        TextView textView = (TextView) findViewById(R.id.label_swap_or_keep_action_echo);
        textView.setText(getString(R.string.swap_action_echo) + " " + logic.selectedDoor);
        textView.setVisibility(View.VISIBLE);

        showProblemResult(logic.selectedDoor == logic.carDoor);
    }

    public void onKeepPress(View view) {
        Log.d("MainActivity", "Pressed keep button");

        logic.incKeeps(this);
        disableSwapAndKeepButtons();

        TextView textView = (TextView) findViewById(R.id.label_swap_or_keep_action_echo);
        textView.setText(getString(R.string.keep_action_echo) + " " + logic.selectedDoor);
        textView.setVisibility(View.VISIBLE);

        showProblemResult(logic.selectedDoor == logic.carDoor);
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

        // generate a new monty hall problem simulation
        logic.generateNewSimulation();

        enableDoorButtons();
        updateLabelChosenDoor("?");
        findViewById(R.id.label_goat_revelation).setVisibility(View.INVISIBLE);
        disableSwapAndKeepButtons();
        findViewById(R.id.button_restart).setEnabled(false);
        findViewById(R.id.label_result).setVisibility(View.INVISIBLE);
        findViewById(R.id.label_swap_or_keep_action_echo).setVisibility(View.INVISIBLE);
    }

    public void onStatisticsButtonPress(View view) {
        Log.d("MainActivity", "Pressed statistics button");

        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load data
        logic.loadStatistics(this);

        // initialize label chosen door
        updateLabelChosenDoor("?");

        // generate a new monty hall problem simulation
        logic.generateNewSimulation();
    }

    @Override
    protected void onPause() {
        super.onPause();

        logic.saveStatistics(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        logic.loadStatistics(this);
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
