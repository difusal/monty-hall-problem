package com.difusal.montyhallproblem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    public static Logic logic;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    View.OnTouchListener gestureListener;
    private GestureDetector gestureDetector;

    public void door1Selected(View view) {
        ImageView imageView = (ImageView) findViewById(R.id.door1);
        imageView.setImageResource(R.drawable.selected_closed_door);

        processSelectedDoor("1");
    }

    public void door2Selected(View view) {
        ImageView imageView = (ImageView) findViewById(R.id.door2);
        imageView.setImageResource(R.drawable.selected_closed_door);

        processSelectedDoor("2");
    }

    public void door3Selected(View view) {
        ImageView imageView = (ImageView) findViewById(R.id.door3);
        imageView.setImageResource(R.drawable.selected_closed_door);
        processSelectedDoor("3");
    }

    public ImageView getDoorImageView(int door) {
        switch (door) {
            case 1:
                return (ImageView) findViewById(R.id.door1);
            case 2:
                return (ImageView) findViewById(R.id.door2);
            case 3:
                return (ImageView) findViewById(R.id.door3);
            default:
                Log.e("MainActivity", "getDoorImageView: No image view found");
                return null;
        }
    }

    public void processSelectedDoor(String str) {
        logic.selectedDoor = Integer.parseInt(str);

        Log.d("MainActivity", "Selected door no. " + logic.selectedDoor);

        disableDoorButtons();
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

    public void revealGoat() {
        logic.revealGoatDoor();

        ImageView imageView = getDoorImageView(logic.revealedGoatDoor);
        imageView.setImageResource(R.drawable.goat_open_door);
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

        disableSwapAndKeepButtons();

        ImageView imageView = getDoorImageView(logic.selectedDoor);
        imageView.setImageResource(R.drawable.closed_door);

        logic.swapSelectedDoor();

        if (logic.selectedDoor == logic.carDoor)
            logic.incSwapWins();
        else
            logic.incSwapLost();

        showProblemResult(logic.selectedDoor == logic.carDoor);
    }

    public void onKeepPress(View view) {
        Log.d("MainActivity", "Pressed keep button");

        disableSwapAndKeepButtons();

        if (logic.selectedDoor == logic.carDoor)
            logic.incKeepWins();
        else
            logic.incKeepLost();

        showProblemResult(logic.selectedDoor == logic.carDoor);
    }

    public void showProblemResult(boolean gotTheCar) {
        // hide step 2 layout
        findViewById(R.id.step_2).setVisibility(View.GONE);

        ImageView imageView = getDoorImageView(logic.selectedDoor);
        ImageView remainingDoorImageView = getDoorImageView(logic.getRemainingDoor());
        TextView label_result = (TextView) findViewById(R.id.label_result);

        if (gotTheCar) {
            imageView.setImageResource(R.drawable.selected_car_open_door);
            remainingDoorImageView.setImageResource(R.drawable.goat_open_door);
            label_result.setText(getString(R.string.label_car_result));
        } else {
            imageView.setImageResource(R.drawable.selected_goat_open_door);
            remainingDoorImageView.setImageResource(R.drawable.car_open_door);
            label_result.setText(getString(R.string.label_goat_result));
        }

        label_result.setVisibility(View.VISIBLE);
    }

    public void closeAllDoors() {
        ImageView imageView;

        imageView = (ImageView) findViewById(R.id.door1);
        imageView.setImageResource(R.drawable.closed_door);

        imageView = (ImageView) findViewById(R.id.door2);
        imageView.setImageResource(R.drawable.closed_door);

        imageView = (ImageView) findViewById(R.id.door3);
        imageView.setImageResource(R.drawable.closed_door);
    }

    public void onRestartButtonPress(View view) {
        Log.d("MainActivity", "Pressed restart button");

        // generate a new monty hall problem simulation
        logic.generateNewSimulation();

        closeAllDoors();
        enableDoorButtons();

        // show step 2 layout
        findViewById(R.id.step_2).setVisibility(View.VISIBLE);
        disableSwapAndKeepButtons();

        // disable restart button
        findViewById(R.id.button_restart).setEnabled(false);

        // hide result label
        findViewById(R.id.label_result).setVisibility(View.INVISIBLE);
    }

    public void onStatisticsButtonPress(View view) {
        Log.d("MainActivity", "Pressed statistics button");

        startStatisticsActivity();
    }

    public void startStatisticsActivity() {
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity", "onCreate was called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create logic
        logic = new Logic(this);

        // load data
        logic.loadStatistics();

        // generate a new monty hall problem simulation
        logic.generateNewSimulation();

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
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        relativeLayout.setOnTouchListener(gestureListener);

        RelativeLayout relativeLayout2 = (RelativeLayout) findViewById(R.id.relativeLayout2);
        relativeLayout2.setOnTouchListener(gestureListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                Intent intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;

                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Log.v("MainActivity", "Swiped Left");

                    startStatisticsActivity();
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Log.v("MainActivity", "Swiped Right");
                }
            } catch (Exception e) {
                e.printStackTrace();
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
        Log.v("MainActivity", "A click was detected");
    }
}
