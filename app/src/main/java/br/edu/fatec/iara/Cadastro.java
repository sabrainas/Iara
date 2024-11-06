package br.edu.fatec.iara;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.edu.fatec.iara.model.Planta;

public class Cadastro extends AppCompatActivity {
    private DatabaseReference dbReference;
    private EditText txtNome;
    private Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbReference = FirebaseDatabase.getInstance().getReference().child("Planta");
        txtNome = findViewById(R.id.txtNome);
        btnSalvar = findViewById(R.id.btnSalvar);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void botao(View view){
        if (!txtNome.getText().toString().isEmpty()) {
            adicionarPlanta(txtNome.getText().toString());
        } else {
            Toast.makeText(Cadastro.this, "Digite o nome da planta", Toast.LENGTH_SHORT).show();
        }
    }

    private void adicionarPlanta(String nome) {
        // Busca o último ID (se o Firebase for configurado com IDs numéricos sequenciais)
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String timestamp = "";

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.child("Data").getChildren()) {
                        timestamp = snapshot.getKey();
                    }


                    int nextId = Integer.parseInt(timestamp);
                    nextId++;

                    // Cria o objeto Planta com o nome inserido
                    Planta novaPlanta = new Planta(nome);

                    // Insere a nova planta com o ID incrementado
                    dbReference.child(String.valueOf(nextId)).setValue(novaPlanta)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(Cadastro.this, "Planta adicionada com sucesso!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(Cadastro.this, "Falha ao adicionar planta", Toast.LENGTH_SHORT).show();
                        });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Cadastro.this, "Erro ao acessar o banco de dados", Toast.LENGTH_SHORT).show();
            }
        });
    }
}