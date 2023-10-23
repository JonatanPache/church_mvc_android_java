package com.jonatan.church_mvc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jonatan.church_mvc.Actividad.ActividadView;
import com.jonatan.church_mvc.BitacoraCargo.BitacoraCargoView;
import com.jonatan.church_mvc.Cargo.CargoView;
import com.jonatan.church_mvc.Ingreso.IngresoView;
import com.jonatan.church_mvc.NotaIngreso.NotaIngresoView;
import com.jonatan.church_mvc.Persona.PersonaView;
import com.jonatan.church_mvc.TipoRelacion.TipoRelacionView;

public class MainActivity extends AppCompatActivity {
    private Button btnIngreso, btnNotaIngreso, btnActividad, btnPersona, btnTipoRelacion, btnCargo, btnBitacoraCargo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnIngreso = findViewById(R.id.btnIngreso);
        btnNotaIngreso = findViewById(R.id.btnNotaIngreso);
        btnActividad = findViewById(R.id.btnActividad);
        btnPersona = findViewById(R.id.btnPersona);
        btnTipoRelacion = findViewById(R.id.btnTipoRelacion);
        btnCargo = findViewById(R.id.btnCargo);
        btnBitacoraCargo = findViewById(R.id.btnBitacoraCargo);

        btnPersona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PersonaView.class);
                startActivity(intent);
            }
        });

        btnTipoRelacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TipoRelacionView.class);
                startActivity(intent);
            }
        });

        btnIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IngresoView.class);
                startActivity(intent);
            }
        });

        btnActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActividadView.class);
                startActivity(intent);
            }
        });

        btnNotaIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotaIngresoView.class);
                startActivity(intent);
            }
        });

        btnCargo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CargoView.class);
                startActivity(intent);
            }
        });

        btnBitacoraCargo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BitacoraCargoView.class);
                startActivity(intent);
            }
        });
    }
}