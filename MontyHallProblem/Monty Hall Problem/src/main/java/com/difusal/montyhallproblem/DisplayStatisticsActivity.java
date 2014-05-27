package com.difusal.montyhallproblem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayStatisticsActivity extends ActionBarActivity {
    long nPlays;
    long nSwaps;
    long nKeeps;

    public void loadStatistics() {
        Log.i("DisplayStatisticsActivity", "Loading statistics");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        nPlays = sharedPref.getLong(getString(R.string.label_times_played), 0);
        nSwaps = sharedPref.getLong(getString(R.string.label_times_swapped), 0);
        nKeeps = sharedPref.getLong(getString(R.string.label_times_kept), 0);
    }

    public void saveStatistics() {
        Log.i("DisplayStatisticsActivity", "Saving statistics");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(getString(R.string.label_times_played), nPlays);
        editor.putLong(getString(R.string.label_times_swapped), nSwaps);
        editor.putLong(getString(R.string.label_times_kept), nKeeps);
        editor.commit();
    }

    public void showResetStatisticsAlert() {
        Log.d("DisplayStatisticsActivity", "Displaying reset statistics alert");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle(R.string.reset_statistics_alert_dialog_title);

        // set dialog message
        alertDialogBuilder.setMessage(R.string.reset_statistics_alert_dialog_message);

        alertDialogBuilder
                .setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        resetStatistics();
                    }
                })
                .setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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

    public void updateNumPlays() {
        Log.d("DisplayStatisticsActivity", "Updating nPlays");

        nPlays = nSwaps + nKeeps;
        saveStatistics();
        updateTextViews();
    }

    public void updateTextViews() {
        Log.d("DisplayStatisticsActivity", "Updating text views");

        TextView nPlaysView = (TextView) findViewById(R.id.nPlaysLabel);
        nPlaysView.setText(getString(R.string.label_times_played) + " " + Long.toString(nPlays));

        TextView nSwapsView = (TextView) findViewById(R.id.nSwapsLabel);
        nSwapsView.setText(getString(R.string.label_times_swapped) + " " + Long.toString(nSwaps));

        TextView nKeepsView = (TextView) findViewById(R.id.nKeepsLabel);
        nKeepsView.setText(getString(R.string.label_times_kept) + " " + Long.toString(nKeeps));
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
            case android.R.id.home:
                super.onOptionsItemSelected(item);
                onBackPressed();
                return true;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
