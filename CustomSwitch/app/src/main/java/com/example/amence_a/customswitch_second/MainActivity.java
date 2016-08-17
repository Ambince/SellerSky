package com.example.amence_a.customswitch_second;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SwitchToggle switchToggle = (SwitchToggle) findViewById(R.id.toggleView);

        switchToggle.setOnSwitchStateUpdateListener(new SwitchToggle.onSwitchStateUpdateListener() {
            @Override
            public void onStateUpdate(boolean state) {
                Toast.makeText(MainActivity.this, state + "", Toast.LENGTH_SHORT).show();
                Log.v("Amence", state + "");
            }
        });
    }
}
