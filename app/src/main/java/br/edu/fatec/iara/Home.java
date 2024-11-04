package br.edu.fatec.iara;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

import java.util.ArrayList;
import java.util.List;

import br.edu.fatec.iara.model.Planta;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define a imersão na janela sem barras de sistema
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_home);

        // Aplicação dos insets para ajustar o padding e evitar sobreposição
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        // Criando uma lista de objetos Planta
        List<Planta> plantas = new ArrayList<>();
        plantas.add(new Planta("Manjericão", 24.5, 60, 1000, 40, "01/11/2024"));
        plantas.add(new Planta("Tomate", 22.3, 70, 900, 35, "01/11/2024"));
        plantas.add(new Planta("Alecrim", 25.0, 55, 1100, 45, "01/11/2024"));

        // Configurando o ListView com o PlantaAdapter
        PlantaAdapter adapter = new PlantaAdapter(this, plantas);
        ListView listView = findViewById(R.id.listaPlantas);
        listView.setAdapter(adapter);

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
}
