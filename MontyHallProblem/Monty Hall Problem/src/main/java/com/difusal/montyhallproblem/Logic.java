package com.difusal.montyhallproblem;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Random;

public class Logic {
    int carDoor, revealedGoatDoor, selectedDoor;
    long nPlays, nSwaps, nKeeps;

    public void loadStatistics(Context context) {
        Log.i("Logic", "Loading statistics");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        nPlays = sharedPref.getLong(context.getString(R.string.label_times_played), 0);
        nSwaps = sharedPref.getLong(context.getString(R.string.label_times_swapped), 0);
        nKeeps = sharedPref.getLong(context.getString(R.string.label_times_kept), 0);
    }

    public void saveStatistics(Context context) {
        Log.i("Logic", "Saving statistics");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(context.getString(R.string.label_times_played), nPlays);
        editor.putLong(context.getString(R.string.label_times_swapped), nSwaps);
        editor.putLong(context.getString(R.string.label_times_kept), nKeeps);
        editor.commit();
    }

    public void generateNewSimulation() {
        Log.d("Logic", "Generating new simulation");

        // randomly place the car
        Random r = new Random();
        carDoor = r.nextInt(3) + 1;

        Log.d("Logic", "The car is on door no. " + carDoor);
    }

    public void revealGoatDoor() {
        revealedGoatDoor = 0;

        // get goat door
        if (selectedDoor == carDoor) {
            Random r = new Random();

            do {
                revealedGoatDoor = r.nextInt(3) + 1;
            } while (revealedGoatDoor == selectedDoor);
        } else
            revealedGoatDoor = 6 - selectedDoor - carDoor;

        Log.d("Logic", "Goat revealed on door no." + revealedGoatDoor);
    }

    public void swapSelectedDoor() {
        selectedDoor = 6 - selectedDoor - revealedGoatDoor;
    }

    public void incSwaps(Context context) {
        Log.d("Logic", "Incrementing nSwaps");

        nSwaps++;
        updateNumPlays(context);
    }

    public void incKeeps(Context context) {
        Log.d("Logic", "Incrementing nKeeps");

        nKeeps++;
        updateNumPlays(context);
    }

    public void updateNumPlays(Context context) {
        Log.d("Logic", "Updating nPlays");

        nPlays = nSwaps + nKeeps;
        saveStatistics(context);
    }

    public void resetStatistics(Context context) {
        Log.d("StatisticsActivity", "Resetting Statistics");

        nSwaps = nKeeps = 0;
        updateNumPlays(context);
    }
}
