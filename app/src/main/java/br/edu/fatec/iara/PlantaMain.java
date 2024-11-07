package br.edu.fatec.iara;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.edu.fatec.iara.model.Planta;

public class PlantaMain extends AppCompatActivity {

    private TextView textViewPlanta;
    private TextView tempAr;
    private TextView umidadeAr;
    private TextView umidadeSolo;
    private TextView tds;
    private Button btnVerRegistro;
    private LinearLayout btnVoltarHome;
    String nomePlanta;
    double temperaturaArValue;
    int umidadeArValue;
    int umidadeSoloValue;
    int tdsValue;
    String dataRegistro;

    private LineChart lineChart; //grafico
    private DatabaseReference dbReference;
    private String lastKey = null;

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
        btnVoltarHome = findViewById(R.id.btnVoltarHome);

        lineChart = findViewById(R.id.lineChart); //grafico

        dbReference = FirebaseDatabase.getInstance().getReference();

        setupLineChart();

        loadChartData();
        // Receber os dados enviados pela atividade anterior
        Intent intent = getIntent();
        nomePlanta = intent.getStringExtra("nomePlanta");
        temperaturaArValue = intent.getDoubleExtra("temperaturaAr", 0);
        umidadeArValue = intent.getIntExtra("umidadeAr", 0);
        umidadeSoloValue = intent.getIntExtra("umidadeSolo", 0);
        tdsValue = intent.getIntExtra("tds", 0);
        dataRegistro = intent.getStringExtra("dataRegistro");

        // Exibir os dados na interface
        textViewPlanta.setText("Planta: " + nomePlanta);
        tempAr.setText(temperaturaArValue + " ºC");
        umidadeAr.setText(umidadeArValue + " %");
        umidadeSolo.setText(umidadeSoloValue + "");
        tds.setText(tdsValue + " ppm");


        // Configurar listeners para os botões
        btnVerRegistro.setOnClickListener(this::verRegistro);
        btnVoltarHome.setOnClickListener(this::voltar);
    }

    private List<String> dataRegistroList = new ArrayList<>();

    private void setupLineChart() {
        lineChart.setNoDataText("Carregando dados...");
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
    }

    private void loadChartData() {
        dbReference.child("TDS").limitToFirst(100).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Entry> entries = new ArrayList<>();
                int index = 0;
                dataRegistroList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String timestamp = snapshot.getKey();

                    // Obtendo o valor de TDS
                    Integer tdsValue = snapshot.getValue(Integer.class);
                    if (tdsValue == null) {
                        tdsValue = 0;
                    }

                    // Armazenar o timestamp ou data correspondente no `dataRegistroList`
                    dataRegistroList.add(timestamp);  // Ajuste se precisar de uma data formatada

                    // Adicionar o valor ao gráfico
                    entries.add(new Entry(index++, tdsValue));
                }

                // Atualizar o gráfico se houver dados
                if (!entries.isEmpty()) {
                    LineDataSet dataSet = new LineDataSet(entries, "Crescimento da Planta (TDS)");
                    dataSet.setColor(Color.parseColor("#319f67"));
                    dataSet.setValueTextColor(Color.BLACK);
                    dataSet.setLineWidth(2f);
                    LineData lineData = new LineData(dataSet);
                    lineChart.setData(lineData);

                    configureChart();
                    lineChart.invalidate();
                } else {
                    lineChart.setNoDataText("Nenhum dado disponível.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Erro ao carregar dados: " + error.getMessage());
            }
        });
    }


    private void configureChart(){
        YAxis leftAxis = lineChart.getAxisLeft();
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(1000f);
        leftAxis.setGranularity(100f);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(){
            @Override
            public String getFormattedValue(float value, AxisBase axis){
                int index = Math.round(value);
                if (index >= 0 && index < dataRegistroList.size()) {
                    return dataRegistroList.get(index);
                }
                return "";
            }
        });
    }

    public void verRegistro(View view) {
        Intent intent = new Intent(PlantaMain.this, Registros.class);

        // Envie dados da planta selecionada (como nome) para a próxima atividade
        intent.putExtra("nomePlanta", nomePlanta);
        intent.putExtra("temperaturaAr", temperaturaArValue);
        intent.putExtra("umidadeAr", umidadeArValue);
        intent.putExtra("umidadeSolo", umidadeSoloValue);
        intent.putExtra("tds", tdsValue);
        intent.putExtra("dataRegistro", dataRegistro);
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
