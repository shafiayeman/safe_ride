package com.shafi.saferide;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private boolean useGps = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnUseLocation = findViewById(R.id.btnUseLocation);
        Button btnContinue = findViewById(R.id.btnContinue);
        EditText etPickupManual = findViewById(R.id.etPickupManual);
        EditText etDestination = findViewById(R.id.etDestination);

        // ---------- Use GPS ----------
        btnUseLocation.setOnClickListener(v -> {
            useGps = true;
            etPickupManual.setText("Current location");
            etPickupManual.setEnabled(false);
        });

        // ---------- Continue button ----------
        btnContinue.setOnClickListener(v ->
                goToVehicleScreen(etPickupManual, etDestination)
        );

        // ---------- ENTER key logic (THIS WAS MISSING) ----------
        etDestination.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_GO ||
                    actionId == EditorInfo.IME_ACTION_NEXT) {

                goToVehicleScreen(etPickupManual, etDestination);
                return true; // VERY IMPORTANT
            }

            return false;
        });
    }

    // ---------- Single navigation method (clean & safe) ----------
    private void goToVehicleScreen(EditText etPickupManual, EditText etDestination) {

        String pickup = etPickupManual.getText().toString().trim();
        String destination = etDestination.getText().toString().trim();

        if (destination.isEmpty()) {
            Toast.makeText(this, "Enter destination", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity.this, VehicleActivity.class);
        intent.putExtra("pickup", pickup);
        intent.putExtra("destination", destination);
        intent.putExtra("useGps", useGps);
        startActivity(intent);
    }
}
