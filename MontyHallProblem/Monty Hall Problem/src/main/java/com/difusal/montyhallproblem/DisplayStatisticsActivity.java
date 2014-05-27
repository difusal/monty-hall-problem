package com.difusal.montyhallproblem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class DisplayStatisticsActivity extends ActionBarActivity {
    int nPlays;
    int nSwaps;
    int nKeeps;

    public void loadStatistics() {
        Log.i("DisplayStatisticsActivity", "Loading statistics");
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        nPlays = sharedPref.getInt(getString(R.string.times_played), 0);
        nSwaps = sharedPref.getInt(getString(R.string.times_swapped), 0);
        nKeeps = sharedPref.getInt(getString(R.string.times_kept), 0);
    }

    public void saveStatistics() {
        Log.i("DisplayStatisticsActivity", "Saving statistics");
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.times_played), nPlays);
        editor.putInt(getString(R.string.times_swapped), nSwaps);
        editor.putInt(getString(R.string.times_kept), nKeeps);
        editor.commit();
    }

    public void updateTextViews() {
        Log.d("DisplayStatisticsActivity", "Updating text views");
        TextView nPlaysView = (TextView) findViewById(R.id.nPlays);
        nPlaysView.setText(" " + Integer.toString(nPlays));

        TextView nSwapsView = (TextView) findViewById(R.id.nSwaps);
        nSwapsView.setText(" " + Integer.toString(nSwaps));

        TextView nKeepsView = (TextView) findViewById(R.id.nKeeps);
        nKeepsView.setText(" " + Integer.toString(nKeeps));
    }

    public void updateNumPlays() {
        Log.d("DisplayStatisticsActivity", "Updating nPlays");

        nPlays = nSwaps + nKeeps;

        saveStatistics();

        updateTextViews();
    }

    public void incSwaps(View view) {
        Log.d("DisplayStatisticsActivity", "Incrementing nSwaps");

        nSwaps++;
        updateNumPlays();
    }

    public void incKeeps(View view) {
        Log.d("DisplayStatisticsActivity", "Incrementing nKeeps");

        nKeeps++;
        updateNumPlays();
    }

    public void showResetStatisticsAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Reset statistics?");

        // set dialog message
        alertDialogBuilder
                .setMessage("You'll loose all gathered data!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        resetStatistics();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void resetStatistics() {
        Log.d("DisplayStatisticsActivity", "Resetting Statistics");

        nSwaps = nKeeps = 0;
        updateNumPlays();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_statistics);

        loadStatistics();
        updateTextViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_statistics, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_reset_statistics:
                showResetStatisticsAlert();
                return true;
            case R.id.action_help:
                Intent intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
