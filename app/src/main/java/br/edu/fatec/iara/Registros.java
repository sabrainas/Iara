package br.edu.fatec.iara;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.edu.fatec.iara.model.Planta;

public class Registros extends AppCompatActivity {

    private ListView listView;
    private PlantaAdapter adapter;
    private List<Planta> plantas;
    private ProgressBar progressBar;

    private DatabaseReference dbReference;
    private int pageSize = 20;
    private String lastKey = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registros);

        listView = findViewById(R.id.listaRegistros);
        progressBar = findViewById(R.id.progressBar);
        plantas = new ArrayList<>();
        adapter = new PlantaAdapter(this, plantas);
        listView.setAdapter(adapter);

        dbReference = FirebaseDatabase.getInstance().getReference();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registros), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        progressBar.setVisibility(View.VISIBLE);
        loadPage();
    }

    private void loadPage() {
      //  Query queryRef;

    /*    if (lastKey == null) {
            queryRef = dbReference.orderByKey().limitToLast(pageSize);
        } else {
            queryRef = dbReference.orderByKey().limitToLast(pageSize);
        }*/

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                plantas.clear();
                List<Planta> newPlants = new ArrayList<>();

                int i = 0;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.child("Data").getChildren()) {
                        if(i >=500){
                            break;
                        }
                        String timestamp = snapshot.getKey();
                        String nomePlanta = dataSnapshot.child("Planta").child("1").getValue(String.class);
                        double temperaturaAr = dataSnapshot.child("Temperatura_do_ar").child(timestamp).getValue(Double.class);
                        int umidadeAr = dataSnapshot.child("Umidade_do_ar").child(timestamp).getValue(Integer.class);
                        int tds = dataSnapshot.child("TDS").child(timestamp).getValue(Integer.class);
                        //int umidadeSolo = dataSnapshot.child("Umidade_do_solo").child(timestamp).getValue(Integer.class);
                        int umidadeSolo;
                        if(dataSnapshot.child("Umidade_do_solo").child(timestamp).getValue(Integer.class) == null){
                            umidadeSolo = 0;
                        } else {
                            umidadeSolo = dataSnapshot.child("Umidade_do_solo").child(timestamp).getValue(Integer.class);
                        }
                        String dataRegistro = dataSnapshot.child("Data").child(timestamp).getValue(String.class);

                        Planta planta = new Planta(nomePlanta, temperaturaAr, umidadeAr, tds, umidadeSolo, dataRegistro);
                        newPlants.add(planta);
                        lastKey = timestamp;  // Atualiza a chave para a próxima consulta
                        //plantas.add(planta);
                        i++;
                    }

                    // Adicionar os novos registros no início (ordem decrescente)
                    plantas.addAll(0, newPlants);
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE); // Esconder o progresso após carregar os dados.
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
