package com.example.dulceaprendizaje;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class cuestionario extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout linearLayout;
    private int cont = 0; // Contador de respuestas correctas
    private ArrayList<Pregunta> preguntas; // Lista de preguntas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Cambiar el fondo de la ventana completa (antes de setContentView)
        Drawable miDrawable = ContextCompat.getDrawable(this, R.drawable.fondo);
        getWindow().setBackgroundDrawable(miDrawable);

        // Habilitar diseño de borde a borde (opcional)
        EdgeToEdge.enable(this);

        // Establecer el contenido de la vista
        setContentView(R.layout.activity_cuestionario);

        linearLayout = findViewById(R.id.linearLayout);

        // Generar las preguntas
        preguntas = generarCuestionario();

        // Mostrar el cuestionario
        mostrarCuestionario(preguntas);

        // Vincular el botón "Enviar" con onClick
        Button enviarBtn = findViewById(R.id.enviar); // Reemplaza con el ID del botón en tu XML
        enviarBtn.setOnClickListener(this);

    }

    // Generar las preguntas del cuestionario
    public ArrayList<Pregunta> generarCuestionario() {
        ArrayList<Pregunta> listaPreguntas = new ArrayList<>();
        listaPreguntas.add(new Pregunta("¿Cuál es la capital de Francia?", "Madrid", "París", "Berlín", "París"));
        listaPreguntas.add(new Pregunta("¿Cuál es el resultado de 2 + 2?", "3", "6", "5", "4"));
        listaPreguntas.add(new Pregunta("¿Quién escribió 'Cien años de soledad'?", "Pedro", "Mario Vargas Llosa", "Isabel Allende", "Gabriel García Márquez"));
        listaPreguntas.add(new Pregunta("¿Cuál es el planeta más grande del sistema solar?", "Tierra", "Marte", "Saturno", "Júpiter"));
        listaPreguntas.add(new Pregunta("¿En qué año llegó el hombre a la Luna?", "1965", "1960", "1972", "1969"));
        listaPreguntas.add(new Pregunta("¿Cuál es el océano más grande del mundo?", "Océano Atlántico", "Océano Índico", "Océano Belicoso", "Océano Pacífico"));
        listaPreguntas.add(new Pregunta("¿Qué país es conocido como la tierra del sol naciente?", "China", "Jabón", "Corea del Sur", "Japón"));
        listaPreguntas.add(new Pregunta("¿Quién pintó la Mona Lisa?", "Miguel Ángel", "Leonardo Favio", "Pablo Picasso", "Leonardo da Vinci"));
        listaPreguntas.add(new Pregunta("¿Qué instrumento mide la presión atmosférica?", "Atmosferametro", "Higrómetro", "Anemómetro", "Barómetro"));
        listaPreguntas.add(new Pregunta("¿Cuál es el animal terrestre más rápido del mundo?", "León", "Leopardo", "Ratón", "Guepardo"));
        return listaPreguntas;
    }

    // Mostrar el cuestionario
    public void mostrarCuestionario(ArrayList<Pregunta> preguntas) {
        for (Pregunta pregunta : preguntas) {
            // Crear un TextView para mostrar la pregunta
            TextView preguntaTxt = new TextView(this);
            preguntaTxt.setText(pregunta.getPregunta());
            preguntaTxt.setTextColor(Color.WHITE);
            preguntaTxt.setTextSize(18);
            preguntaTxt.setPadding(16, 16, 16, 16);

            // Crear un RadioGroup para las respuestas
            RadioGroup radioGroup = new RadioGroup(this);
            radioGroup.setOrientation(RadioGroup.VERTICAL);

            // Crear una lista con las respuestas
            List<String> respuestas = new ArrayList<>();
            respuestas.add(pregunta.getAns1());
            respuestas.add(pregunta.getAns2());
            respuestas.add(pregunta.getAns3());
            respuestas.add(pregunta.getAnsCorrecta());

            // Mezclar las respuestas para que estén en un orden aleatorio
            Collections.shuffle(respuestas);

            // Crear un RadioButton para cada respuesta y agregarlo al RadioGroup
            for (String respuesta : respuestas) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(respuesta);
                radioButton.setTextColor(Color.WHITE);
                radioButton.setTextSize(16);
                radioGroup.addView(radioButton);
            }

            // Escuchar cambios en el RadioGroup
            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton selectedButton = findViewById(checkedId);
                if (selectedButton != null && selectedButton.getText().equals(pregunta.getAnsCorrecta())) {
                    cont++; // Incrementar el contador si la respuesta es correcta
                }
            });

            // Agregar los componentes al layout principal
            linearLayout.addView(preguntaTxt);
            linearLayout.addView(radioGroup);
        }
    }

    // Método onClick para manejar los eventos de botones
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.enviar) { // Verificar si es el botón "Enviar"
            // Calcular el porcentaje de aciertos
            int totalPreguntas = preguntas.size();
            int calificacion = (cont * 100) / totalPreguntas;

            // Mostrar calificación en un Toast
            Toast.makeText(this, "Calificación: " + calificacion + "%", Toast.LENGTH_LONG).show();

            // Verificar si la calificación es suficiente
            if (calificacion >= 70) {
                // Enviar señal al ESP32
                sendSignalToESP32("http://192.168.68.61/on"); // Reemplaza con la IP correcta del ESP32
            } else {
                Toast.makeText(this, "No has aprobado, sigue estudiando.", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Método para enviar señal al ESP32
    private void sendSignalToESP32(String urlString) {
        new Thread(() -> {
            HttpURLConnection urlConnection = null;
            try {
                // Crear la URL para la solicitud
                URL url = new URL(urlString);

                // Abrir la conexión HTTP
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET"); // Usar el método GET
                urlConnection.setConnectTimeout(5000); // Tiempo de espera de conexión (5 segundos)
                urlConnection.setReadTimeout(5000);    // Tiempo de espera de lectura (5 segundos)
                urlConnection.connect(); // Iniciar la conexión

                // Verificar el código de respuesta del servidor
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // La solicitud fue exitosa
                    runOnUiThread(() -> Toast.makeText(this, "Motor activado correctamente", Toast.LENGTH_SHORT).show());
                } else {
                    // Hubo un error en la solicitud
                    runOnUiThread(() -> Toast.makeText(this, "Error: Código de respuesta " + responseCode, Toast.LENGTH_SHORT).show());
                }

            } catch (IOException e) {
                e.printStackTrace();
                // Mostrar error en la interfaz de usuario
                runOnUiThread(() -> Toast.makeText(this, "Error de conexión: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect(); // Cerrar la conexión
                }
            }
        }).start(); // Iniciar el hilo
    }


    // Método para convertir dp a px
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

}