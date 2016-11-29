package com.wearable.fitnessapp;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.FragmentManager;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap;
import android.app.Fragment;
import android.os.Handler;

public class MainActivity extends AppCompatActivity implements PortraitFragment.ButtonClicked,SensorEventListener{
    private PortraitFragment portraitFragment;
    private boolean startWorkout =false;
    private Sensor accelo;
    private SensorManager sensorManager;
    private double totalsteps=0;
    private double totalseconds=0;
    private long beginningtime;

    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;
        if (values.length > 0) {
            value = (int) values[0];
        }
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER&& startWorkout) {
            totalsteps+=0.1;
            portraitFragment.senddata(totalsteps);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    public void sendText(boolean clicked ){

        portraitFragment.receive(clicked);

    }

    Handler timehandler =new Handler();
    Runnable timerrunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis()- beginningtime;
            int seconds = (int)(millis/1000);
            int minutes = (seconds)/60;
            seconds= seconds%60;
            portraitFragment.setTime(minutes,seconds);
            timehandler.postDelayed(this, 500);

        }
        //Credit goes to Dave.B in StackOverFlow for providing this
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelo= sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor stepdetect = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        Sensor stepcount = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, accelo, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, stepcount, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, stepdetect, SensorManager.SENSOR_DELAY_NORMAL);
        beginningtime= System.currentTimeMillis();

        if(accelo==null)
            Toast.makeText(MainActivity.this, "Device does not contain Accelerometer", Toast.LENGTH_SHORT).show();
        if(stepcount==null)
            Toast.makeText(MainActivity.this, "Device does not contain stepcounter", Toast.LENGTH_SHORT).show();
        if(stepdetect==null)
            Toast.makeText(MainActivity.this, "Device does not contain stepdetecter", Toast.LENGTH_SHORT).show();

        Configuration config= getResources().getConfiguration();

        FragmentManager fragmentManager= getFragmentManager();
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();

        if(config.orientation== Configuration.ORIENTATION_LANDSCAPE){
            LandscapeFragment landscapefragment = new LandscapeFragment();
            fragmentTransaction.replace(android.R.id.content,landscapefragment);
        }else{
            portraitFragment = new PortraitFragment();
            fragmentTransaction.replace(android.R.id.content,portraitFragment);

        }

        fragmentTransaction.commit();
    }

    public void goDetails(View view){
        Intent intent = new Intent(this,DetailsActivity.class);
        startActivity(intent);

    }
    public void goWorkout(View view){
        Button button = (Button) findViewById(R.id.button2);
        if(startWorkout==false) {
            button.setText("Stop Workout");
            startWorkout = true;
            sendText(true);
            timehandler.postDelayed(timerrunnable,0);

        }else{
            startWorkout= false;
            sendText(false);
            button.setText("Start Workout");
            timehandler.removeCallbacks(timerrunnable);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
