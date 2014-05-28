package com.difusal.montyhallproblem;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Random;

public class Logic {
    Context context;
    int carDoor, revealedGoatDoor, selectedDoor;
    long nSwapWins, nSwapLost, nKeepWins, nKeepLost;
    long nPlays, nSwaps, nKeeps;

    public Logic(Context context) {
        this.context = context;
    }

    public void loadStatistics() {
        Log.i("Logic", "Loading statistics");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        nSwapWins = sharedPref.getLong(context.getString(R.string.swapped_and_won_key), 0);
        nSwapLost = sharedPref.getLong(context.getString(R.string.swapped_and_lost_key), 0);
        nKeepWins = sharedPref.getLong(context.getString(R.string.kept_and_won_key), 0);
        nKeepLost = sharedPref.getLong(context.getString(R.string.kept_and_lost_key), 0);

        updateNumPlays();
    }

    public void saveStatistics() {
        Log.i("Logic", "Saving statistics");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(context.getString(R.string.swapped_and_won_key), nSwapWins);
        editor.putLong(context.getString(R.string.swapped_and_lost_key), nSwapLost);
        editor.putLong(context.getString(R.string.kept_and_won_key), nKeepWins);
        editor.putLong(context.getString(R.string.kept_and_lost_key), nKeepLost);
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

    public int getRemainingDoor() {
        return 6 - selectedDoor - revealedGoatDoor;
    }

    public void incSwapWins() {
        Log.d("Logic", "nSwapWins++");

        nSwapWins++;
        updateNumPlays();
        saveStatistics();
    }

    public void incSwapLost() {
        Log.d("Logic", "nSwapLost++");

        nSwapLost++;
        updateNumPlays();
        saveStatistics();
    }

    public void incKeepWins() {
        Log.d("Logic", "nKeepWins++");

        nKeepWins++;
        updateNumPlays();
        saveStatistics();
    }

    public void incKeepLost() {
        Log.d("Logic", "nKeepLost++");

        nKeepLost++;
        updateNumPlays();
        saveStatistics();
    }

    public void updateNumPlays() {
        Log.v("Logic", "nSwapWins = " + nSwapWins);
        Log.v("Logic", "nSwapLost = " + nSwapLost);

        Log.v("Logic", "nKeepWins = " + nKeepWins);
        Log.v("Logic", "nKeepLost = " + nKeepLost);

        nSwaps = nSwapWins + nSwapLost;
        Log.v("Logic", "nSwaps = " + nSwaps);

        nKeeps = nKeepWins + nKeepLost;
        Log.v("Logic", "nKeeps = " + nKeeps);

        nPlays = nSwaps + nKeeps;
        Log.v("Logic", "nPlays = " + nPlays);
    }

    public void resetStatistics() {
        Log.i("StatisticsActivity", "Resetting Statistics");

        nSwapWins = nSwapLost = nKeepWins = nKeepLost = 0;
        updateNumPlays();
        saveStatistics();
    }
}
