package com.example.dulceaprendizaje;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cambiar el fondo de la ventana completa (antes de setContentView)
        Drawable miDrawable = ContextCompat.getDrawable(this, R.drawable.inicio);
        getWindow().setBackgroundDrawable(miDrawable);

        // Habilitar diseño de borde a borde (opcional)
        EdgeToEdge.enable(this);

        // Establecer el contenido de la vista
        setContentView(R.layout.activity_main);

        // Vincula el botón con el ID
        start = findViewById(R.id.btnEmpezar);

        // Configurar el listener del botón
        start.setOnClickListener(v -> empezar(v));
    }

    // Método que se llama cuando el botón es clickeado
    public void empezar(View view) {
        Intent intent = new Intent(MainActivity.this, cuestionario.class);
        startActivity(intent);
    }
}