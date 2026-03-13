package com.shafi.saferide;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VehicleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // MUST match the layout you actually have
        setContentView(R.layout.activity_vehicle_selection);

        // These are TextViews (NOT Buttons)
        TextView bike = findViewById(R.id.btnBike);
        TextView auto = findViewById(R.id.btnAuto);
        TextView car  = findViewById(R.id.btnCar);

        // Safety check (prevents silent crash)
        if (bike == null || auto == null || car == null) {
            Toast.makeText(this, "Vehicle UI not loaded", Toast.LENGTH_LONG).show();
            return;
        }

        bike.setOnClickListener(v -> selectVehicle("BIKE"));
        auto.setOnClickListener(v -> selectVehicle("AUTO"));
        car.setOnClickListener(v -> selectVehicle("CAR"));
    }

    private void selectVehicle(String vehicle) {
        Toast.makeText(this, vehicle + " selected", Toast.LENGTH_SHORT).show();
        // Next screen later (partner selection)
    }
}
;l.