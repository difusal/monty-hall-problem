package com.difusal.montyhallproblem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class StatisticsActivity extends ActionBarActivity implements View.OnClickListener {
    Logic logic;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    View.OnTouchListener gestureListener;
    private GestureDetector gestureDetector;

    public void showResetStatisticsAlert() {
        Log.d("StatisticsActivity", "Displaying reset statistics alert");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle(R.string.reset_statistics_alert_dialog_title);

        // set dialog message
        alertDialogBuilder.setMessage(R.string.reset_statistics_alert_dialog_message);

        // set dialog buttons
        alertDialogBuilder
                .setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logic.resetStatistics();
                        updateTextViews();
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

    public void updateTextViews() {
        Log.d("StatisticsActivity", "Updating text views");

        TextView view;

        view = (TextView) findViewById(R.id.nPlays);
        view.setText(getString(R.string.label_times_played) + " " + Long.toString(logic.nPlays));

        view = (TextView) findViewById(R.id.nSwaps);
        view.setText(getString(R.string.label_times_swapped) + " " + Long.toString(logic.nSwaps));
        view = (TextView) findViewById(R.id.nSwapWins);
        view.setText(getString(R.string.label_times_swapped_and_won) + " " + Long.toString(logic.nSwapWins));
        view = (TextView) findViewById(R.id.nSwapLost);
        view.setText(getString(R.string.label_times_swapped_and_lost) + " " + Long.toString(logic.nSwapLost));

        view = (TextView) findViewById(R.id.nKeeps);
        view.setText(getString(R.string.label_times_kept) + " " + Long.toString(logic.nKeeps));
        view = (TextView) findViewById(R.id.nKeepWins);
        view.setText(getString(R.string.label_times_kept_and_won) + " " + Long.toString(logic.nKeepWins));
        view = (TextView) findViewById(R.id.nKeepLost);
        view.setText(getString(R.string.label_times_kept_and_lost) + " " + Long.toString(logic.nKeepLost));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("StatisticsActivity", "onCreate was called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        logic = MainActivity.logic;
        updateTextViews();

        // Gesture detection
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };

        setViewsTouchListeners();
    }

    public void setViewsTouchListeners() {
        RelativeLayout statisticsRelativeLayout = (RelativeLayout) findViewById(R.id.statisticsRelativeLayout);
        statisticsRelativeLayout.setOnTouchListener(gestureListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.statistics, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;

                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Log.v("StatisticsActivity", "Swiped Left");
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Log.v("StatisticsActivity", "Swiped Right");

                    onBackPressed();
                }
            } catch (Exception e) {
                // nothing
            }

            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

    @Override
    public void onClick(View view) {
        Log.v("StatisticsActivity", "A click was detected");
    }
}
