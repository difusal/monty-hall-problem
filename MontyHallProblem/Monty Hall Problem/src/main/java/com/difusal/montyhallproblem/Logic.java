package com.difusal.montyhallproblem;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Random;

public class Logic {
    int carDoor, revealedGoatDoor, selectedDoor;
    long nSwapWins, nSwapLost, nKeepWins, nKeepLost;
    long nPlays, nSwaps, nKeeps;

    public void loadStatistics(Context context) {
        Log.i("Logic", "Loading statistics");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        nSwapWins = sharedPref.getLong(context.getString(R.string.label_times_swapped_and_won), 0);
        nSwapLost = sharedPref.getLong(context.getString(R.string.label_times_swapped_and_lost), 0);
        nKeepWins = sharedPref.getLong(context.getString(R.string.label_times_kept_and_won), 0);
        nKeepLost = sharedPref.getLong(context.getString(R.string.label_times_kept_and_lost), 0);

        updateNumPlays(context);
    }

    public void saveStatistics(Context context) {
        Log.i("Logic", "Saving statistics");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(context.getString(R.string.label_times_swapped_and_won), nSwapWins);
        editor.putLong(context.getString(R.string.label_times_swapped_and_lost), nSwapLost);
        editor.putLong(context.getString(R.string.label_times_kept_and_won), nKeepWins);
        editor.putLong(context.getString(R.string.label_times_kept_and_lost), nKeepLost);
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

    public void incSwapWins(Context context) {
        Log.d("Logic", "nSwapWins++");

        nSwapWins++;
        updateNumPlays(context);
    }

    public void incSwapLost(Context context) {
        Log.d("Logic", "nSwapLost++");

        nSwapLost++;
        updateNumPlays(context);
    }

    public void incKeepWins(Context context) {
        Log.d("Logic", "nKeepWins++");

        nKeepWins++;
        updateNumPlays(context);
    }

    public void incKeepLost(Context context) {
        Log.d("Logic", "nKeepLost++");

        nKeepLost++;
        updateNumPlays(context);
    }

    public void updateNumPlays(Context context) {
        nSwaps = nSwapWins + nSwapLost;
        Log.d("Logic", "nSwaps = " + nSwaps);

        nKeeps = nKeepWins + nKeepLost;
        Log.d("Logic", "nKeeps = " + nKeeps);

        nPlays = nSwaps + nKeeps;
        Log.d("Logic", "nPlays = " + nPlays);

        saveStatistics(context);
    }

    public void resetStatistics(Context context) {
        Log.d("StatisticsActivity", "Resetting Statistics");

        nSwapWins = nSwapLost = nKeepWins = nKeepLost = 0;
        updateNumPlays(context);
    }
}
