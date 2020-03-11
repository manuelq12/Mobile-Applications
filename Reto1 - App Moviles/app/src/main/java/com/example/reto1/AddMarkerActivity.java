package com.example.reto1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.UUID;

public class AddMarkerActivity extends AppCompatActivity {

    private EditText txtName;
    private EditText txtLat;
    private EditText txtLong;
    private Button btnSubmit;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marker);

        txtName = findViewById(R.id.txtName);
        txtLat = findViewById(R.id.txtLat);
        txtLong = findViewById(R.id.txtLong);
        btnSubmit = findViewById(R.id.btnSubmit);

        Log.d("Add Marker: ", "Seralizable");
        Serializable serializable = getIntent().getExtras().getSerializable("tempMarker");
        if (serializable != null){
            Log.d("Add Marker: ", "Entro if");
            marker = (Marker) serializable;
            txtName.setText(marker.getName());
            txtLat.setText("" + marker.getLatitude());
            txtLat.setText("" + marker.getLongitude());
        }
        Log.d("Add Marker: ", "Salió if");
        btnSubmit.setOnClickListener(
                (v)->{
                    if(validateInputs()){
                        Marker m = new Marker ();
                        m.setName(txtName.getText().toString());
                        double lat = Double.parseDouble(txtLat.getText().toString());
                        double lon = Double.parseDouble(txtLong.getText().toString());
                        m.setLatitude(lat);
                        m.setLongitude(lon);


                        Intent i = new Intent();
                        i.putExtra("tempMarker", m);
                        setResult(RESULT_OK,i);
                        finish();

                    }

                }
        );

    }

    private boolean validateInputs(){
        String name = txtName.getText().toString();
        if(name.equals("")){
            Toast.makeText(this, "Por favor escriba un nombre para su marcador. ", Toast.LENGTH_SHORT).show();
            return false;
        }

        try{
            double lat = Double.parseDouble(txtLat.getText().toString());
            double lon = Double.parseDouble(txtLong.getText().toString());
        }catch (Exception e){
            Toast.makeText(this, "Existe un error en el valor de Latitud y Longitud", Toast.LENGTH_SHORT).show();
            Log.d("Validate Inputs: ", e.getMessage());
            return false;
        }
        return true;
    }
}
