package br.edu.fatec.iara;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlantaMain extends AppCompatActivity {

    private TextView textViewPlanta;
    private TextView tempAr;
    private TextView umidadeAr;
    private TextView umidadeSolo;
    private TextView tds;
    private Button btnVerRegistro;
    private LinearLayout btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planta);

        // Configuração de insets para a tela
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.planta), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos TextViews e Buttons
        textViewPlanta = findViewById(R.id.textViewPlanta);
        tempAr = findViewById(R.id.tempAr);
        umidadeAr = findViewById(R.id.umidadeAr);
        umidadeSolo = findViewById(R.id.umidadeSolo);
        tds = findViewById(R.id.qtdSolidosDissolvidos);
        btnVerRegistro = findViewById(R.id.btnVerRegistro);
        btnVoltar = findViewById(R.id.btnVoltar);

        // Receber os dados enviados pela atividade anterior
        Intent intent = getIntent();
        String nomePlanta = intent.getStringExtra("nomePlanta");
        double temperaturaArValue = intent.getDoubleExtra("temperaturaAr", 0);
        int umidadeArValue = intent.getIntExtra("umidadeAr", 0);
        int umidadeSoloValue = intent.getIntExtra("umidadeSolo", 0);
        int tdsValue = intent.getIntExtra("tds", 0);
        String dataRegistro = intent.getStringExtra("dataRegistro");

        // Exibir os dados na interface
        textViewPlanta.setText("Planta: " + nomePlanta);
        tempAr.setText(temperaturaArValue + " ºC");
        umidadeAr.setText(umidadeArValue + " %");
        umidadeSolo.setText(umidadeSoloValue + " %");
        tds.setText(tdsValue + " ppm");

        // Configurar listeners para os botões
        btnVerRegistro.setOnClickListener(this::verRegistro);
        btnVoltar.setOnClickListener(this::btnSair);
    }

    public void verRegistro(View view) {
        Intent intent = new Intent(PlantaMain.this, Registros.class);
        startActivity(intent);
    }

    public void voltar(View view){
        Intent intent = new Intent(PlantaMain.this, Home.class);
        startActivity(intent);
    }

    public void btnSair(View view) {
        finish();
    }
}
