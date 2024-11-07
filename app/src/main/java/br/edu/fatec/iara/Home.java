package br.edu.fatec.iara;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.edu.fatec.iara.model.Planta;

public class Home extends AppCompatActivity {

    private Button btnCadastrar;
    private DatabaseReference dbReference;
    private ListView listView;
    private PlantaAdapter adapter;
    private List<Planta> plantas;
    //private static final String TAG = "Home";

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define a imersão na janela sem barras de sistema
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_home);

        btnCadastrar = findViewById(R.id.btnCadastrar);
        dbReference = FirebaseDatabase.getInstance().getReference();
        listView = findViewById(R.id.listaPlantas);
        plantas = new ArrayList<>();
        adapter = new PlantaAdapter(this, plantas, false);
        listView.setAdapter(adapter);
        
        // Aplicação dos insets para ajustar o padding e evitar sobreposição
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        // Criando uma lista de objetos Planta
        //List<Planta> plantas = new ArrayList<>();


        //plantas.add(new Planta("Alface", 24.5, 60, 1000, 40, "07/11/2024"));

        // Configurando o ListView com o PlantaAdapter
        /*PlantaAdapter adapter = new PlantaAdapter(this, plantas);
        ListView listView = findViewById(R.id.listaPlantas);*/
        //listView.setAdapter(adapter);

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                plantas.clear();
                String timestamp = "";
                String escolhidoTimestamp = "";
                String normalTimestamp = "";
                String escolhido = "";

                if (dataSnapshot.exists()) {
                    int maior = 0;
                    for (DataSnapshot snapshot : dataSnapshot.child("Data").getChildren()) {
                        timestamp = snapshot.getKey();
                        if (timestamp.contains("_")) {
                            escolhidoTimestamp = timestamp.split("_")[0];
                            String resto = timestamp.split("_")[1];

                            if ((Integer.parseInt(escolhidoTimestamp) > maior)) {
                                maior = Integer.parseInt(escolhidoTimestamp);
                                escolhido = resto;
                            }
                        } else {
                            normalTimestamp = timestamp;
                        }
                    }
                    if(normalTimestamp == ""){
                        timestamp = String.valueOf(maior);
                        timestamp = timestamp + "_" + escolhido;
                    } else {
                        timestamp = normalTimestamp;
                    }

                    Log.i("HOME", timestamp);
                    String nomePlanta = dataSnapshot.child("Planta").child("1").getValue(String.class);
                    Double temperaturaAr;
                    if(dataSnapshot.child("Temperatura_do_ar").child(timestamp).getValue(Integer.class) == null){
                        temperaturaAr = 0.0;
                    } else {
                        temperaturaAr = dataSnapshot.child("Temperatura_do_ar").child(timestamp).getValue(Double.class);
                    }
                    int umidadeAr;
                    if(dataSnapshot.child("Umidade_do_ar").child(timestamp).getValue(Integer.class) == null){
                        umidadeAr = 0;
                    } else {
                        umidadeAr = dataSnapshot.child("Umidade_do_ar").child(timestamp).getValue(Integer.class);
                    }
                    int tds;
                    if(dataSnapshot.child("TDS").child(timestamp).getValue(Integer.class) == null){
                        tds = 0;
                    } else {
                        tds = dataSnapshot.child("TDS").child(timestamp).getValue(Integer.class);
                    }
                    //int umidadeSolo = dataSnapshot.child("Umidade_do_solo").child(timestamp).getValue(Integer.class);
                    int umidadeSolo;
                    if(dataSnapshot.child("Umidade_do_solo").child(timestamp).getValue(Integer.class) == null){
                        umidadeSolo = 0;
                    } else {
                        umidadeSolo = dataSnapshot.child("Umidade_do_solo").child(timestamp).getValue(Integer.class);
                    }
                    String dataRegistro = dataSnapshot.child("Data").child(timestamp).getValue(String.class);

                    Planta planta = new Planta(nomePlanta, temperaturaAr, umidadeAr, tds, umidadeSolo, dataRegistro);
                    //newPlants.add(planta);
                    //lastKey = timestamp;  // Atualiza a chave para a próxima consulta
                    //plantas.add(planta);

                    plantas.add(planta);
                    // Adicionar os novos registros no início (ordem decrescente)
                    //plantas.addAll(0, newPlants);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Obtenha o objeto Planta correspondente ao item clicado
            Planta plantaSelecionada = adapter.getItem(position);

            // Crie um Intent para iniciar a atividade PlantaMain
            Intent intent = new Intent(Home.this, PlantaMain.class);

            // Envie dados da planta selecionada (como nome) para a próxima atividade
            intent.putExtra("nomePlanta", plantaSelecionada.getNome());
            intent.putExtra("temperaturaAr", plantaSelecionada.getTemperaturaAr());
            intent.putExtra("umidadeAr", plantaSelecionada.getUmidadeAr());
            intent.putExtra("umidadeSolo", plantaSelecionada.getUmidadeSolo());
            intent.putExtra("tds", plantaSelecionada.getTds());
            intent.putExtra("dataRegistro", plantaSelecionada.getDataRegistro());

            // Inicie a atividade PlantaMain
            startActivity(intent);
        });
    }

    public void registrar(View v){
        Intent it = new Intent(getApplicationContext(), Cadastro.class);
        startActivity(it);
    }
}
