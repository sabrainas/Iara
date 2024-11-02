package br.edu.fatec.iara;

import android.os.Bundle;
import android.widget.ListView;

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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.edu.fatec.iara.model.Planta;

public class Home extends AppCompatActivity {

    private ListView listView;
    private PlantaAdapter adapter;
    private List<Planta> plantas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        listView = findViewById(R.id.listViewPlantas);
        plantas = new ArrayList<>();
        adapter = new PlantaAdapter(this, plantas);
        listView.setAdapter(adapter);
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                plantas.clear(); // Limpa a lista antes de adicionar novos dados

                for (DataSnapshot snapshot : dataSnapshot.child("Data").getChildren()) {
                    String timestamp = snapshot.getKey();
                    String nomePlanta = "Planta " + timestamp; // Exemplo de nome, ajuste conforme necessário
                    double temperaturaAr = dataSnapshot.child("Temperatura_do_ar").child(timestamp).getValue(Double.class);
                    int umidadeAr = dataSnapshot.child("Umidade_do_ar").child(timestamp).getValue(Integer.class);
                    int tds = dataSnapshot.child("TDS").child(timestamp).getValue(Integer.class);
                    int umidadeSolo = dataSnapshot.child("Umidade_do_solo").child(timestamp).getValue(Integer.class);

                    Planta planta = new Planta(nomePlanta, temperaturaAr, umidadeAr, tds, umidadeSolo);
                    plantas.add(planta);
                }

                adapter.notifyDataSetChanged(); // Notifica o adapter sobre os dados atualizados
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Tratar erro
            }
        });
    }
}